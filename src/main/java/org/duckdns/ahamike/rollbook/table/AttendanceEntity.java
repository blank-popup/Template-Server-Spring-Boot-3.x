package org.duckdns.ahamike.rollbook.table;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tb_attendance")
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity extends AuditableCU {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "terminal_id")
    private Long terminalId;

    @Column(name = "tag_id")
    private Long tagId;
}
