package com.revolut.interview.exceptions;

/**
 * Exception thrown when the maximum capacity of backend instances is reached.
 */
public class MaxCapacityReachedException extends RuntimeException {
    public MaxCapacityReachedException() {
        super("Maximum capacity of backend instances reached.");
    }
}
