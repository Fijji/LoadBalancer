package com.revolut.interview.strategy;

import com.revolut.interview.BackendInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Strategy for selecting a backend instance in a round-robin fashion.
 *  *
 *  * The round-robin strategy ensures that each server gets a roughly equal share of the load over time,
 *  * which can help in managing resources effectively and preventing any single server from being overwhelmed.
 */
public class RoundRobinStrategy implements SelectionStrategy {
//    The use of AtomicInteger ensures that even if multiple threads call the select method concurrently,
//    each thread will receive a unique instance,
//    maintaining the round-robin order without overlaps or race conditions.
    private final AtomicInteger index = new AtomicInteger(0);
//    This is an AtomicInteger instance, which is used to keep track of the current index in the round-robin selection process.
//    AtomicInteger provides thread-safe operations, ensuring that the index can be updated safely in concurrent environments.

    @Override
    public BackendInstance select(Map<String, BackendInstance> instances) {
        List<BackendInstance> instanceList = instances.values().stream().collect(Collectors.toList());
        return instanceList.get(index.getAndUpdate(i -> (i + 1) % instanceList.size()));  //Total Time Complexity for RoundRobinStrategy: O(n)
    }
//    getAndUpdate(i -> (i + 1) % instanceList.size()):
//    This is a method of the AtomicInteger class. It updates the current value of the AtomicInteger and returns the previous value atomically.
//    Lambda Expression (i -> (i + 1) % instanceList.size()):
//    The lambda expression defines the update operation. It takes the current index i, increments it by 1, and then uses the modulus operator % with instanceList.size()
//    to ensure the index wraps around if it exceeds the size of the list (i.e., if i reaches the end of the list, it resets to 0). This implements the round-robin selection.
//    get() method of instanceList:
//    After index is updated, the method retrieves the backend instance at the position indicated by the previous value of index.
}
