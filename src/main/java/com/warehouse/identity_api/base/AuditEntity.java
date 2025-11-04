package com.warehouse.identity_api.base;

import java.time.LocalDateTime;

public interface AuditEntity {
  void setCreatedAt(LocalDateTime createdAt);

  void setCreatedBy(Long createdBy);

  void setUpdatedAt(LocalDateTime updatedAt);

  void setUpdatedBy(Long updatedBy);

  void setDeletedBy(Long deletedBy);

  LocalDateTime getDeletedAt();

  void setDeletedAt(LocalDateTime deletedAt);
}
