package com.warehouse.identity_api.entities;

import com.warehouse.identity_api.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "identity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Identity implements AuditEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_id", nullable = false)
  Long userId;

  @Column(name = "email", nullable = false, unique = true)
  String email;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "refresh_token")
  String refreshToken;

  @Column(name = "expired")
  LocalDateTime expired;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "created_by")
  Long createdBy;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @Column(name = "updated_by")
  Long updatedBy;

  @Column(name = "deleted_at")
  LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  Long deletedBy;
}
