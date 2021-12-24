package me.lyriclaw.gallery.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.AbstractAuditable_;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Data
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Album")
public class Album extends AbstractAuditable_ implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "album_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "cover_id")
    private Long coverId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Album album = (Album) o;
        return albumId != null && Objects.equals(albumId, album.albumId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected boolean canEqual(Object other) {
        return other instanceof Album;
    }
}
