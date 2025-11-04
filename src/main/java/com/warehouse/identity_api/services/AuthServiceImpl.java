package com.warehouse.identity_api.services;

import com.warehouse.identity_api.services.interfaces.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  
  @Override
  public Long getAuthId() {
    // For now, return a default user ID
    // In a real application, this would extract the user ID from the security context
    return 1L;
  }
}
