package week3;

/**
 * Week 3 - Module 2: Custom Singly & Doubly Linked Lists in Java
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - Non-Contiguous Heap Allocation: Each node is an independent Heap object containing payload
 *   and 64-bit (or compressed 32-bit OOP) object reference pointers (`next`, and `prev` in DLL).
 * - Memory Overhead: Unlike primitive arrays, every node incurs a JVM Object Header (12-16 bytes)
 *   plus pointer reference overhead, trading memory compactness for O(1) structural splicing.
 * - Algorithms Implemented:
 *   1. Singly Linked List: Insert at head/tail/k-th position, delete by value/position, traversal.
 *   2. Doubly Linked List: Bidirectional O(1) head/tail insertion and O(1) tail-pointer deletion.
 *   3. Floyd's Tortoise and Hare Algorithm: O(n) time, O(1) space cycle detection and entry point resolution.
 * - Enterprise Use Case: Distributed transaction audit trail & LRU/browser navigation chain.
 */
public class LinkedListDSDemo {

    // =========================================================================
    // 1. Custom Singly Linked List
    // =========================================================================
    public static class SinglyLinkedList<T> {
        public static class Node<T> {
            T data;
            Node<T> next;

            public Node(T data) {
                this.data = data;
                this.next = null;
            }
        }

        private Node<T> head;
        private Node<T> tail;
        private int size;

        public void insertAtHead(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
            size++;
        }

        public void insertAtTail(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        public void insertAtPosition(int index, T data) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Invalid index: " + index + " for size " + size);
            }
            if (index == 0) {
                insertAtHead(data);
                return;
            }
            if (index == size) {
                insertAtTail(data);
                return;
            }
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            Node<T> newNode = new Node<>(data);
            newNode.next = prev.next;
            prev.next = newNode;
            size++;
        }

