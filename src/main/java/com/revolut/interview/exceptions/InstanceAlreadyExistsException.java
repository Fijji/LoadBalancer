package com.revolut.interview.exceptions;

/**
 * Exception thrown when a backend instance with a duplicate address is registered.
 */
public class InstanceAlreadyExistsException extends RuntimeException {
    public InstanceAlreadyExistsException(String address) {
        super("Backend instance with address " + address + " already exists.");
    }
}
