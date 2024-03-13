package ru.jsms.backend.files.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "file_metadata")
public class FileMetadataEntity extends BaseOwneredEntity<Long> implements Serializable {

    @Column(unique = true)
    private UUID uuid;

    private String name;

    private long size;
}
