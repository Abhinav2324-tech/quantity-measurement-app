
package com.bridgelabz.quantitymeasurement.exception;

/**
 * UC16: Exception for database persistence failures.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
