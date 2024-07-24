package com.ecua3d.email.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "emlt_email_log", schema = "email")
@DynamicUpdate
public class EmailLogEntity extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generador1")
    @SequenceGenerator(name = "generador1", schema = "email", sequenceName = "email.emls_email_log", allocationSize = 1)
    @Column(name = "email_log_id")
    private Integer emailLogId;
    @Column(name = "type_email")
    private String typeEmail;
    @Column(name = "quote_id")
    private Integer quoteId;
    @Column(name = "status_email")
    private String statusEmail;
    private Integer chances;
    private String error;
}
