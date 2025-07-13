/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025,
 * ValidatorResult.java
 * This class is used to message the validation, so it is easily displayed on the gui.
 */
package com.butlert.tradingcardmanager.utils.validation;

public class ValidatorResult {
    private final boolean valid;
    private final String message;

    public ValidatorResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public static ValidatorResult success() {
        return new ValidatorResult(true, "");
    }

    public static ValidatorResult fail(String message) {
        return new ValidatorResult(false, message);
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
