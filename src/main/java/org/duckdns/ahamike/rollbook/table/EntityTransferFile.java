package org.duckdns.ahamike.rollbook.table;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableC;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
public class EntityTransferFile extends AuditableC {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "active")
    private Long active;

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = 100L;
        }
    }

    public EntityTransferFile(String subDirectory, String filename, String originalFilename, String description, String directory0, String directory1, String directory2, String mimeType, String mimeSubtype, String mimeParameter) {
        this.subDirectory = subDirectory;
        this.filename = filename;
        this.originalFilename = originalFilename;
        this.description = description;
        this.directory0 = directory0;
        this.directory1 = directory1;
        this.directory2 = directory2;
        this.mimeType = mimeType;
        this.mimeSubtype = mimeSubtype;
        this.mimeParameter = mimeParameter;
    }
}
