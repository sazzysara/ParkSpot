package com.parkspot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an invalid operation is attempted (e.g., exiting a vehicle that hasn't entered).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
