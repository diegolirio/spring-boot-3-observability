package com.medium.devspoint.spring.boot3.distributed.tracing.one.config;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.internals.KafkaProducerMetrics;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerProducerListener;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.streams.KafkaStreamsMicrometerListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {


    @Autowired
    private MeterRegistry meterRegistry;

    @Bean
    public ProducerFactory<String, String> producerFactory() {

        //io.micrometer.core.instrument.binder.kafka.KafkaProducerApiMetrics

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "oneClientId");

        //configProps.put("metric.reporters", KafkaStreamsMicrometerListener.class.getName());
        //configProps.put(ConsumerConfig.METRIC_REPORTER_CLASSES_CONFIG, "io.micrometer.core.instrument.binder.kafka.KafkaProducerApiMetrics" );
        //props.put(CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG, KafkaMetricReporter.class.getCanonicalName());
        //configProps.put(CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG, KafkaClientMetrics.class.getCanonicalName());

        //configProps.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, KafkaMetricReport.class.getCanonicalName());
        //configProps.put("micrometer.registry", meterRegistry);

        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(configProps);
//        pf.addListener(new MicrometerProducerListener<>(
//                meterRegistry,
//                Collections.singletonList(new ImmutableTag("producer-detail", "customTagValue")))
//        );


        return pf;
//        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setMicrometerEnabled(true);
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }


}
