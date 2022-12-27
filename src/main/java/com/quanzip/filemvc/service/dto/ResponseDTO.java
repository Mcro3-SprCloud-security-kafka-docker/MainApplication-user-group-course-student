package com.quanzip.filemvc.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDTO <T>{
    private int status;
    private String error;
    private T data;
}