        public T deleteAtPosition(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Invalid index: " + index + " for size " + size);
            }
            T removedData;
            if (index == 0) {
                removedData = head.data;
                head = head.next;
                if (head == null) {
                    tail = null;
                }
            } else {
                Node<T> prev = head;
                for (int i = 0; i < index - 1; i++) {
                    prev = prev.next;
                }
                removedData = prev.next.data;
                prev.next = prev.next.next;
                if (prev.next == null) {
                    tail = prev;
                }
            }
            size--;
            return removedData;
        }

        public Node<T> getHead() { return head; }
        public int size() { return size; }

        public String formatChain() {
            StringBuilder sb = new StringBuilder();
            Node<T> curr = head;
            while (curr != null) {
                sb.append("[").append(curr.data).append("]");
                if (curr.next != null) {
                    sb.append(" -> ");
                }
                curr = curr.next;
            }
            sb.append(" -> null");
            return sb.toString();
        }
    }

    // =========================================================================
    // 2. Custom Doubly Linked List (Bidirectional Pointers)
    // =========================================================================
    public static class DoublyLinkedList<T> {
        private static class DNode<T> {
            T data;
            DNode<T> prev;
            DNode<T> next;

            DNode(T data) {
                this.data = data;
            }
        }

        private DNode<T> head;
        private DNode<T> tail;
        private int size;

        public void addLast(T data) {
            DNode<T> node = new DNode<>(data);
            if (tail == null) {
                head = tail = node;
            } else {
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
            size++;
        }

        public void addFirst(T data) {
            DNode<T> node = new DNode<>(data);
            if (head == null) {
                head = tail = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
            size++;
        }

        // O(1) removal from tail (impossible in Singly Linked List without O(n) predecessor scan)
        public T removeLast() {
            if (tail == null) {
                throw new IllegalStateException("Cannot remove from empty DoublyLinkedList.");
            }
            T val = tail.data;
            if (head == tail) {
                head = tail = null;
            } else {
                tail = tail.prev;
                tail.next = null; // Sever backward reference so old tail node is eligible for GC
            }
            size--;
            return val;
        }

        public String formatForward() {
            StringBuilder sb = new StringBuilder("null <-> ");
            DNode<T> curr = head;
            while (curr != null) {
                sb.append("[").append(curr.data).append("]");
                if (curr.next != null) sb.append(" <-> ");
                curr = curr.next;
            }
            sb.append(" <-> null");
            return sb.toString();
        }

        public String formatBackward() {
            StringBuilder sb = new StringBuilder("TAIL -> ");
            DNode<T> curr = tail;
            while (curr != null) {
                sb.append("[").append(curr.data).append("]");
                if (curr.prev != null) sb.append(" <- ");
                curr = curr.prev;
            }
            sb.append(" -> HEAD");
            return sb.toString();
        }
    }

    // =========================================================================
    // 3. Floyd's Tortoise and Hare Cycle Detection (O(n) Time, O(1) Space)
    // =========================================================================
    public static <T> SinglyLinkedList.Node<T> detectCycleEntry(SinglyLinkedList.Node<T> head) {
        if (head == null || head.next == null) {
            return null;
        }
        SinglyLinkedList.Node<T> slow = head;
        SinglyLinkedList.Node<T> fast = head;
        boolean hasCycle = false;

        // Phase 1: Detect meeting point inside loop
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        if (!hasCycle) {
            return null;
        }

        // Phase 2: Mathematical invariant - distance from head to cycle start equals
        // distance from meeting point to cycle start
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 2: SINGLY & DOUBLY LINKED LISTS + CYCLE DETECTION");
        System.out.println("==========================================================================\n");

        // 1. Singly Linked List Operations
        System.out.println("--- 1. Singly Linked List: Head, Tail & K-th Position Splicing ---");
        SinglyLinkedList<String> pipeline = new SinglyLinkedList<>();
        pipeline.insertAtTail("AUTH-CHECK");
        pipeline.insertAtTail("FRAUD-SCAN");
        pipeline.insertAtHead("INGRESS-GW");
        pipeline.insertAtPosition(2, "RATE-LIMIT");
        System.out.println("  Pipeline Chain : " + pipeline.formatChain());

        String removed = pipeline.deleteAtPosition(2);
        System.out.println("  Deleted idx 2  : " + removed);
        System.out.println("  Updated Chain  : " + pipeline.formatChain() + " (Size: " + pipeline.size() + ")");

        // 2. Doubly Linked List Operations (Enterprise Browser / Undo Navigation)
        System.out.println("\n--- 2. Doubly Linked List: Enterprise Navigation History ---");
        DoublyLinkedList<String> navHistory = new DoublyLinkedList<>();
        navHistory.addLast("Dashboard");
        navHistory.addLast("Orders");
        navHistory.addLast("Invoice-#402");
        navHistory.addFirst("Login");
        System.out.println("  Forward Pass   : " + navHistory.formatForward());
        System.out.println("  Backward Pass  : " + navHistory.formatBackward());
        System.out.println("  O(1) Tail Pop  : Removed [" + navHistory.removeLast() + "]");
        System.out.println("  After Pop      : " + navHistory.formatForward());

        // 3. Floyd's Cycle Detection
        System.out.println("\n--- 3. Floyd's Tortoise & Hare Cycle Detection (O(n) Time, O(1) Space) ---");
        SinglyLinkedList.Node<String> n1 = new SinglyLinkedList.Node<>("Hop-1: Edge");
        SinglyLinkedList.Node<String> n2 = new SinglyLinkedList.Node<>("Hop-2: CoreRouter");
        SinglyLinkedList.Node<String> n3 = new SinglyLinkedList.Node<>("Hop-3: GatewayA");
        SinglyLinkedList.Node<String> n4 = new SinglyLinkedList.Node<>("Hop-4: GatewayB");
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;

        System.out.println("  Acyclic Check  : Cycle Entry = " + detectCycleEntry(n1));

        // Introduce deliberate routing loop: Hop-4 -> Hop-2
        n4.next = n2;
        SinglyLinkedList.Node<String> entry = detectCycleEntry(n1);
        System.out.println("  Corrupted Loop : n4.next -> n2 injected.");
        System.out.println("  Cycle Detected : " + (entry != null) + " | Loop Entry Node = [" + (entry != null ? entry.data : "none") + "]");

        // 4. Big-O Summary Table
        System.out.println("\n--- 4. Big-O Complexity Breakdown (Singly vs Doubly Linked List) ---");
        System.out.println("  +--------------------------+------------+------------+--------------+------------+");
        System.out.println("  | Operation                | SLL Time   | DLL Time   | Worst Case   | Aux Space  |");
        System.out.println("  +--------------------------+------------+------------+--------------+------------+");
        System.out.println("  | Access / Search by Index | O(n)       | O(n)       | O(n)         | O(1)       |");
        System.out.println("  | Insert / Delete at Head  | O(1)       | O(1)       | O(1)         | O(1)       |");
        System.out.println("  | Insert at Tail (w/ tail) | O(1)       | O(1)       | O(1)         | O(1)       |");
        System.out.println("  | Delete at Tail           | O(n)       | O(1)       | O(n) / O(1)  | O(1)       |");
        System.out.println("  | Insert/Delete at k-th    | O(n)       | O(n)       | O(n)         | O(1)       |");
        System.out.println("  | Floyd's Cycle Detection  | O(n)       | O(n)       | O(n)         | O(1)       |");
        System.out.println("  +--------------------------+------------+------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 2 COMPLETE: ALL LINKED LIST MECHANICS VERIFIED");
        System.out.println("==========================================================================");
    }
}

