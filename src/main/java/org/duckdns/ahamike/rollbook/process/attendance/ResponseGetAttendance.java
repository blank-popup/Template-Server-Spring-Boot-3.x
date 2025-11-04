package org.duckdns.ahamike.rollbook.process.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ResponseGetAttendance {
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getDate();
    Long getUserId();
    String getUsername();
    String getName();
    String getTag();
    // String getTerminal();

    Long getFirstId();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime getFirstCreatedAt();
    String getFirstTerminal();
    Long getLastId();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime getLastCreatedAt();
    String getLastTerminal();
}
