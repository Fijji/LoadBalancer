package com.revolut.interview.strategy;

import com.revolut.interview.BackendInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Strategy for selecting a backend instance randomly.
 */
public class RandomStrategy implements SelectionStrategy {
    private final Random random = new Random();

    @Override
    public BackendInstance select(Map<String, BackendInstance> instances) {
        List<BackendInstance> instanceList = instances.values().stream().collect(Collectors.toList());  //Convert map values to list: O(n),  Random index selection: O(1)
        return instanceList.get(random.nextInt(instanceList.size())); //Get instance from list: O(1) so  Total Time Complexity for RandomStrategy: O(n)
    }

}
