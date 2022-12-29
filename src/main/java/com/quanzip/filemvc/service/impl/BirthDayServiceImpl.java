package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.BirthDay;
import com.quanzip.filemvc.repository.BirthDayRepository;
import com.quanzip.filemvc.service.BirthDayService;
import com.quanzip.filemvc.service.dto.BirthDayDTO;
import com.quanzip.filemvc.service.mapper.BirthDayMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BirthDayServiceImpl implements BirthDayService {
    @Autowired
    private BirthDayRepository birthDayRepository;

    @Autowired
    private BirthDayMapper birthDayMapper;

    @Override
    public List<BirthDayDTO> getAllUserHasBirthDayThisWeek() {
        List<BirthDay> birthDays = birthDayRepository.findAll();
        if (birthDays.size() == 0) return Collections.emptyList();
        final LocalDate localDate = LocalDate.now();
        List<BirthDayDTO> birthDayDTOs = birthDayMapper.toDtos(birthDays).stream()
                .peek(birthDayDTO -> birthDayDTO.setStatus(birthDayDTO.getBirthDate().compareTo(localDate)))
                .collect(Collectors.toList());
        return birthDayDTOs;
    }
}
