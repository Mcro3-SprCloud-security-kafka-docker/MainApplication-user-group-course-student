package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.client.EmailService;
import com.quanzip.filemvc.client.StatisticService;
import com.quanzip.filemvc.common.KafkaUtils;
import com.quanzip.filemvc.common.NetWorkUtils;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.dto.MailDTO;
import com.quanzip.filemvc.service.dto.StatisticDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {
    private final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StatisticService statisticService;

    @Override
    public void sendStatisticByKafka(String message, String clazz) {

        // calling statistic-service
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setClazz(clazz);
        statisticDTO.setCreateBy("quanph20");
        statisticDTO.setMessage(message);
        kafkaTemplate.send("user", statisticDTO).addCallback(KafkaUtils.getStringObjectKafkaSendCallback());
    }

    @Override
    public void sendStatisticByService(String message, String clazz) {
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setClazz(clazz);
        statisticDTO.setCreateBy("quanph20");
        statisticDTO.setMessage(message);
        statisticService.saveStatistic(statisticDTO);
    }

    @Override
    public void sendMail(String message) {
        // calling email service
        MailDTO mailDTO = new MailDTO();
        mailDTO.setFromMail("quanph1998@gmail.com");
        mailDTO.setToMail("quanaqtzip4@gmail.com");
        mailDTO.setFromName("Pham hong quanaqt");
        mailDTO.setToName("Pham hong quanzip");
        mailDTO.setSubject("Hello from project 3");
        mailDTO.setContent(message);

        if (NetWorkUtils.checkNetworkAvailable()) emailService.sendMail(mailDTO);
        else {
            // network failed to connect
            mailDTO.setResult("F");
            kafkaTemplate.send("email", mailDTO);
        }

    }
}
