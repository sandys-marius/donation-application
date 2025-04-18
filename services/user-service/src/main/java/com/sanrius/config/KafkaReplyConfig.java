package com.sanrius.config;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

public class KafkaReplyConfig {

    @Bean
    ReplyingKafkaTemplate<String, String, String> template(
            ProducerFactory<String, String> pf,
            ConcurrentKafkaListenerContainerFactory<String, String> factory) {

        ConcurrentMessageListenerContainer<String, String> replyContainer =
                factory.createContainer("replies");
        replyContainer.getContainerProperties().setGroupId("request.replies");
        ReplyingKafkaTemplate<String, String, String> template =
                new ReplyingKafkaTemplate<>(pf, replyContainer);
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

}
