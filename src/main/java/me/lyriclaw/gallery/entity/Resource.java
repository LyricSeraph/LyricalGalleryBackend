package me.lyriclaw.gallery.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "Resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "failed_tries", insertable = false, nullable = false)
    private Integer failedTries;

    /**
     * idle: 0, downloading: 1, finished: 2
     */
    @Column(name = "status", nullable = false)
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2")
    private Integer status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

    public String getStorageFilename() {
        return getUuid() + getExtension();
    }
}
