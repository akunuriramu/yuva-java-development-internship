package week3;

import java.util.Arrays;

/**
 * Week 3 - Module 1: Arrays & Dynamic Array Mechanics in Java
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - Static Arrays (`int[]`): Contiguous block of memory allocated on the JVM Heap.
 *   Indexed lookup `arr[i]` computes `baseAddress + (i * elementSize)` in strictly O(1) time.
 * - Dynamic Arrays: Backed by an underlying fixed array that resizes by a geometric factor (2x)
 *   when capacity is exhausted, yielding O(1) amortized append despite occasional O(n) copy spikes.
 * - Key Algorithms Covered:
 *   1. Dynamic array append, indexed insert (right-shift), indexed delete (left-shift), O(1) lookup.
 *   2. Two-pointer in-place array reversal (O(n) time, O(1) auxiliary space).
 *   3. Kadane's Algorithm for Maximum Subarray Sum (O(n) time, O(1) space).
 *   4. Fixed-size Sliding Window Subarray Maximum Average / Sum (O(n) time, O(1) space).
 * - Enterprise Use Case: High-frequency telemetry & financial tick buffer auditing.
 */
public class ArraysDSDemo {

    /**
     * Custom Dynamic Array implementation from scratch to expose resizing & shifting mechanics.
     */
    public static class DynamicIntArray {
        private int[] backingStore;
        private int size;
        private int resizeCount;

        public DynamicIntArray(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Initial capacity must be positive.");
            }
            this.backingStore = new int[initialCapacity];
            this.size = 0;
            this.resizeCount = 0;
        }

        // O(1) Amortized append; O(n) worst-case when resizing triggers
        public void append(int value) {
            if (size == backingStore.length) {
                resize(backingStore.length * 2);
            }
            backingStore[size++] = value;
        }

