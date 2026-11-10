package week3;

import java.util.NoSuchElementException;

/**
 * Week 3 - Module 4: Queue (FIFO) - Circular Array Queue & LinkedList Queue
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - FIFO Principle (First-In, First-Out): Preserves strict arrival ordering.
 * - Naive Array Queue Pitfall: Dequeueing from index 0 requires O(n) shifting, or wastes front slots.
 * - Circular Array Queue: Uses modular arithmetic `(idx + 1) % capacity` for `front` and `rear`
 *   pointers, guaranteeing O(1) enqueue & dequeue with zero element shifting and high CPU cache locality.
 * - LinkedList Queue: Uses `head` (front) and `tail` (rear) node pointers on the Heap for unbounded
 *   O(1) worst-case enqueue and dequeue without resizing pauses.
 * - Enterprise Use Case: Customer Support & Microservice Request Dispatch Engine.
 */
public class QueueDSDemo {

    // =========================================================================
    // 1. Circular Array Queue (Ring Buffer Mechanics)
    // =========================================================================
    public static class CircularArrayQueue<T> {
        private final Object[] buffer;
        private int front;
        private int rear;
        private int size;
        private final int capacity;

        public CircularArrayQueue(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive.");
            }
            this.capacity = capacity;
            this.buffer = new Object[capacity];
            this.front = 0;
            this.rear = -1;
            this.size = 0;
        }

        public void enqueue(T item) {
            if (isFull()) {
                throw new IllegalStateException("Queue Overflow: Ring buffer capacity (" + capacity + ") reached.");
            }
            rear = (rear + 1) % capacity;
            buffer[rear] = item;
            size++;
        }

        @SuppressWarnings("unchecked")
        public T dequeue() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue Underflow: Cannot dequeue from empty queue.");
            }
            T item = (T) buffer[front];
            buffer[front] = null; // Clear reference for GC
            front = (front + 1) % capacity;
            size--;
            return item;
        }

        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue is empty.");
            }
            return (T) buffer[front];
        }

        public boolean isEmpty() { return size == 0; }
        public boolean isFull() { return size == capacity; }
        public int size() { return size; }
        public int getFrontIndex() { return front; }
        public int getRearIndex() { return rear; }
    }

    // =========================================================================
    // 2. Unbounded LinkedList Queue
    // =========================================================================
    public static class LinkedQueue<T> {
        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        private Node<T> head;
        private Node<T> tail;
        private int size;

        public void enqueue(T item) {
            Node<T> node = new Node<>(item);
            if (tail == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            size++;
        }

        public T dequeue() {
            if (head == null) {
                throw new NoSuchElementException("Queue Underflow: LinkedQueue is empty.");
            }
            T val = head.data;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
            return val;
        }

        public T peek() {
            if (head == null) {
                throw new NoSuchElementException("LinkedQueue is empty.");
            }
            return head.data;
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }
    }

    // =========================================================================
    // 3. Enterprise Use Case: Customer Support Ticket Dispatch Engine
    // =========================================================================
    public static class SupportTicket {
        final String ticketId;
        final String customerName;
        final String issueSummary;
        final int arrivalMinute;

        public SupportTicket(String ticketId, String customerName, String issueSummary, int arrivalMinute) {
            this.ticketId = ticketId;
            this.customerName = customerName;
            this.issueSummary = issueSummary;
            this.arrivalMinute = arrivalMinute;
        }
    }

    public static void simulateDispatchEngine() {
        LinkedQueue<SupportTicket> dispatchQueue = new LinkedQueue<>();
        dispatchQueue.enqueue(new SupportTicket("TKT-901", "Ananya R.", "Payment Gateway Timeout", 0));
        dispatchQueue.enqueue(new SupportTicket("TKT-902", "Vikram S.", "API Key Rotation Failure", 2));
        dispatchQueue.enqueue(new SupportTicket("TKT-903", "Meera K.", "Invoice PDF Export Error", 5));
        dispatchQueue.enqueue(new SupportTicket("TKT-904", "Rohan P.", "SSO SAML Callback 403", 7));

        int currentClockMin = 5;
        int serviceDurationMin = 4;

        System.out.println("  +-----------+---------------+---------------------------+---------+------------+-----------+");
        System.out.println("  | Ticket ID | Customer      | Issue Summary             | Arrived | Dispatched | Wait Time |");
        System.out.println("  +-----------+---------------+---------------------------+---------+------------+-----------+");

        while (!dispatchQueue.isEmpty()) {
            SupportTicket t = dispatchQueue.dequeue();
            int startTime = Math.max(currentClockMin, t.arrivalMinute);
            int waitTime = startTime - t.arrivalMinute;
            System.out.printf("  | %-9s | %-13s | %-25s | %4d m  | %7d m  | %6d m  |%n",
                    t.ticketId, t.customerName, t.issueSummary, t.arrivalMinute, startTime, waitTime);
            currentClockMin = startTime + serviceDurationMin;
        }
        System.out.println("  +-----------+---------------+---------------------------+---------+------------+-----------+");
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 4: QUEUE (FIFO) - CIRCULAR RING BUFFER & LINKED QUEUE");
        System.out.println("==========================================================================\n");

        // 1. Circular Array Queue Wrap-Around Demo
        System.out.println("--- 1. Circular Array Queue Wrap-Around & Pointer Mechanics ---");
        CircularArrayQueue<String> ringQueue = new CircularArrayQueue<>(4);
        ringQueue.enqueue("REQ-01");
        ringQueue.enqueue("REQ-02");
        ringQueue.enqueue("REQ-03");
        System.out.printf("  Enqueued 3 items -> front=%d, rear=%d, size=%d%n",
                ringQueue.getFrontIndex(), ringQueue.getRearIndex(), ringQueue.size());

        System.out.printf("  Dequeued: %s | Dequeued: %s%n", ringQueue.dequeue(), ringQueue.dequeue());
        System.out.printf("  After 2 dequeues -> front=%d, rear=%d, size=%d%n",
                ringQueue.getFrontIndex(), ringQueue.getRearIndex(), ringQueue.size());

        // Enqueue 2 more items to demonstrate circular wrap-around of rear pointer
        ringQueue.enqueue("REQ-04");
        ringQueue.enqueue("REQ-05"); // rear wraps from index 3 -> 0
        System.out.printf("  Enqueued REQ-04, REQ-05 (Wrap-Around!) -> front=%d, rear=%d, size=%d, peek=%s%n",
                ringQueue.getFrontIndex(), ringQueue.getRearIndex(), ringQueue.size(), ringQueue.peek());

        // 2. Enterprise Dispatch Simulation
        System.out.println("\n--- 2. Enterprise Use Case: Customer Support Ticket Dispatch Engine ---");
        simulateDispatchEngine();

        // 3. Big-O Summary Table
        System.out.println("\n--- 3. Big-O Time & Space Complexity Breakdown (Queue) ---");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | enqueue(item) [Circ.]  | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | enqueue(item) [Linked] | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | dequeue()              | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | peek()                 | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | search(target)         | O(1)         | O(n)         | O(n)         | O(1)       |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 4 COMPLETE: CIRCULAR & LINKED QUEUE OPERATIONS VERIFIED");
        System.out.println("==========================================================================");
    }
}

