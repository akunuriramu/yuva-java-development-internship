package week2;

/**
 * Week 2 - Topic 1: Classes, Objects, Constructors, and Memory Lifecycle in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - A Class is a compile-time metadata template (loaded into JVM Metaspace).
 * - An Object is a runtime instance allocated on the JVM Heap.
 * - Variables holding objects are references stored on Thread Stacks pointing to Heap addresses.
 * - Constructor Chaining via 'this(...)': Avoids code duplication across multiple constructor overloads.
 * - Copy Constructor Pattern: Idiomatic Java alternative to Object.clone() providing controlled shallow/deep copying.
 * - Garbage Collection (GC) Eligibility: An object becomes eligible for GC when it is no longer reachable from any GC Root
 *   (via nullification, reference reassignment, or island of isolation).
 */
public class ClassesAndObjectsDemo {

    // Inner Domain Class demonstrating blueprint definition
    public static class CloudServerInstance {
        // Instance variables (State stored on Heap inside each object instance)
        private String instanceId;
        private String serverType;
        private int cpuCores;
        private double ramGigabytes;
        private boolean isRunning;

        // Static variable (Shared across all instances, stored in JVM Metaspace)
        public static int totalInstancesCreated = 0;

        // 1. Default / No-Argument Constructor
        public CloudServerInstance() {
            // Constructor chaining: delegates to parameterized constructor
            this("srv-default-000", "general-compute", 2, 4.0);
            System.out.println("  [Constructor Log] No-argument constructor invoked -> Delegated via this(...)");
        }

        // 2. Parameterized Constructor
        public CloudServerInstance(String instanceId, String serverType, int cpuCores, double ramGigabytes) {
            // Disambiguating instance variables from method arguments using 'this'
            this.instanceId = instanceId;
            this.serverType = serverType;
            this.cpuCores = cpuCores;
            this.ramGigabytes = ramGigabytes;
            this.isRunning = false; // Initial state

            totalInstancesCreated++;
        }

        // 3. Copy Constructor (Industry-standard cloning alternative)
        public CloudServerInstance(CloudServerInstance target) {
            if (target == null) {
                throw new IllegalArgumentException("Cannot copy from a null server instance.");
            }
            this.instanceId = target.instanceId + "-replica";
            this.serverType = target.serverType;
            this.cpuCores = target.cpuCores;
            this.ramGigabytes = target.ramGigabytes;
            this.isRunning = false; // Cloned instance starts stopped

            totalInstancesCreated++;
            System.out.println("  [Constructor Log] Copy constructor invoked -> Cloned state from " + target.instanceId);
        }

        // Instance methods (Behaviors)
        public void startInstance() {
            this.isRunning = true;
            System.out.println("  [Status] Server instance " + this.instanceId + " is now RUNNING.");
        }

        public void stopInstance() {
            this.isRunning = false;
            System.out.println("  [Status] Server instance " + this.instanceId + " has STOPPED.");
        }

        public void printSpecifications() {
            System.out.printf("  | %-18s | %-16s | %2d vCPUs | %5.1f GB RAM | State: %-7s |%n",
                    this.instanceId, this.serverType, this.cpuCores, this.ramGigabytes, (this.isRunning ? "RUNNING" : "STOPPED"));
        }

        public String getInstanceId() {
            return this.instanceId;
        }
    }

    // Helper node class to illustrate "Island of Isolation" GC concept
    public static class Node {
        String name;
        Node neighbor;

