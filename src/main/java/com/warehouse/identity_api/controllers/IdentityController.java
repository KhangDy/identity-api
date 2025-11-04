package com.warehouse.identity_api.controllers;

import com.warehouse.identity_api.dto.ApiResponse;
import com.warehouse.identity_api.dto.req.IdentityDTO;
import com.warehouse.identity_api.entities.Identity;
import com.warehouse.identity_api.services.IdentityService;
import com.warehouse.identity_api.validation.Action;
import com.warehouse.identity_api.validation.Resource;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/identity")
@Resource(name = "identity")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityController {
  IdentityService identityService;

  @Action(name = "create")
  @PostMapping("create")
  public ApiResponse<Identity> create(@RequestBody @Valid IdentityDTO identity) {
    return ApiResponse.<Identity>builder()
                      .result(identityService.create(identity))
                      .build();
  }

  @Action(name = "update")
  @PutMapping("update/{id}")
  public ApiResponse<Identity> update(@PathVariable Long id, @RequestBody @Valid IdentityDTO identity) {
    return ApiResponse.<Identity>builder()
                      .result(identityService.update(id, identity))
                      .build();
  }

  @Action(name = "view")
  @GetMapping("get-by-id/{id}")
  public ApiResponse<Identity> findById(@PathVariable Long id) {
    return ApiResponse.<Identity>builder()
                      .result(identityService.findById(id))
                      .build();
  }

  @Action(name = "view")
  @GetMapping("get-by-email/{email}")
  public ApiResponse<Identity> findByEmail(@PathVariable String email) {
    return ApiResponse.<Identity>builder()
                      .result(identityService.findByEmail(email))
                      .build();
  }

  @Action(name = "view")
  @GetMapping("get-by-user-id/{userId}")
  public ApiResponse<Identity> findByUserId(@PathVariable Long userId) {
    return ApiResponse.<Identity>builder()
                      .result(identityService.findByUserId(userId))
                      .build();
  }

  @Action(name = "view")
  @GetMapping("find-all")
  public ApiResponse<Page<Identity>> findAll(
        @RequestParam(required = false) Map<String, String> filters,
        @RequestParam(required = false) Map<String, String> sort,
        @RequestParam int size,
        @RequestParam int page
  ) {
    return ApiResponse.<Page<Identity>>builder()
                      .result(identityService.findAll(filters, sort, size, page))
                      .build();
  }

  @Action(name = "delete")
  @DeleteMapping("delete/{id}")
  public ApiResponse<Object> delete(@PathVariable Long id) {
    identityService.delete(id);
    return ApiResponse.builder()
                      .message("Delete identity with id: " + id + " successfully")
                      .build();
  }
}
