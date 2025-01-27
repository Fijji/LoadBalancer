package com.exercise.interview.strategy;

import com.exercise.interview.BackendInstance;

import java.util.Map;

/**
 * Strategy interface for selecting a backend instance from the available instances.
 */
public interface SelectionStrategy {
    BackendInstance select(Map<String, BackendInstance> instances);
}
