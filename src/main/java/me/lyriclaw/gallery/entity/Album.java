package me.lyriclaw.gallery.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractAuditable_;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "Album")
public class Album extends AbstractAuditable_ implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

}
