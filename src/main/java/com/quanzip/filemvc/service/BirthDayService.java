package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.BirthDayDTO;

import java.util.List;

public interface BirthDayService {
    List<BirthDayDTO> getAllUserHasBirthDayThisWeek();
}
