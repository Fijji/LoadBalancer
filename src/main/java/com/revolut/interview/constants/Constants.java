package com.revolut.interview.constants;

/**
 * Constants used across the Load Balancer library.
 */
public class Constants {

    /**
     * Constants used across the Load Balancer library.
     * <p>
     * This class contains various constants used throughout the load balancer
     * implementation. It is a final class to prevent inheritance, and it cannot
     * be instantiated.
     * </p>
     */
    //  public final class Constants {

    // Private constructor to prevent instantiation
    //    private Constants() {
    //        throw new AssertionError("Cannot instantiate Constants class");
    //    }

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

/**
 * Enum for constants used across the Load Balancer library.
 */
//public enum Constants {
//
//    MAX_INSTANCES(10);
//
//    private final int value;
//
//    Constants(int value) {
//        this.value = value;
//    }
//
//    /**
//     * Returns the integer value of the constant.
//     *
//     * @return the value
//     */
//    public int getValue() {
//        return value;
//    }
//}
