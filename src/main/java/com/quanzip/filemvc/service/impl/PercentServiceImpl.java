package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.client.EmailService;
import com.quanzip.filemvc.service.PercentService;
import com.quanzip.filemvc.service.dto.PercentDTO;
import com.quanzip.filemvc.service.mapper.PercentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PercentServiceImpl implements PercentService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private PercentMapper percentMapper;

    @Override
    public List<PercentDTO> showMeasure() {
        return percentMapper.toDtos(emailService.showMeasure().getBody());
    }
}
