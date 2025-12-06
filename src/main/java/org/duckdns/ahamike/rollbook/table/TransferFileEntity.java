package org.duckdns.ahamike.rollbook.table;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(
    name = "tb_transfer_file",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_FILEPATH",
            columnNames = {
                "sub_directory",
                "filename"
            }
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class TransferFileEntity extends AuditableCU {

    @Column(name = "sub_directory", nullable = false)
    private String subDirectory;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "description")
    private String description;

    @Column(name = "directory0")
    private String directory0;

    @Column(name = "directory1")
    private String directory1;

    @Column(name = "directory2")
    private String directory2;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "mime_subtype")
    private String mimeSubtype;

    @Column(name = "mime_parameter")
    private String mimeParameter;
}
