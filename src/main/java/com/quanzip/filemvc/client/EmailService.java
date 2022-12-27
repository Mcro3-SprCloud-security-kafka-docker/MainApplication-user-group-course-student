package com.quanzip.filemvc.client;

import com.quanzip.filemvc.entity.Percent;
import com.quanzip.filemvc.service.dto.MailDTO;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.cfg.defs.EmailDef;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "mcro3-email-service", fallback = EmailServiceFallback.class)
public interface EmailService {
    @PostMapping(value = "/send")
    MailDTO sendMail(@RequestBody MailDTO mailDTO);

    @GetMapping(value = "/percent/up")
    ResponseEntity<List<Percent>> showMeasure();
}

@Component
@Log4j2
class EmailServiceFallback implements EmailService{

    @Override
    public MailDTO sendMail(MailDTO mailDTO) {
        log.error("Send mail error!");
        return null;
    }

    @Override
    public ResponseEntity<List<Percent>> showMeasure() {
        log.error("Can not get percent to show measure!!!");
        return null;
    }
}