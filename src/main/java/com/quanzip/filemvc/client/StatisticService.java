package com.quanzip.filemvc.client;

import com.quanzip.filemvc.service.dto.StatisticDTO;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mcro3-statistic-service", fallback = StatisticFallback.class)
public interface StatisticService {
    @PostMapping(value = "/statistic")
    void saveStatistic(@RequestBody StatisticDTO statisticDTO);
}

@Component
@Log4j2
class StatisticFallback implements StatisticService{

    @Override
    public void saveStatistic(com.quanzip.filemvc.service.dto.StatisticDTO statisticDTO) {
        log.error("Save statistic error!");
    }
}
