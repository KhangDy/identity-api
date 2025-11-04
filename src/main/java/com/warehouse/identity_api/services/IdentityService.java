package com.warehouse.identity_api.services;

import com.warehouse.identity_api.base.BaseServiceImpl;
import com.warehouse.identity_api.dto.req.IdentityDTO;
import com.warehouse.identity_api.entities.Identity;
import com.warehouse.identity_api.exception.AppException;
import com.warehouse.identity_api.exception.ErrorCode;
import com.warehouse.identity_api.mapper.IdentityMapper;
import com.warehouse.identity_api.repository.IdentityRepository;
import com.warehouse.identity_api.services.interfaces.AuthService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService extends BaseServiceImpl<Identity, Long, IdentityDTO, IdentityRepository> {
  PasswordEncoder passwordEncoder;

  public IdentityService(
        AuthService authService,
        IdentityRepository repository,
        IdentityMapper identityMapper,
        PasswordEncoder passwordEncoder
  ) {
    super(repository, identityMapper, authService);
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void validationCommon(IdentityDTO identityDTO) {
    if (repository.existsByEmail(identityDTO.getEmail())) {
      throw new AppException(ErrorCode.FIELD_ALREADY_EXISTS, "Email");
    }
    
    if (repository.existsByUserId(identityDTO.getUserId())) {
      throw new AppException(ErrorCode.FIELD_ALREADY_EXISTS, "User ID");
    }
  }

  @Override
  protected void beforeSave(Identity identity, IdentityDTO identityDTO) {
    // Hash the password before saving
    identity.setPassword(passwordEncoder.encode(identityDTO.getPassword()));
  }

  public Identity findByEmail(String email) {
    return repository.findByEmail(email)
            .orElseThrow(() -> new AppException(ErrorCode.FIELD_NOT_FOUND, "Identity with email: " + email));
  }

  public Identity findByUserId(Long userId) {
    return repository.findByUserId(userId)
            .orElseThrow(() -> new AppException(ErrorCode.FIELD_NOT_FOUND, "Identity with user ID: " + userId));
  }

  public Identity findByRefreshToken(String refreshToken) {
    return repository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new AppException(ErrorCode.FIELD_NOT_FOUND, "Identity with refresh token"));
  }

  public boolean checkIdentityExists(Long userId) {
    return repository.existsByUserId(userId);
  }

  @Override
  protected RuntimeException createNotFoundException() {
    return new AppException(ErrorCode.FIELD_NOT_FOUND, "Identity");
  }
}
