package com.quanzip.filemvc.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static LocalDateTime getLocalDateTime(){
        return LocalDateTime.now();
    }

    public static String getDateNowToString(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"));
    }
}
