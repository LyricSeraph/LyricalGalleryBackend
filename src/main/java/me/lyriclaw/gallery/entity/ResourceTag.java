package me.lyriclaw.gallery.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "ResourceTag")
public class ResourceTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id", nullable = false, updatable = false)
    private Long tagId;

    @Column(name = "resource_id", nullable = false, updatable = false)
    private Long resourceId;

}
