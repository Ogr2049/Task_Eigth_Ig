package com.example.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String myBootstrapServers;
    
    @Bean
    public ConsumerFactory<String, Object> myConsumerFactory() {
        Map<String, Object> myConfigProps = new HashMap<>();
        myConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, myBootstrapServers);
        myConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        myConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        myConfigProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        myConfigProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        myConfigProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        return new DefaultKafkaConsumerFactory<>(myConfigProps);
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> myKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> myFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        myFactory.setConsumerFactory(myConsumerFactory());
        return myFactory;
    }
}