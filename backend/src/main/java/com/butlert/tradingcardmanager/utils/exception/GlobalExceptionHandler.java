/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 7, 2025,
 * GlobalExceptionHandler.java
 * This class handles global exceptions to catch and respond to validation errors.
 */
package com.butlert.tradingcardmanager.utils.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * method: handleIllegalArgument
     * parameters: exception thrown
     * return: response entity containing the error
     * purpose: handles exceptions from 400 responses
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * method: handleImport
     * parameters: exception thrown
     * return: response entity containing the error
     * purpose: handles custom exceptions related to invalid import actions
     */
    @ExceptionHandler(CardImportException.class)
    public ResponseEntity<?> handleImport(CardImportException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * method: handleGenericException
     * parameters: exception thrown
     * return: response entity containing the error
     * purpose: handles uncaught exceptions in a standard way
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "Unexpected error");
        errorBody.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    /**
     * method: handleJsonParseException
     * parameters: exception thrown
     * return: response entity containing the error
     * purpose: handles parsing errors in json
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseException(HttpMessageNotReadableException ex) {
        String message = "Invalid input format.";

        if (ex.getCause() instanceof InvalidFormatException ife) {
            String fieldName = "";
            if (!ife.getPath().isEmpty()) {
                fieldName = ife.getPath().get(0).getFieldName();
            }

            message = "Invalid value for field '" + fieldName + "': expected a number.";
        }

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", message);
        errorBody.put("details", "Bad Request");

        return ResponseEntity.badRequest().body(errorBody);
    }

    /**
     * method: handleTypeMismatch
     * parameters: exception thrown
     * return: Map of string for errors
     * purpose: handles incorrect arguments passed directly into the url
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return Map.of(
                "error", "Invalid card number: must be a numeric value.",
                "details", ex.getMessage()
        );
    }

    /**
     * method: handleValidationError
     * parameters: exception thrown
     * return: response entity contain the error
     * purpose: handles incorrect arguments on date
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        FieldError firstError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

        String message = (firstError != null)
                ? firstError.getField() + " " + firstError.getDefaultMessage()
                : "Validation failed";

        return ResponseEntity.badRequest().body(Map.of("error", message));
    }

    /**
     * method: handleConstraintViolationException
     * parameters: exception thrown
     * return: response entity contain the error
     * purpose: handles data violates from the entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validation Failed");

        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
}