        public Node(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 1: CLASSES, OBJECTS, CONSTRUCTORS & MEMORY DYNAMICS");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Class Blueprint vs Heap Object Instantiation
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Class Blueprint vs. Heap Object Instantiation ---");
        System.out.println("Memory Concept: 'CloudServerInstance server1' allocates a reference on Stack.");
        System.out.println("                'new CloudServerInstance(...)' allocates object memory on Heap.\n");

        // Instantiation via Parameterized Constructor
        CloudServerInstance server1 = new CloudServerInstance("srv-prod-web01", "compute-optimized", 8, 16.0);
        server1.startInstance();

        // Instantiation via No-Arg Constructor (chains to parameterized)
        System.out.println("\nCreating server via No-Arg constructor (demonstrating constructor chaining):");
        CloudServerInstance server2 = new CloudServerInstance();

        // Instantiation via Copy Constructor
        System.out.println("\nCreating server via Copy Constructor:");
        CloudServerInstance server3 = new CloudServerInstance(server1);

        System.out.println("\nFleet Specifications Summary:");
        System.out.println("  +--------------------+------------------+---------+-------------+---------------+");
        System.out.println("  | Instance ID        | Workload Profile | Cores   | RAM Size    | Current State |");
        System.out.println("  +--------------------+------------------+---------+-------------+---------------+");
        server1.printSpecifications();
        server2.printSpecifications();
        server3.printSpecifications();
        System.out.println("  +--------------------+------------------+---------+-------------+---------------+");
        System.out.println("  Total Server Objects Created in Metaspace Registry: " + CloudServerInstance.totalInstancesCreated);

        // ---------------------------------------------------------------------
        // 2. Object Identity vs State Equality (Stack Reference vs Heap Payload)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Stack Reference Equality vs Heap Identity ---");
        CloudServerInstance serverRef = server1; // Reference copy (Aliasing: two stack pointers to one heap object)
        System.out.println("server1 memory reference equals serverRef? (server1 == serverRef): " + (server1 == serverRef) + " (Points to SAME Heap memory)");
        System.out.println("server1 memory reference equals server3?   (server1 == server3):   " + (server1 == server3) + " (Independent Heap allocations)");

        // ---------------------------------------------------------------------
        // 3. Garbage Collection (GC) Eligibility Mechanisms
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Garbage Collection (GC) Lifecycle & Root Reachability ---");

        // Scenario A: Reference Nullification
        System.out.println("Scenario A - Reference Nullification:");
        CloudServerInstance tempServer = new CloudServerInstance("srv-temp-999", "spot-batch", 1, 2.0);
        System.out.println("  tempServer allocated at Heap. Reachable from current Stack Frame.");
        tempServer = null; // Object is now disconnected from GC Roots
        System.out.println("  tempServer set to null -> Heap object 'srv-temp-999' is now ELIGIBLE for Garbage Collection.");

        // Scenario B: Reference Reassignment
        System.out.println("\nScenario B - Reference Reassignment:");
        CloudServerInstance alpha = new CloudServerInstance("srv-alpha", "memory-optimized", 16, 64.0);
        CloudServerInstance beta = new CloudServerInstance("srv-beta", "gpu-accelerated", 32, 128.0);
        System.out.println("  Initial: 'alpha' points to srv-alpha, 'beta' points to srv-beta.");
        alpha = beta; // 'srv-alpha' loses its only remaining incoming reference pointer
        System.out.println("  After 'alpha = beta' -> Original Heap object 'srv-alpha' is now ELIGIBLE for GC.");

        // Scenario C: Island of Isolation (Classic Interview Problem)
        System.out.println("\nScenario C - Island of Isolation:");
        Node nodeA = new Node("Cluster-Node-A");
        Node nodeB = new Node("Cluster-Node-B");
        nodeA.neighbor = nodeB; // A references B
        nodeB.neighbor = nodeA; // B references A
        System.out.println("  nodeA and nodeB cross-reference each other on Heap.");

        nodeA = null; // Sever stack pointer to Node A
        nodeB = null; // Sever stack pointer to Node B
        System.out.println("  Stack references severed (nodeA = null, nodeB = null).");
        System.out.println("  Result: Node A and Node B reference each other, but NEITHER is reachable from GC Root.");
        System.out.println("  Island of Isolation formed -> BOTH nodes are eligible for Garbage Collection!");

        // Requesting JVM GC hint
        System.gc();
        System.out.println("  System.gc() requested to JVM (Non-blocking GC execution hint passed).");

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 1 DEMONSTRATION COMPLETE: ZERO RUNTIME ANOMALIES");
        System.out.println("==========================================================================\n");
    }
}
