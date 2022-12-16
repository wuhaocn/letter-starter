package org.letter.perform.jmx;

import io.prometheus.client.Collector;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * JmxCollector
 *
 * @author wuhao
 */
public class JmxCollector extends Collector implements Collector.Describable {


	private static final Logger LOGGER = Logger.getLogger(JmxCollector.class.getName());

	static class Rule {
		Pattern pattern;
		String name;
		String value;
		Double valueFactor = 1.0;
		String help;
		boolean attrNameSnakeCase;
		boolean cache = false;
		Type type = Type.UNKNOWN;
		ArrayList<String> labelNames;
		ArrayList<String> labelValues;
	}

	private static class Config {
		Integer startDelaySeconds = 0;
		boolean lowercaseOutputName;
		boolean lowercaseOutputLabelNames;
		List<ObjectName> whitelistObjectNames = new ArrayList<ObjectName>();
		List<ObjectName> blacklistObjectNames = new ArrayList<ObjectName>();
		List<Rule> rules = new ArrayList<Rule>();
		long lastUpdate = 0L;

		MatchedRulesCache rulesCache;
	}

	private Config config;
	private long createTimeNanoSecs = System.nanoTime();

	private final JmxMBeanPropertyCache jmxMBeanPropertyCache = new JmxMBeanPropertyCache();

	public JmxCollector() throws MalformedObjectNameException {
		config = new Config();
	}


	private synchronized Config getLatestConfig() {
		return config;
	}

	static String toSnakeAndLowerCase(String attrName) {
		if (attrName == null || attrName.isEmpty()) {
			return attrName;
		}
		char firstChar = attrName.subSequence(0, 1).charAt(0);
		boolean prevCharIsUpperCaseOrUnderscore = Character.isUpperCase(firstChar) || firstChar == '_';
		StringBuilder resultBuilder = new StringBuilder(attrName.length()).append(Character.toLowerCase(firstChar));
		for (char attrChar : attrName.substring(1).toCharArray()) {
			boolean charIsUpperCase = Character.isUpperCase(attrChar);
			if (!prevCharIsUpperCaseOrUnderscore && charIsUpperCase) {
				resultBuilder.append("_");
			}
			resultBuilder.append(Character.toLowerCase(attrChar));
			prevCharIsUpperCaseOrUnderscore = charIsUpperCase || attrChar == '_';
		}
		return resultBuilder.toString();
	}

	/**
	 * Change invalid chars to underscore, and merge underscores.
	 *
	 * @param name Input string
	 * @return
	 */
	static String safeName(String name) {
		if (name == null) {
			return null;
		}
		boolean prevCharIsUnderscore = false;
		StringBuilder safeNameBuilder = new StringBuilder(name.length());
		if (!name.isEmpty() && Character.isDigit(name.charAt(0))) {
			// prevent a numeric prefix.
			safeNameBuilder.append("_");
		}
		for (char nameChar : name.toCharArray()) {
			boolean isUnsafeChar = !JmxCollector.isLegalCharacter(nameChar);
			if ((isUnsafeChar || nameChar == '_')) {
				if (prevCharIsUnderscore) {
					continue;
				} else {
					safeNameBuilder.append("_");
					prevCharIsUnderscore = true;
				}
			} else {
				safeNameBuilder.append(nameChar);
				prevCharIsUnderscore = false;
			}
		}

		return safeNameBuilder.toString();
	}

	private static boolean isLegalCharacter(char input) {
		return ((input == ':') ||
				(input == '_') ||
				(input >= 'a' && input <= 'z') ||
				(input >= 'A' && input <= 'Z') ||
				(input >= '0' && input <= '9'));
	}

	class Receiver implements JmxScraper.MBeanReceiver {
		Map<String, MetricFamilySamples> metricFamilySamplesMap =
				new HashMap<String, MetricFamilySamples>();

		Config config;
		MatchedRulesCache.StalenessTracker stalenessTracker;

