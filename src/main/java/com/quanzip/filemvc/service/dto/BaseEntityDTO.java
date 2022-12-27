package com.quanzip.filemvc.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntityDTO {
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}
