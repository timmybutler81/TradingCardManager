/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025,
 * CardImportException.java
 * This class is a custom exception thrown that gives more details about the error.
 */
package com.butlert.tradingcardmanager.utils.exception;

public class CardImportException extends RuntimeException {
    public CardImportException(String message, Throwable cause) {
        super(message, cause);
    }
}

