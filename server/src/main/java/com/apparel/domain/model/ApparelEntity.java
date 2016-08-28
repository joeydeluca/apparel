package com.apparel.domain.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mick on 4/16/2016.
 */
@MappedSuperclass
public abstract class ApparelEntity {
    @Id
    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", insertable = false)
    private Date modifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    private int version = 0;

    private boolean markedForDelete = false;

    @PrePersist
    public void prePersist(){
        this.setCreatedDate(new Date());
    }

    @PreUpdate
    public void preUpdate(){
        this.setModifiedDate(new Date());
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

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
}
