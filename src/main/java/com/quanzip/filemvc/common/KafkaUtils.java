package com.quanzip.filemvc.common;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.support.SendResult;

@Configuration
public class KafkaUtils {
    private static final Logger logger = LoggerFactory.getLogger(KafkaUtils.class);
//    @Bean
//    public NewTopic getStatisticTopic(){
//        return new NewTopic("statistic", 2, (short)1);
//    }
//
//    @Bean
//    public NewTopic getEmailTopic(){
//        return new NewTopic("email", 2, (short)1);
//    }

    @Bean
    public NewTopic geUserTopic(){
        return new NewTopic("user", 2, (short)1);
    }

    @Bean
    public NewTopic getGroupTopic(){
        return new NewTopic("group", 2, (short)1);
    }

    @Bean
    public NewTopic getCourseTopic(){
        return new NewTopic("course", 2, (short)1);
    }
    @Bean
    public NewTopic getStudentTopic(){
        return new NewTopic("student", 2, (short)1);
    }
    @Bean
    public NewTopic getRoleTopic(){
        return new NewTopic("role", 2, (short)1);
    }

    @Bean
    public NewTopic getScoreTopic(){
        return new NewTopic("score", 2, (short)1);
    }

    public static KafkaSendCallback<String, Object> getStringObjectKafkaSendCallback(){
        KafkaSendCallback<String, Object> kafkaSendCallback = new KafkaSendCallback<String, Object>() {
            @Override
            public void onFailure(KafkaProducerException e) {
                logger.error(" kafka error when sending event: " + e.toString());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                assert result != null;
                logger.info("Kafka success when sending topic: " + result.getRecordMetadata().topic() + ", value: " + result.getProducerRecord().value().toString());
            }
        };
        return kafkaSendCallback;
    };
}
