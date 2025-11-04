package com.warehouse.identity_api.base;

import com.warehouse.identity_api.services.interfaces.AuthService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BaseServiceImpl<E, ID, DTO, R extends BaseRepository<E, ID>> implements BaseService<E, ID, DTO> {
  R repository;
  BaseMapper<E, DTO> mapper;
  AuthService authService;

  protected BaseServiceImpl(R repository, BaseMapper<E, DTO> mapper, AuthService authService) {
    this.authService = authService;
    this.repository = repository;
    this.mapper = mapper;
  }

  private static String convertDateToString(Object value) {
    if (value instanceof LocalDate localDate) {
      return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    return value.toString();
  }

  private static Object convertNumber(String value, Class<?> targetType) {
    if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
      return Integer.parseInt(value);
    } else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
      return Long.parseLong(value);
    }

    return value;
  }

  @Override
  public Page<E> findAll(Map<String, String> filters, Map<String, String> sort, int size, int page) {
    Map<String, Object> filterMap = filters != null ? new HashMap<>(filters) : new HashMap<>();
    filterMap.remove("size");
    filterMap.remove("page");
    filterMap.keySet().removeIf(key -> key.startsWith("sort["));

    Map<String, Object> sortMap = sort != null ? new HashMap<>(sort) : new HashMap<>();
    sortMap.remove("size");
    sortMap.remove("page");

    Specification<E> specification = buildSpec(filterMap);

    Pageable pageable = buildPageable(sortMap, size, page);

    return repository.findAll(specification, pageable);
  }

  private <T> Specification<T> buildSpec(Map<String, Object> filters) {
    return ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(cb.isNull(root.get("deletedAt")));

      if (filters != null && !filters.isEmpty()) {
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
          String fieldName = entry.getKey();
          Object value = entry.getValue();

          if (fieldName != null && !fieldName.trim().isEmpty() && value != null && !value.toString().isEmpty()) {
            String searchValue = convertDateToString(value).toLowerCase();
            Class<?> fieldType = root.get(fieldName).getJavaType();

            if (fieldType.equals(LocalDate.class)) {
              predicates.add(
                    cb.equal(root.get(fieldName), LocalDate.parse(searchValue, DateTimeFormatter.ISO_LOCAL_DATE))
              );
            } else if (fieldType.equals(Boolean.class)) {
              predicates.add(cb.equal(root.get(fieldName), Boolean.parseBoolean(searchValue)));
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class) ||
                  fieldType.equals(Long.class) || fieldType.equals(long.class)) {
              predicates.add(cb.equal(root.get(fieldName), convertNumber(searchValue, fieldType)));
            } else {
              predicates.add(cb.like(root.get(fieldName).as(String.class), "%" + searchValue + "%"));
            }
          }
        }

      }
      return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
    });
  }

  private Pageable buildPageable(Map<String, Object> sortMap, int size, int page) {
    if (sortMap == null || sortMap.isEmpty()) {
      return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    List<Sort.Order> orders = new ArrayList<>();

    for (Map.Entry<String, Object> entry : sortMap.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (key != null && key.startsWith("sort[") && key.endsWith("]") && value != null) {
        String fieldName = key.replaceAll("sort\\[(.*)\\]", "$1");

        if (!fieldName.isEmpty()) {
          String directionStr = value.toString().toLowerCase();
          if (directionStr.equals("asc") || directionStr.equals("dec")) {
            Sort.Direction direction = Sort.Direction.fromString(directionStr);
            orders.add(new Sort.Order(direction, fieldName));
          }
        }
      }
    }

    Sort sortResult = orders.isEmpty() ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(orders);

    return PageRequest.of(page, size, sortResult);
  }

  @Override
  public E create(DTO dto) {
    validationCreate(dto);
    validationCommon(dto);

    var e = mapper.toCreate(dto);

    beforeSave(e, dto);

    if (e instanceof AuditEntity auditEntity) {
      auditEntity.setCreatedAt(LocalDateTime.now());
      auditEntity.setCreatedBy(authService.getAuthId());
    }

    repository.save(e);
    afterCreate(dto, e);

    return e;
  }

  @Override
  public E update(ID id, DTO dto) {
    var e = findById(id);

    validationUpdate(dto, id);
    validationCommon(dto);

    mapper.toUpdate(e, dto);

    if (e instanceof AuditEntity auditEntity) {
      auditEntity.setUpdatedAt(LocalDateTime.now());
      auditEntity.setUpdatedBy(authService.getAuthId());
    }

    beforeUpdate(e, dto);

    repository.save(e);

    return e;
  }

  @Override
  public void delete(ID id) {
    var e = findById(id);

    if (e instanceof AuditEntity auditEntity) {
      auditEntity.setDeletedAt(LocalDateTime.now());
      auditEntity.setDeletedBy(authService.getAuthId());
    }

    repository.save(e);
  }

  @Override
  public E findById(ID id) {
    return repository.findById(id)
                     .filter(e -> !(e instanceof AuditEntity auditEntity) || auditEntity.getDeletedAt() == null)
                     .orElseThrow(this::createNotFoundException);
  }

  protected void validationCreate(DTO dto) {
  }

  protected void validationUpdate(DTO dto, ID id) {
  }

  protected void validationCommon(DTO dto) {
  }

  protected void afterCreate(DTO dto, E entity) {
  }

  protected void beforeSave(E e, DTO dto) {
  }

  protected void beforeUpdate(E e, DTO dto) {
  }

  protected void beforeDelete(ID id) {
  }

  protected abstract RuntimeException createNotFoundException();
}