		private static final char SEP = '_';

		Receiver(Config config, MatchedRulesCache.StalenessTracker stalenessTracker) {
			this.config = config;
			this.stalenessTracker = stalenessTracker;
		}

		// [] and () are special in regexes, so swtich to <>.
		private String angleBrackets(String s) {
			return "<" + s.substring(1, s.length() - 1) + ">";
		}

		void addSample(MetricFamilySamples.Sample sample, Type type, String help) {
			MetricFamilySamples mfs = metricFamilySamplesMap.get(sample.name);
			if (mfs == null) {
				// JmxScraper.MBeanReceiver is only called from one thread,
				// so there's no race here.
				mfs = new MetricFamilySamples(sample.name, type, help, new ArrayList<MetricFamilySamples.Sample>());
				metricFamilySamplesMap.put(sample.name, mfs);
			}
			MetricFamilySamples.Sample existing = findExisting(sample, mfs);
			if (existing != null) {
				String labels = "{";
				for (int i = 0; i < existing.labelNames.size(); i++) {
					labels += existing.labelNames.get(i) + "=" + existing.labelValues.get(i) + ",";
				}
				labels += "}";
				LOGGER.fine("Metric " + existing.name + labels + " was created multiple times. Keeping the first occurrence. Dropping the others.");
			} else {
				mfs.samples.add(sample);
			}
		}

		private MetricFamilySamples.Sample findExisting(MetricFamilySamples.Sample sample, MetricFamilySamples mfs) {
			for (MetricFamilySamples.Sample existing : mfs.samples) {
				if (existing.name.equals(sample.name) && existing.labelValues.equals(sample.labelValues) && existing.labelNames.equals(sample.labelNames)) {
					return existing;
				}
			}
			return null;
		}

		// Add the matched rule to the cached rules and tag it as not stale
		// if the rule is configured to be cached
		private void addToCache(final Rule rule, final String cacheKey, final MatchedRule matchedRule) {
			if (rule.cache) {
				config.rulesCache.put(rule, cacheKey, matchedRule);
				stalenessTracker.add(rule, cacheKey);
			}
		}

		private MatchedRule defaultExport(
				String matchName,
				String domain,
				LinkedHashMap<String, String> beanProperties,
				LinkedList<String> attrKeys,
				String attrName,
				String help,
				Double value,
				double valueFactor,
				Type type) {
			StringBuilder name = new StringBuilder();
			name.append(domain);
			if (beanProperties.size() > 0) {
				name.append(SEP);
				name.append(beanProperties.values().iterator().next());
			}
			for (String k : attrKeys) {
				name.append(SEP);
				name.append(k);
			}
			name.append(SEP);
			name.append(attrName);
			String fullname = safeName(name.toString());

			if (config.lowercaseOutputName) {
				fullname = fullname.toLowerCase();
			}

			List<String> labelNames = new ArrayList<String>();
			List<String> labelValues = new ArrayList<String>();
			if (beanProperties.size() > 1) {
				Iterator<Map.Entry<String, String>> iter = beanProperties.entrySet().iterator();
				// Skip the first one, it's been used in the name.
				iter.next();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String labelName = safeName(entry.getKey());
					if (config.lowercaseOutputLabelNames) {
						labelName = labelName.toLowerCase();
					}
					labelNames.add(labelName);
					labelValues.add(entry.getValue());
				}
			}

			return new MatchedRule(fullname, matchName, type, help, labelNames, labelValues, value, valueFactor);
		}

