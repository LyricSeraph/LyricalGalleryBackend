package me.lyriclaw.gallery.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "resource_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(name = "uuid", insertable = false, updatable = false)
    private String uuid;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "ratio", nullable = false)
    private Float ratio;

    @Column(name = "s_thumb", insertable = false)
    private String sThumb;

    @Column(name = "m_thumb", insertable = false)
    private String mThumb;

    @Column(name = "l_thumb", insertable = false)
    private String lThumb;

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

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToMany(targetEntity = ResourceTag.class, mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ResourceTag> tags;

    public String getStorageFilename() {
        return getUuid() + getExtension();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Resource resource = (Resource) o;
        return resourceId != null && Objects.equals(resourceId, resource.resourceId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    protected boolean canEqual(Object other) {
        return other instanceof Resource;
    }
}
