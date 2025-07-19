package com.butlert.tradingcardmanager.utils.exception;

/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardImportException.java
 *
 * Custom unchecked exception used during the card import process.
 * This exception wraps more detailed context about parsing or file processing errors,
 * typically thrown when reading or converting card data from text files fails.
 *
 * <p>Extends {@link RuntimeException} so it can be thrown without explicit declaration.</p>
 */
public class CardImportException extends RuntimeException {
    /**
     * Constructs a new CardImportException with a detailed message and root cause.
     *
     * @param message a descriptive error message explaining the context
     * @param cause   the original exception that triggered this error
     */
    public CardImportException(String message, Throwable cause) {
        super(message, cause);
    }
}

