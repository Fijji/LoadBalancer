   TASKS:

1. Create a LoadBalancer class that has a method to register backend instances
   Each backend instance address should be unique, it should not be possible to register the same address two times
   Load balancer should accept up to 10 backend instances
   The code should be production ready
   The Loadbalancer will be released as a library

2. Develop an algorithm that, when invoking the Load Balancers get() method multiple times,should return one
   backend-instance choosing
   between the registered ones randomly.

3. Implement Round Robin Algorithm