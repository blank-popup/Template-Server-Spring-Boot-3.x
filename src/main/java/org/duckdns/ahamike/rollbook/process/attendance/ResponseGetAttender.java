package org.duckdns.ahamike.rollbook.process.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ResponseGetAttender {
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getDate();
    Long getIdUser();
    String getUsername();
    String getName();
    String getTag();
    String getTerminal();

    Long getIdFirst();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime getCreatedAtFirst();
    String getTerminalFirst();
    Long getIdLast();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime getCreatedAtLast();
    String getTerminalLast();
}
