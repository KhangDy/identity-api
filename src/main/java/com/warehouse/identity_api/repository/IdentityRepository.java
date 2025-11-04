package com.warehouse.identity_api.repository;

import com.warehouse.identity_api.base.BaseRepository;
import com.warehouse.identity_api.entities.Identity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends BaseRepository<Identity, Long> {
  Optional<Identity> findByEmail(String email);
  
  Optional<Identity> findByUserId(Long userId);
  
  Optional<Identity> findByRefreshToken(String refreshToken);
  
  boolean existsByEmail(String email);
  
  boolean existsByUserId(Long userId);
}
