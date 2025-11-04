package com.warehouse.identity_api.dto.req;

import com.warehouse.identity_api.validation.FieldNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDTO {
  @FieldNotEmpty(field = "User ID")
  Long userId;

  @FieldNotEmpty(field = "Email")
  String email;

  @FieldNotEmpty(field = "Password")
  String password;

  String refreshToken;

  LocalDateTime expired;
}
