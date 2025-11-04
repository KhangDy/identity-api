package com.warehouse.identity_api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  FIELD_NOT_FOUND("FIELD_NOT_FOUND", "Field not found"),
  FIELD_ALREADY_EXISTS("FIELD_ALREADY_EXISTS", "Field already exists"),
  INVALID_INPUT("INVALID_INPUT", "Invalid input"),
  UNAUTHORIZED("UNAUTHORIZED", "Unauthorized"),
  FORBIDDEN("FORBIDDEN", "Forbidden"),
  INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
