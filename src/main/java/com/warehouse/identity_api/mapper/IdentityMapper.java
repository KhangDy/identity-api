package com.warehouse.identity_api.mapper;

import com.warehouse.identity_api.base.BaseMapper;
import com.warehouse.identity_api.dto.req.IdentityDTO;
import com.warehouse.identity_api.entities.Identity;
import org.springframework.stereotype.Component;

@Component
public class IdentityMapper implements BaseMapper<Identity, IdentityDTO> {
  
  @Override
  public Identity toCreate(IdentityDTO dto) {
    return Identity.builder()
            .userId(dto.getUserId())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .refreshToken(dto.getRefreshToken())
            .expired(dto.getExpired())
            .build();
  }

  @Override
  public void toUpdate(Identity entity, IdentityDTO dto) {
    entity.setUserId(dto.getUserId());
    entity.setEmail(dto.getEmail());
    entity.setPassword(dto.getPassword());
    entity.setRefreshToken(dto.getRefreshToken());
    entity.setExpired(dto.getExpired());
  }
}
