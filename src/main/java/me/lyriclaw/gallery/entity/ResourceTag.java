package me.lyriclaw.gallery.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ResourceTag")
public class ResourceTag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rt_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rtId;

    @Column(name = "tag_id", nullable = false, updatable = false)
    private Long tagId;

    @Column(name = "resource_id", nullable = false, updatable = false)
    private Long resourceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourceTag that = (ResourceTag) o;
        return rtId != null && Objects.equals(rtId, that.rtId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
