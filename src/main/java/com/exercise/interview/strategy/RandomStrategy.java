package com.exercise.interview.strategy;

import com.exercise.interview.BackendInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Strategy for selecting a backend instance randomly.
 */
public class RandomStrategy implements SelectionStrategy {
    private final Random random = new Random();

    @Override
    public BackendInstance select(Map<String, BackendInstance> instances) {
        List<BackendInstance> instanceList = instances.values().stream().toList();
        return instanceList.get(random.nextInt(instanceList.size()));
    }

}
