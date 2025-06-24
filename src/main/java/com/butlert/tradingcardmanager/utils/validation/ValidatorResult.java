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
