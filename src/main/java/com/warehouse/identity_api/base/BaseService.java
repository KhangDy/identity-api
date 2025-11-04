package com.warehouse.identity_api.base;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface BaseService<E, ID, DTO> {
  E create(DTO dto);

  E update(ID id, DTO dto);

  void delete(ID id);

  E findById(ID id);

  Page<E> findAll(Map<String, String> filters, Map<String, String> sort, int size, int page);
}
