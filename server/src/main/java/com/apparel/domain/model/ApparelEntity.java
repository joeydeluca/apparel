package com.apparel.domain.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mick on 4/16/2016.
 */
@MappedSuperclass
public abstract class ApparelEntity {
    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", insertable = false)
    private Date modifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @Version
    private Integer version;

    @PrePersist
    public void prePersist(){
        this.setCreatedDate(new Date());
    }

    @PreUpdate
    public void preUpdate(){
        this.setModifiedDate(new Date());
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

}