		@Override
		public void recordBean(
				String domain,
				LinkedHashMap<String, String> beanProperties,
				LinkedList<String> attrKeys,
				String attrName,
				String attrType,
				String attrDescription,
				Object beanValue) {

			String beanName = domain + angleBrackets(beanProperties.toString()) + angleBrackets(attrKeys.toString());

			// Build the HELP string from the bean metadata.
			String help = domain + ":name=" + beanProperties.get("name") + ",type=" + beanProperties.get("type") + ",attribute=" + attrName;
			// Add the attrDescription to the HELP if it exists and is useful.
			if (attrDescription != null && !attrDescription.equals(attrName)) {
				help = attrDescription + " " + help;
			}

			String attrNameSnakeCase = toSnakeAndLowerCase(attrName);

			MatchedRule matchedRule = MatchedRule.unmatched();

			for (Rule rule : config.rules) {
				// Rules with bean values cannot be properly cached (only the value from the first scrape will be cached).
				// If caching for the rule is enabled, replace the value with a dummy <cache> to avoid caching different values at different times.
				Object matchBeanValue = rule.cache ? "<cache>" : beanValue;

				String matchName = beanName + (rule.attrNameSnakeCase ? attrNameSnakeCase : attrName) + ": " + matchBeanValue;

				if (rule.cache) {
					MatchedRule cachedRule = config.rulesCache.get(rule, matchName);
					if (cachedRule != null) {
						stalenessTracker.add(rule, matchName);
						if (cachedRule.isMatched()) {
							matchedRule = cachedRule;
							break;
						}

						// The bean was cached earlier, but did not match the current rule.
						// Skip it to avoid matching against the same pattern again
						continue;
					}
				}

				Matcher matcher = null;
				if (rule.pattern != null) {
					matcher = rule.pattern.matcher(matchName);
					if (!matcher.matches()) {
						addToCache(rule, matchName, MatchedRule.unmatched());
						continue;
					}
				}

				Double value = null;
				if (rule.value != null && !rule.value.isEmpty()) {
					String val = matcher.replaceAll(rule.value);
					try {
						value = Double.valueOf(val);
					} catch (NumberFormatException e) {
						LOGGER.fine("Unable to parse configured value '" + val + "' to number for bean: " + beanName + attrName + ": " + beanValue);
						return;
					}
				}

				// If there's no name provided, use default export format.
				if (rule.name == null) {
					matchedRule = defaultExport(matchName, domain, beanProperties, attrKeys, rule.attrNameSnakeCase ? attrNameSnakeCase : attrName, help, value, rule.valueFactor, rule.type);
					addToCache(rule, matchName, matchedRule);
					break;
				}

				// Matcher is set below here due to validation in the constructor.
				String name = safeName(matcher.replaceAll(rule.name));
				if (name.isEmpty()) {
					return;
				}
				if (config.lowercaseOutputName) {
					name = name.toLowerCase();
				}

				// Set the help.
				if (rule.help != null) {
					help = matcher.replaceAll(rule.help);
				}

				// Set the labels.
				ArrayList<String> labelNames = new ArrayList<String>();
				ArrayList<String> labelValues = new ArrayList<String>();
				if (rule.labelNames != null) {
					for (int i = 0; i < rule.labelNames.size(); i++) {
						final String unsafeLabelName = rule.labelNames.get(i);
						final String labelValReplacement = rule.labelValues.get(i);
						try {
							String labelName = safeName(matcher.replaceAll(unsafeLabelName));
							String labelValue = matcher.replaceAll(labelValReplacement);
							if (config.lowercaseOutputLabelNames) {
								labelName = labelName.toLowerCase();
							}
							if (!labelName.isEmpty() && !labelValue.isEmpty()) {
								labelNames.add(labelName);
								labelValues.add(labelValue);
							}
						} catch (Exception e) {
							throw new RuntimeException(
									format("Matcher '%s' unable to use: '%s' value: '%s'", matcher, unsafeLabelName, labelValReplacement), e);
						}
					}
				}

				matchedRule = new MatchedRule(name, matchName, rule.type, help, labelNames, labelValues, value, rule.valueFactor);
				addToCache(rule, matchName, matchedRule);
				break;
			}

			if (matchedRule.isUnmatched()) {
				return;
			}

			Number value;
			if (matchedRule.value != null) {
				beanValue = matchedRule.value;
			}

			if (beanValue instanceof Number) {
				value = ((Number) beanValue).doubleValue() * matchedRule.valueFactor;
			} else if (beanValue instanceof Boolean) {
				value = (Boolean) beanValue ? 1 : 0;
			} else {
				LOGGER.fine("Ignoring unsupported bean: " + beanName + attrName + ": " + beanValue);
				return;
			}

			// Add to samples.
			LOGGER.fine("add metric sample: " + matchedRule.name + " " + matchedRule.labelNames + " " + matchedRule.labelValues + " " + value.doubleValue());
			addSample(new MetricFamilySamples.Sample(matchedRule.name, matchedRule.labelNames, matchedRule.labelValues, value.doubleValue()), matchedRule.type, matchedRule.help);
		}

	}

	@Override
	public List<MetricFamilySamples> collect() {
		// Take a reference to the current config and collect with this one
		// (to avoid race conditions in case another thread reloads the config in the meantime)
		Config config = getLatestConfig();

		MatchedRulesCache.StalenessTracker stalenessTracker = new MatchedRulesCache.StalenessTracker();
		Receiver receiver = new Receiver(config, stalenessTracker);
		JmxScraper scraper = new JmxScraper(config.whitelistObjectNames, config.blacklistObjectNames, receiver, jmxMBeanPropertyCache);
		long start = System.nanoTime();
		double error = 0;
		if ((config.startDelaySeconds > 0) &&
				((start - createTimeNanoSecs) / 1000000000L < config.startDelaySeconds)) {
			throw new IllegalStateException("JMXCollector waiting for startDelaySeconds");
		}
		try {
			scraper.doScrape();
		} catch (Exception e) {
			error = 1;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.severe("JMX scrape failed: " + sw.toString());
		}
		config.rulesCache.evictStaleEntries(stalenessTracker);

		List<MetricFamilySamples> mfsList = new ArrayList<MetricFamilySamples>();
		mfsList.addAll(receiver.metricFamilySamplesMap.values());
		List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
		samples.add(new MetricFamilySamples.Sample(
				"jmx_scrape_duration_seconds", new ArrayList<String>(), new ArrayList<String>(), (System.nanoTime() - start) / 1.0E9));
		mfsList.add(new MetricFamilySamples("jmx_scrape_duration_seconds", Type.GAUGE, "Time this JMX scrape took, in seconds.", samples));

		samples = new ArrayList<MetricFamilySamples.Sample>();
		samples.add(new MetricFamilySamples.Sample(
				"jmx_scrape_error", new ArrayList<String>(), new ArrayList<String>(), error));
		mfsList.add(new MetricFamilySamples("jmx_scrape_error", Type.GAUGE, "Non-zero if this scrape failed.", samples));
		samples = new ArrayList<MetricFamilySamples.Sample>();
		samples.add(new MetricFamilySamples.Sample(
				"jmx_scrape_cached_beans", new ArrayList<String>(), new ArrayList<String>(), stalenessTracker.cachedCount()));
		mfsList.add(new MetricFamilySamples("jmx_scrape_cached_beans", Type.GAUGE, "Number of beans with their matching rule cached", samples));
		return mfsList;
	}

	@Override
	public List<MetricFamilySamples> describe() {
		List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();
		sampleFamilies.add(new MetricFamilySamples("jmx_scrape_duration_seconds", Type.GAUGE, "Time this JMX scrape took, in seconds.", new ArrayList<MetricFamilySamples.Sample>()));
		sampleFamilies.add(new MetricFamilySamples("jmx_scrape_error", Type.GAUGE, "Non-zero if this scrape failed.", new ArrayList<MetricFamilySamples.Sample>()));
		sampleFamilies.add(new MetricFamilySamples("jmx_scrape_cached_beans", Type.GAUGE, "Number of beans with their matching rule cached", new ArrayList<MetricFamilySamples.Sample>()));
		return sampleFamilies;
	}

}
