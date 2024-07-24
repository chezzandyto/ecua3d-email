package com.ecua3d.email.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    @Column(name = "status")
    private String status = "1";

    @PrePersist
    public void setAuditToNew() {
        this.lastModifiedDate = new Date();
        this.status = "1";
    }

    @PreUpdate
    public void setAuditToUpdate() {
        this.lastModifiedDate = new Date();
        this.status = "1";
    }
}
