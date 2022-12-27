package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.StatisticDTO;

public interface KafkaService {
    void sendStatisticByKafka(String message, String clazz);
    void sendStatisticByService(String message, String clazz);
    void sendMail(String message);
}
