package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.ScoreDTO;

public interface ScoreService {
    void saveScore(ScoreDTO scoreDTO) throws Exception;

    void deleteByScoreId(Long scoreId) throws Exception;
}
