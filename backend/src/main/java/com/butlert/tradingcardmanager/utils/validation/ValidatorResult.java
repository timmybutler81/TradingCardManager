package com.butlert.tradingcardmanager.utils.validation;

/**
 * Represents the outcome of a validation check, encapsulating both success/failure
 * and an associated message for display or logging purposes.
 *
 * <p>This class is commonly used in the validation layer to return detailed results
 * that can be shown to the user or logged for debugging.</p>
 *
 * <p>Use {@link #success()} to return a successful validation and
 * {@link #fail(String)} to return a failure with an explanation.</p>
 */
public class ValidatorResult {
    private final boolean valid;
    private final String message;

    /**
     * Constructs a ValidatorResult.
     *
     * @param valid true if the validation passed, false otherwise
     * @param message a message describing the result or error (empty if valid)
     */
    public ValidatorResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Factory method to create a successful validation result.
     *
     * @return a {@code ValidatorResult} indicating success
     */
    public static ValidatorResult success() {
        return new ValidatorResult(true, "");
    }

    /**
     * Factory method to create a failed validation result with a message.
     *
     * @param message the reason for failure
     * @return a {@code ValidatorResult} indicating failure and including an explanation
     */
    public static ValidatorResult fail(String message) {
        return new ValidatorResult(false, message);
    }

    /**
     * Returns whether the validation was successful.
     *
     * @return true if validation passed, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the message associated with the validation result.
     * This may be empty if the result is valid.
     *
     * @return the validation message
     */
    public String getMessage() {
        return message;
    }
}
