package com.exercise.interview;

import com.exercise.interview.constants.Constants;
import com.exercise.interview.exceptions.InstanceAlreadyExistsException;
import com.exercise.interview.exceptions.MaxCapacityReachedException;
import com.exercise.interview.strategy.SelectionStrategy;

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

    private final Map<String, BackendInstance> instances = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();


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
        }

        lock.lock();
        try {
            if (instances.size() >= Constants.MAX_INSTANCES) {
                throw new MaxCapacityReachedException();
            }
            if (instances.containsKey(instance.address())) {
                throw new InstanceAlreadyExistsException(instance.address());
            }
            instances.put(instance.address(), instance);
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * Returns a randomly selected backend instance from the registered instances.
     *
     * @return a BackendInstance
     */
    public BackendInstance get() {
        if (instances.isEmpty()) {
            throw new NoSuchElementException("No backend instances available.");
        }
        return strategy.select(Collections.unmodifiableMap(instances));
    }
}

