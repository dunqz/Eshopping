package com.crud.crudfrontendbackend.auditable;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class Auditable {
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @Column(name = "updated_by")
    private String updatedBy;

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        this.dateCreated = LocalDateTime.now();
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        this.dateUpdated = LocalDateTime.now();
    }

    // Override the JPA save operation to automatically update the date
    @PrePersist
    public void prePersist() {
        if (dateCreated == null) {
            dateCreated = LocalDateTime.now();
        }
        if (dateUpdated == null) {
            dateUpdated = LocalDateTime.now();
        }
    }

    // Override the JPA update operation to automatically update the date
    @PreUpdate
    public void preUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}