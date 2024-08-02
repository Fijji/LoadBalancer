package com.exercise.interview.constants;

/**
 * Constants used across the Load Balancer library.
 */
public class Constants {

    /**
     * Maximum number of backend instances that can be registered.
     * <p>
     * This constant defines the upper limit for the number of backend instances
     * that the load balancer can handle. Exceeding this limit will result in an
     * exception being thrown during instance registration.
     * </p>
     */
    public static final int MAX_INSTANCES = 10;
}


