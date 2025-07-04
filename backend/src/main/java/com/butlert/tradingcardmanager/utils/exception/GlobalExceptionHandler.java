package com.butlert.tradingcardmanager.utils.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CardImportException.class)
    public ResponseEntity<?> handleImport(CardImportException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "Unexpected error");
        errorBody.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

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
}