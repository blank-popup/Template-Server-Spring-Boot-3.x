package org.duckdns.ahamike.rollbook.config.autitable;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseTimeDomain {
    protected Long id;
    protected Boolean isEnabled;
    protected String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime createdAt;
    protected String updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime updatedAt;
}
