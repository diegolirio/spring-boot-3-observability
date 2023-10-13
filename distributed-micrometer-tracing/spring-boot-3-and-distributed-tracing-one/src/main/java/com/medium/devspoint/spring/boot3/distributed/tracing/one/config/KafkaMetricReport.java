package com.medium.devspoint.spring.boot3.distributed.tracing.one.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KafkaMetricReport implements MetricsReporter {

    private final Logger log = LoggerFactory.getLogger(KafkaMetricReport.class);

    public static final String PREFIX = "kafka.";
    private static final String TAG_KAFKA_METRIC_GROUP = "kafka-metric-group";
    private MeterRegistry meterRegistry;
    private Counter badMeasurement;

    public KafkaMetricReport() {
    }

    @Override
    public void init(List<KafkaMetric> metrics) {
        metrics.forEach(this::metricChange);
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.debug("configure: {}", configs);
        meterRegistry = (MeterRegistry) configs.get("micrometer.registry");
        Assert.notNull(meterRegistry, "Micrometer register must be available at micrometer.registry");

        badMeasurement = meterRegistry.counter(PREFIX + "badMeasurement");
    }

    @Override
    public void close() {
    }

    @Override
    public void metricChange(KafkaMetric metric) {
        log.debug("change: {}", metric.metricName());

        final List<Tag> tags =
                Stream.concat(
                                metric.metricName().tags().entrySet()
                                        .stream()
                                        .filter(entry -> StringUtils.hasText(entry.getValue()))
                                        .map(entry -> Tag.of(entry.getKey(), entry.getValue())),
                                Stream.of(Tag.of(TAG_KAFKA_METRIC_GROUP, metric.metricName().group()))
                        )
                        .collect(Collectors.toList());

        final String metricName = PREFIX + metric.metricName().name().replace('-', '_');

        meterRegistry.gauge(
                metricName,
                tags,
                metric,
                // lambda must used passed instance to leverage weak reference managed by the gauge
                metricObj ->
                {
                    try {
                        final double value = metricObj.measurable()
                                .measure(metricObj.config(), System.currentTimeMillis());
                        if (Double.isFinite(value)) {
                            return value;
                        }
                        else {
                            log.debug("Illegal measurement value {}={}", metricObj.metricName(), value);
                            badMeasurement.increment();
                            return 0;
                        }
                    } catch (IllegalStateException e) {
                        log.debug("Failed to measure {}", metricObj.metricName(), e);
                        badMeasurement.increment();
                        return 0;
                    }
                }
        );
    }

    @Override
    public void metricRemoval(KafkaMetric metric) {
        log.debug("remove: {}", metric);
    }
}
