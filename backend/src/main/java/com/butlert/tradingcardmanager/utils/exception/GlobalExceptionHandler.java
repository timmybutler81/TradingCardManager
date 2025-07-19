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

/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 7, 2025
 * GlobalExceptionHandler.java
 *
 * This class provides centralized exception handling across the application.
 * It captures and formats validation, input, and runtime exceptions into structured HTTP responses
 * for the client, improving API usability and debugging.
 *
 * <p>Handled exceptions include:</p>
 * <ul>
 *   <li>{@link IllegalArgumentException}</li>
 *   <li>{@link com.butlert.tradingcardmanager.utils.exception.CardImportException}</li>
 *   <li>{@link HttpMessageNotReadableException}</li>
 *   <li>{@link MethodArgumentNotValidException}</li>
 *   <li>{@link MethodArgumentTypeMismatchException}</li>
 *   <li>{@link ConstraintViolationException}</li>
 *   <li>Generic {@link Exception}</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Default constructor for {@code GlobalExceptionHandler}.
     * <p>
     * No arguments are needed since all exception handling logic is defined via annotations
     * and Spring manages this bean automatically.
     */
    public GlobalExceptionHandler() {

    }

    /**
     * Handles {@link IllegalArgumentException} typically thrown from validation or business logic.
     *
     * @param ex the thrown IllegalArgumentException
     * @return a 400 Bad Request response with error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles {@link CardImportException} thrown during file import processing.
     *
     * @param ex the thrown CardImportException
     * @return a 400 Bad Request response with error details
     */
    @ExceptionHandler(CardImportException.class)
    public ResponseEntity<?> handleImport(CardImportException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles any uncaught {@link Exception} that is not specifically handled by other methods.
     *
     * @param ex the thrown exception
     * @return a 500 Internal Server Error response with a generic error message and exception details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "Unexpected error");
        errorBody.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    /**
     * Handles malformed JSON payloads or data type mismatches during deserialization.
     * Specifically catches {@link HttpMessageNotReadableException} and attempts to extract
     * field-specific errors (e.g., invalid number format).
     *
     * @param ex the thrown HttpMessageNotReadableException
     * @return a 400 Bad Request response with field-specific parsing error (if available)
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
     * Handles incorrect types passed as path or query parameters, such as non-numeric
     * strings where an integer is expected.
     *
     * @param ex the thrown MethodArgumentTypeMismatchException
     * @return a 400 Bad Request response with explanation of the type mismatch
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
     * Handles validation errors triggered by invalid request bodies using {@link MethodArgumentNotValidException}.
     *
     * @param ex the validation exception
     * @return a 400 Bad Request with a user-friendly field-specific error message
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
     * Handles validation errors caused by constraint violations outside of request body validation,
     * such as path variables, query parameters, or method-level constraints.
     *
     * @param ex the thrown ConstraintViolationException
     * @return a 400 Bad Request response with the first constraint violation message
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