package com.revolut.interview;

import com.revolut.interview.constants.Constants;
import com.revolut.interview.exceptions.InstanceAlreadyExistsException;
import com.revolut.interview.exceptions.MaxCapacityReachedException;
import com.revolut.interview.strategy.SelectionStrategy;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The LoadBalancer class manages backend instances and provides
 * functionality to register and retrieve instances.
 */
public class LoadBalancer {
    //    Ensures thread-safe access and modification of the map.
    //    Enforces immutability of the map reference.
    //    Provides high performance in concurrent access scenarios.
    //    This setup is particularly beneficial in environments where the load balancer needs to handle multiple requests simultaneously,
    //    ensuring that the internal state remains consistent and operations are efficient.
    private final Map<String, BackendInstance> instances = new ConcurrentHashMap<>();

    //    Using ReentrantLock in the LoadBalancer class provides a fine-grained, flexible, and robust mechanism for ensuring thread safety and atomicity for operations that involve multiple steps.
    //    This approach is particularly important in concurrent programming scenarios to maintain the integrity and consistency of shared resources.
    private final ReentrantLock lock = new ReentrantLock();

    //    enable the LoadBalancer class to select backend instances using different strategies.
    //    This design adheres to the Strategy Design Pattern, which is part of the SOLID principles,
    //    specifically the Open/Closed Principle, allowing new strategies to be added without modifying the existing code.
    private final SelectionStrategy strategy;

    /**
     * Constructs a LoadBalancer with a specific selection strategy.
     *
     * @param strategy the strategy to use for selecting backend instances
     */
    public LoadBalancer(SelectionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Registers a backend instance if the address is unique and the limit has not been reached.
     *
     * @param instance the backend instance to register
     * @return true if registration is successful, false otherwise
     */
    public boolean register(BackendInstance instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance cannot be null.");
        }  // Check for null instance: O(1)

        lock.lock(); //Lock acquisition: O(1) (assuming the lock acquisition itself is a constant time operation)
        try {
            if (instances.size() >= Constants.MAX_INSTANCES) {  //Check size of instances: O(1)
                throw new MaxCapacityReachedException();
            }
            if (instances.containsKey(instance.getAddress())) {  //Check if instances contains the address: O(1) (average case for hash map operations)
                throw new InstanceAlreadyExistsException(instance.getAddress());
            }
            instances.put(instance.getAddress(), instance);  //Insert into instances: O(1) (average case for hash map operations)
        } finally {
            lock.unlock();  //Lock release: O(1)
        }
        return true;  //Total Time Complexity: O(1)
    }

    /**
     * Returns a randomly selected backend instance from the registered instances.
     *
     * @return a BackendInstance
     */
    public BackendInstance get() {
        if (instances.isEmpty()) {  //Check if instances is empty: O(1)
            throw new NoSuchElementException("No backend instances available.");
        }
        return strategy.select(Collections.unmodifiableMap(instances)); //Unmodifiable map creation: O(1)
    } //Total Time Complexity: O(1) + Strategy selection time complexity

//    By using Collections.unmodifiableMap(instances), the LoadBalancer class ensures that the strategy can only read from the instances map and cannot modify it.
//    This enforces better encapsulation, maintains the integrity of the internal state, and provides an additional layer of safety against unintended modifications.
//    This practice is aligned with the principles of defensive programming and helps in building robust and maintainable code.
}

