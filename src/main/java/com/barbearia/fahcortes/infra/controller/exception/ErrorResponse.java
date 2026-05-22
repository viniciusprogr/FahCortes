package com.barbearia.fahcortes.infra.controller.exception;

import java.util.List;

public record ErrorResponse(String code, String message, List<FieldError> details) {

    public record FieldError(String field, String message) {}

    public ErrorResponse(String code, String message) {
        this(code, message, null);
    }
}
