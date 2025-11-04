package com.warehouse.identity_api.base;

public interface BaseMapper<E, DTO> {
  E toCreate(DTO dto);

  void toUpdate(E entity, DTO dto);
}