        // O(n) insertion at arbitrary index due to right-shifting elements
        public void insertAt(int index, int value) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
            }
            if (size == backingStore.length) {
                resize(backingStore.length * 2);
            }
            // Shift elements right starting from the tail to avoid overwriting
            for (int i = size; i > index; i--) {
                backingStore[i] = backingStore[i - 1];
            }
            backingStore[index] = value;
            size++;
        }

        // O(n) deletion at arbitrary index due to left-shifting elements
        public int deleteAt(int index) {
            validateIndex(index);
            int removed = backingStore[index];
            for (int i = index; i < size - 1; i++) {
                backingStore[i] = backingStore[i + 1];
            }
            backingStore[size - 1] = 0; // Clear stale slot
            size--;
            return removed;
        }

        // O(1) direct random access via contiguous memory offset
        public int get(int index) {
            validateIndex(index);
            return backingStore[index];
        }

        public void set(int index, int value) {
            validateIndex(index);
            backingStore[index] = value;
        }

        public int size() { return size; }
        public int capacity() { return backingStore.length; }
        public int getResizeCount() { return resizeCount; }

        // Two-Pointer In-Place Reversal: O(n) time, O(1) space
        public void reverseInPlace() {
            int left = 0;
            int right = size - 1;
            while (left < right) {
                int temp = backingStore[left];
                backingStore[left] = backingStore[right];
                backingStore[right] = temp;
                left++;
                right--;
            }
        }

        private void resize(int newCapacity) {
            int[] newBlock = new int[newCapacity];
            System.arraycopy(backingStore, 0, newBlock, 0, size);
            System.out.printf("  [Heap Resize Event] Capacity expanded: %d -> %d (Copied %d elements, O(n))%n",
                    backingStore.length, newCapacity, size);
            backingStore = newBlock; // Old backing array becomes eligible for GC
            resizeCount++;
        }

        private void validateIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
            }
        }

        public int[] toSnapshotArray() {
            return Arrays.copyOf(backingStore, size);
        }
    }

    /**
     * Kadane's Algorithm: Finds contiguous subarray with the maximum sum in O(n) time, O(1) space.
     * Tracks start and end indices for enterprise diagnostic reporting.
     */
    public static void runKadaneMaxSubarray(int[] netCashFlows) {
        int maxSoFar = netCashFlows[0];
        int currentMax = netCashFlows[0];
        int start = 0, end = 0, candidateStart = 0;

        for (int i = 1; i < netCashFlows.length; i++) {
            if (netCashFlows[i] > currentMax + netCashFlows[i]) {
                currentMax = netCashFlows[i];
                candidateStart = i;
            } else {
                currentMax += netCashFlows[i];
            }

            if (currentMax > maxSoFar) {
                maxSoFar = currentMax;
                start = candidateStart;
                end = i;
            }
        }

        System.out.printf("  Input Cash Flows : %s%n", Arrays.toString(netCashFlows));
        System.out.printf("  Peak Profit Sum  : %d (Window Indices [%d..%d])%n", maxSoFar, start, end);
    }

    /**
     * Sliding Window Algorithm: Finds peak k-period sum in O(n) time instead of O(n*k) brute force.
     */
    public static void runSlidingWindowTelemetry(int[] latencyMs, int windowSize) {
        if (latencyMs.length < windowSize) {
            throw new IllegalArgumentException("Array length must be >= windowSize.");
        }
        int windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += latencyMs[i];
        }
        int maxWindowSum = windowSum;
        int peakStartIdx = 0;

        for (int i = windowSize; i < latencyMs.length; i++) {
            windowSum += latencyMs[i] - latencyMs[i - windowSize];
            if (windowSum > maxWindowSum) {
                maxWindowSum = windowSum;
                peakStartIdx = i - windowSize + 1;
            }
        }

        System.out.printf("  Latency Stream (ms): %s%n", Arrays.toString(latencyMs));
        System.out.printf("  Peak %d-Req Window  : Sum = %d ms | Avg = %.2f ms (Start Index: %d)%n",
                windowSize, maxWindowSum, (double) maxWindowSum / windowSize, peakStartIdx);
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 1: ARRAYS, DYNAMIC RESIZING & SUBARRAY ALGORITHMS");
        System.out.println("==========================================================================\n");

        // 1. Dynamic Array Mechanics & Heap Allocation
        System.out.println("--- 1. Dynamic Array Contiguous Memory & Geometric Resizing (2x) ---");
        DynamicIntArray dynArray = new DynamicIntArray(2);
        int[] initialStream = {105, 210, 315, 420, 525};
        for (int val : initialStream) {
            dynArray.append(val);
        }
        System.out.printf("  Current Elements : %s | Size: %d | Capacity: %d | Resizes: %d%n",
                Arrays.toString(dynArray.toSnapshotArray()), dynArray.size(), dynArray.capacity(), dynArray.getResizeCount());

        // 2. O(1) Indexed Lookup, O(n) Insert & Delete
        System.out.println("\n--- 2. O(1) Random Access, O(n) Insert & Delete Operations ---");
        System.out.printf("  O(1) Lookup at index 3: %d%n", dynArray.get(3));

        dynArray.insertAt(2, 999);
        System.out.printf("  After insertAt(2, 999): %s (Elements 2..4 shifted right)%n",
                Arrays.toString(dynArray.toSnapshotArray()));

        int deleted = dynArray.deleteAt(1);
        System.out.printf("  After deleteAt(1) [removed %d]: %s (Elements 2..5 shifted left)%n",
                deleted, Arrays.toString(dynArray.toSnapshotArray()));

        // 3. Two-Pointer In-Place Reversal
        System.out.println("\n--- 3. Two-Pointer In-Place Reversal (O(n) Time, O(1) Space) ---");
        dynArray.reverseInPlace();
        System.out.printf("  Reversed Array   : %s%n", Arrays.toString(dynArray.toSnapshotArray()));

        // 4. Enterprise Subarray Algorithms
        System.out.println("\n--- 4. Enterprise Use Case: Financial P&L & Telemetry Subarray Analytics ---");
        int[] dailyNetProfit = {-25, 40, -10, 65, 30, -90, 55, 20};
        runKadaneMaxSubarray(dailyNetProfit);

        int[] apiLatenciesMs = {18, 22, 45, 80, 95, 30, 15, 25};
        runSlidingWindowTelemetry(apiLatenciesMs, 3);

        // 5. Big-O Summary Table
        System.out.println("\n--- 5. Big-O Time & Space Complexity Breakdown (Dynamic Array) ---");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Indexed Access get(i)  | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | Append (End)           | O(1)         | O(1) amort.  | O(n) resize  | O(n)       |");
        System.out.println("  | Insert at Index k      | O(1) (tail)  | O(n)         | O(n)         | O(1)       |");
        System.out.println("  | Delete at Index k      | O(1) (tail)  | O(n)         | O(n)         | O(1)       |");
        System.out.println("  | Two-Pointer Reversal   | O(n)         | O(n)         | O(n)         | O(1)       |");
        System.out.println("  | Kadane / Sliding Win.  | O(n)         | O(n)         | O(n)         | O(1)       |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 1 COMPLETE: ALL ARRAY OPERATIONS VERIFIED");
        System.out.println("==========================================================================");
    }
}

