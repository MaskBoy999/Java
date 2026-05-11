package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    protected void onCreate(Object entity) {
        if (entity instanceof Auditable auditable) {
            LocalDateTime now = LocalDateTime.now();
            if (auditable.getCreatedAt() == null) {
                auditable.setCreatedAt(now);
            }
            if (auditable.getUpdatedAt() == null) {
                auditable.setUpdatedAt(now);
            }
        }
    }

    @PreUpdate
    protected void onUpdate(Object entity) {
        if (entity instanceof Auditable auditable) {
            auditable.setUpdatedAt(LocalDateTime.now());
        }
    }
}
