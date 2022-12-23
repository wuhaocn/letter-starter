package org.letter.perfmon.jmx.collector;

import io.prometheus.client.Collector.Type;

import java.util.List;

/**
 * MatchedRule is the result of matching a JMX bean against the rules present in the configuration file.
 * As rules are matched using regular expressions, caching helps prevent having to match the same beans to the same list
 * of regular expressions.
 *
 * @author letter
 */
public class MatchedRule {
    final String name;
    final String matchName;
    final Type type;
    final String help;
    final List<String> labelNames;
    final List<String> labelValues;
    final Double value;
    final double valueFactor;
	final boolean invalid;

    private static final MatchedRule _unmatched = new MatchedRule();

    private MatchedRule() {
        this.name = null;
        this.matchName = null;
        this.type = null;
        this.help = null;
        this.labelNames = null;
        this.labelValues = null;
        this.value = null;
        this.valueFactor = 1.0;
		this.invalid = false;
    }

    public MatchedRule(
            final String name,
            final String matchName,
            final Type type,
            final String help,
            final List<String> labelNames,
            final List<String> labelValues,
            final Double value,
            double valueFactor,
			boolean isInvalid) {
        this.name = name;
        this.matchName = matchName;
        this.type = type;
        this.help = help;
        this.labelNames = labelNames;
        this.labelValues = labelValues;
        this.value = value;
        this.valueFactor = valueFactor;
		this.invalid = isInvalid;
    }

    /**
     * A unmatched MatchedRule, used when no rule matching a JMX bean has been found in the configuration.
     * Cached unmatched rules are still a cache hit, that will not produce any metric/value.
     * @return the invalid rule
     */
    public static MatchedRule unmatched() {
        return _unmatched;
    }

    public boolean isUnmatched() {
		if (invalid){
			return true;
		}
        return this == _unmatched;
    }

    public boolean isMatched() {
		if (!invalid){
			return true;
		}
        return !isUnmatched();
    }
}
