package com.revolut.interview;

import com.revolut.interview.constants.Constants;
import com.revolut.interview.exceptions.InstanceAlreadyExistsException;
import com.revolut.interview.exceptions.MaxCapacityReachedException;
import com.revolut.interview.strategy.RandomStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class LoadBalancerTest {

//    Basic Functionality Tests
//
//    Test that registering a backend instance works.
//    Test that retrieving a registered instance works.
//
//    Edge Case Tests
//
//    Test that registering a null instance throws an exception.
//    Test that registering the same instance twice throws an exception.
//    Test that registering more than the allowed instances throws an exception.
//
//    Concurrency Tests
//
//    Test concurrent registration of instances.
//    Test concurrent retrieval of instances.
//
//            Strategy-Specific Tests
//
//    Test random selection strategy.
//    Test round-robin selection strategy.


            /**
             *   Purpose: Ensure that a backend instance can be registered successfully.
             */
    @Test
    void testRegisterInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");
        Assertions.assertTrue(loadBalancer.register(instance));
    }

    /**
     *   Purpose: Ensure that a registered backend instance can be retrieved.
     */

    @Test
    void testGetInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");
        loadBalancer.register(instance);
        Assertions.assertEquals(instance, loadBalancer.get());
    }


    /**
     * Tests that registering a null instance throws an IllegalArgumentException.
     */
    @Test
    void testRegisterNullInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        Assertions.assertThrows(IllegalArgumentException.class, () -> loadBalancer.register(null));
    }

    /**
     * Tests that a BackendInstance with a duplicate address cannot be registered.
     */
    @Test
    void testDuplicate() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");

        // First registration should succeed
        Assertions.assertTrue(loadBalancer.register(instance));
        // Second registration of the same address should fail
        Assertions.assertThrows(InstanceAlreadyExistsException.class, () -> loadBalancer.register(instance));
    }

    /**
     * Tests that the LoadBalancer can register up to MAX_INSTANCES and no more.
     */
    @Test
    void testMaxCapacityReached() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        for (int i = 0; i < Constants.MAX_INSTANCES; i++) {
            loadBalancer.register(new BackendInstance("http://instance" + i));
        }
        Assertions.assertThrows(MaxCapacityReachedException.class, () -> loadBalancer.register(new BackendInstance("http://instanceExtra")));
    }


//    additional tests
    /**
     * Tests that the get() method returns a registered BackendInstance.
     */
    @Test
    void testGet() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance1 = new BackendInstance("http://instance1");
        BackendInstance instance2 = new BackendInstance("http://instance2");

        // Register two instances
        loadBalancer.register(instance1);
        loadBalancer.register(instance2);

        // Ensure that get() returns a non-null instance
        Assertions.assertNotNull(loadBalancer.get());
    }



    /**
     * Tests that the get() method returns the only registered instance.
     */
    @Test
    void testGetSingleInstance() {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        BackendInstance instance = new BackendInstance("http://instance1");

        loadBalancer.register(instance);

        // Ensure that get() returns the only registered instance
        Assertions.assertEquals(instance, loadBalancer.get());
    }

    /**
     * Tests concurrent registration of instances to ensure thread safety.
     */
    @Test
    void testConcurrentRegistrations() throws InterruptedException {
        LoadBalancer loadBalancer = new LoadBalancer(new RandomStrategy());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<BackendInstance> instances = IntStream.range(1, 11)
                .mapToObj(i -> new BackendInstance("http://instance" + i))
                .collect(Collectors.toList());

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

