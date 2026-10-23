package com.parkspot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a slot is already occupied and cannot accept a new vehicle.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class SlotAlreadyOccupiedException extends RuntimeException {

    public SlotAlreadyOccupiedException(String message) {
        super(message);
    }
}
