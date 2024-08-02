package com.exercise.interview.strategy;

import com.exercise.interview.BackendInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Strategy for selecting a backend instance in a round-robin fashion.
 * *
 * * The round-robin strategy ensures that each server gets a roughly equal share of the load over time,
 * * which can help in managing resources effectively and preventing any single server from being overwhelmed.
 */
public class RoundRobinStrategy implements SelectionStrategy {

    private final AtomicInteger index = new AtomicInteger(0);


    @Override
    public BackendInstance select(Map<String, BackendInstance> instances) {
        List<BackendInstance> instanceList = instances.values().stream().collect(Collectors.toList());
        return instanceList.get(index.getAndUpdate(i -> (i + 1) % instanceList.size()));
    }
}
