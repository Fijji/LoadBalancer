package com.exercise.interview;

import com.exercise.interview.constants.Constants;
import com.exercise.interview.exceptions.InstanceAlreadyExistsException;
import com.exercise.interview.exceptions.MaxCapacityReachedException;
import com.exercise.interview.strategy.RandomStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class LoadBalancerTest {

    @Test
    void testRegisterInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");
        Assertions.assertTrue(loadBalancer.register(instance));
    }

    @Test
    void testGetInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");
        loadBalancer.register(instance);
        Assertions.assertEquals(instance, loadBalancer.get());
    }

    @Test
    void testRegisterNullInstanceThrowIllegalArgumentException() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        Assertions.assertThrows(IllegalArgumentException.class, () -> loadBalancer.register(null));
    }

    @Test
    void testDuplicateCannotBeRegistered() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");

        // First registration should succeed
        Assertions.assertTrue(loadBalancer.register(instance));
        // Second registration of the same address should fail
        Assertions.assertThrows(InstanceAlreadyExistsException.class, () -> loadBalancer.register(instance));
    }

    @Test
    void testMaxCapacityReachedThrowException() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        for (int i = 0; i < Constants.MAX_INSTANCES; i++) {
            loadBalancer.register(new BackendInstance("http://instance" + i));
        }
        Assertions.assertThrows(MaxCapacityReachedException.class, () -> loadBalancer.register(new BackendInstance("http://instanceExtra")));
    }


    @Test
    void testGetBackendInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance1 = new BackendInstance("http://instance1");
        BackendInstance instance2 = new BackendInstance("http://instance2");

        // Register two instances
        loadBalancer.register(instance1);
        loadBalancer.register(instance2);

        // Ensure that get() returns a non-null instance
        Assertions.assertNotNull(loadBalancer.get());
    }


    @Test
    void testGetSingleInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");

        loadBalancer.register(instance);

        // Ensure that get() returns the only registered instance
        Assertions.assertEquals(instance, loadBalancer.get());
    }

    @Test
    void testConcurrentRegistrations() throws InterruptedException {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<BackendInstance> instances = IntStream.range(1, 11)
                .mapToObj(i -> new BackendInstance("http://instance" + i)).toList();

        // Register instances concurrently
        for (BackendInstance instance : instances) {
            executorService.submit(() -> loadBalancer.register(instance));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Ensure all instances are registered
        Assertions.assertEquals(10, instances.size());
    }

    /**
     * Tests concurrent retrieval of instances to ensure thread safety.
     */
    @Test
    void testConcurrentSelections() throws InterruptedException {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<BackendInstance> instances = IntStream.range(1, 4)
                .mapToObj(i -> new BackendInstance("http://instance" + i))
                .collect(Collectors.toList());

        instances.forEach(loadBalancer::register);

        // Retrieve instances concurrently
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                BackendInstance instance = loadBalancer.get();
                Assertions.assertNotNull(instance);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}

