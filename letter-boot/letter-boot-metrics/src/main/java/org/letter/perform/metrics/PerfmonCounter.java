package org.letter.perform.metrics;

import com.codahale.metrics.*;
import org.letter.perform.jmx.metrics.MetricRegistryManager;
import org.letter.perform.jmx.metrics.MetricsConstant;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

/**
 * PerfmonCounter
 *
 * @author wuhao
 */
public class PerfmonCounter {
	private PerfmonCounter() {
	}

	private static String name(String metric, String scope, String[] names) {
		String prefix = MetricRegistry.name(scope, metric);
		if (names == null || names.length < 1) {
			return prefix;
		}
		return MetricRegistry.name(prefix, names);
	}

	private static MetricRegistry registry() {
		return MetricRegistryManager.getInstance().getMetricRegistry();
	}

	public static Pooler pooler(String scope, String... names) {
		return new Pooler(scope, names);
	}

	public static Timer timer(String scope, String... names) {
		return new Timer(registry(), scope, names);
	}

	public static Counter counter(String scope, String... names) {
		return registry().counter(name(MetricsConstant.COUNTER, scope, names));
	}

	public static Meter meter(String scope, String... names) {
		return registry().meter(name(MetricsConstant.METER, scope, names));
	}

	public static Histogram histogram(String scope, String... names) {
		return registry().histogram(name(MetricsConstant.HISTOGRAM, scope, names));
	}

	public static <T> void gauge(Gauge<T> metric, String scope, String... names) {
		registry().register(name(MetricsConstant.GAUGE, scope, names), metric);
	}

	public static <T> Gauge gaugeGetOrAdd(Gauge<T> metric, String scope, String... names) {
		String name = name(MetricsConstant.GAUGE, scope, names);
		return registry().gauge(name, () -> metric);
	}

	public static <T> void gaugeRemove(String scope, String... names) {
		String name = name(MetricsConstant.GAUGE, scope, names);
		registry().remove(name);
	}

	public static class Pooler {
		private static String[] append(String name, String... names) {
			return Stream.concat(Stream.of(names), Stream.of(name)).toArray(n -> new String[n]);
		}

		private final Meter _incr;
		private final Meter _decr;
		private final Counter _count;

		Pooler(String scope, String... names) {
			this._incr = PerfmonCounter.meter(scope, append("incr", names));
			this._decr = PerfmonCounter.meter(scope, append("decr", names));
			this._count = PerfmonCounter.counter(scope, append("size", names));
		}

		public void inc() {
			this.inc(1);
		}

		public void inc(long n) {
			this._incr.mark(n);
			this._count.inc(n);
		}

		public void dec() {
			this.dec(1);
		}

		public void dec(long n) {
			this._decr.mark(n);
			this._count.dec(n);
		}
	}

	public static class Timer {
		private final com.codahale.metrics.Timer _timer;
		private final Meter _meter;
		private final Counter _counter;

		Timer(MetricRegistry registry, String scope, String... names) {
			this._timer = registry.timer(name(MetricsConstant.TIMER, scope, names));
			this._meter = PerfmonCounter.meter(scope, names);
			this._counter = PerfmonCounter.counter(scope, names);
		}

		/**
		 * 手工设置耗时
		 *
		 * @param duration
		 * @param unit
		 */
		public void setCostTime(long duration, TimeUnit unit) {
			this._meter.mark();
			this._counter.inc();
			this._timer.update(duration, unit);
		}

		public Context time() {
			return this.time((LongConsumer) null);
		}

		public Context time(LongConsumer elapsed) {
			this._meter.mark();
			this._counter.inc();
			return new Context(this._timer.time(), this._counter, elapsed);
		}

		public void time(Runnable event) {
			this.time(event, null);
		}

		public void time(Runnable event, LongConsumer elapsed) {
			try (Context ctx = this.time(elapsed)) {
				event.run();
			}
		}

		public <T> T time(Callable<T> event) throws Exception {
			return time(event, null);
		}

		public <T> T time(Callable<T> event, LongConsumer elapsed) throws Exception {
			try (Context ctx = this.time(elapsed)) {
				return event.call();
			}
		}

		public static class Context implements AutoCloseable {
			private final com.codahale.metrics.Timer.Context _context;
			private final Counter _counter;
			private LongConsumer _elapsed;

			Context(com.codahale.metrics.Timer.Context context, Counter counter, LongConsumer consume) {
				this._context = context;
				this._counter = counter;
				this._elapsed = consume;
			}

			public long stop() {
				this._counter.dec();
				long elapsed = this._context.stop();

				LongConsumer consume = this._elapsed;
				if (consume != null) {
					consume.accept(elapsed);
				}
				return elapsed;
			}

			@Override
			public void close() {
				this.stop();
			}
		}
	}
}
