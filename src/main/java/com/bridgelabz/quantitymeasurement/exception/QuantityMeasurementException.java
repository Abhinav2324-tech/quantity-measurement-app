
package com.bridgelabz.quantitymeasurement.exception;

/**
 * UC15: Custom unchecked exception for measurement operations.
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}
