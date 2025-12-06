package org.duckdns.ahamike.rollbook.config.autitable;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AuditableCUS implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "is_non_locked")
    protected Boolean isNonLocked;

    @Column(name = "is_non_expired")
    protected Boolean isNonExpired;

    @Column(name = "is_non_deleted")
    protected Boolean isNonDeleted;

    @Column(name = "is_enabled")
    protected Boolean isEnabled;

    @Column(name = "created_by", insertable = true, updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "created_at", insertable = true, updatable = false)
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime createdAt;
    
    @Column(name = "updated_by", insertable = true, updatable = true)
    @LastModifiedBy
    protected String updatedBy;

    @Column(name = "updated_at", insertable = true, updatable = true)
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.isNonLocked == null) {
            this.isNonLocked = true;
        }
        if (this.isNonExpired == null) {
            this.isNonExpired = true;
        }
        if (this.isNonDeleted == null) {
            this.isNonDeleted = true;
        }
        if (this.isEnabled == null) {
            this.isEnabled = true;
        }
    }
}
