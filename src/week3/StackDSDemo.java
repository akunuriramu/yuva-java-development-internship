package week3;

import java.util.EmptyStackException;

/**
 * Week 3 - Module 3: Stack (LIFO) Data Structure - Array & Linked Implementations
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - LIFO Principle (Last-In, First-Out): Only the top element is accessible in O(1) time.
 * - Array-Backed Stack: Uses a contiguous array and a `top` index pointer. Cache-friendly
 *   (spatial locality) with amortized O(1) push when auto-resizing, though resizing incurs O(n) copy.
 * - Linked-Node Stack: Allocates each frame on the Heap as a linked `Node` with `top` reference.
 *   Strictly O(1) worst-case push/pop without resize spikes, at the cost of per-node pointer overhead.
 * - JVM Connection: Mirrors the JVM Thread Call Stack where method stack frames are pushed/popped.
 * - Enterprise Use Case: Balanced Delimiter & JSON/Syntax Validator for API Gateways.
 */
public class StackDSDemo {

    // =========================================================================
    // 1. Dynamic Array-Backed Stack
    // =========================================================================
    public static class ArrayStack<T> {
        private Object[] elements;
        private int top;

        public ArrayStack(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Capacity must be > 0");
            }
            this.elements = new Object[initialCapacity];
            this.top = -1;
        }

        public void push(T item) {
            if (top + 1 == elements.length) {
                Object[] expanded = new Object[elements.length * 2];
                System.arraycopy(elements, 0, expanded, 0, elements.length);
                elements = expanded;
            }
            elements[++top] = item;
        }

        @SuppressWarnings("unchecked")
        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            T item = (T) elements[top];
            elements[top--] = null; // Prevent loitering / allow GC
            return item;
        }

        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return (T) elements[top];
        }

        public boolean isEmpty() { return top == -1; }
        public int size() { return top + 1; }
        public int capacity() { return elements.length; }
    }

    // =========================================================================
    // 2. Linked-Node Stack
    // =========================================================================
    public static class LinkedStack<T> {
        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data, Node<T> next) {
                this.data = data;
                this.next = next;
            }
        }

        private Node<T> top;
        private int size;

        public void push(T item) {
            top = new Node<>(item, top);
            size++;
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            T val = top.data;
            top = top.next;
            size--;
            return val;
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return top.data;
        }

        public boolean isEmpty() { return top == null; }
        public int size() { return size; }
    }

    // =========================================================================
    // 3. Enterprise Use Case: Balanced Expression & Delimiter Validator
    // =========================================================================
    public static class ValidationResult {
        final boolean valid;
        final String message;

        ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
    }

    public static ValidationResult validateDelimiters(String expression) {
        LinkedStack<Character> stack = new LinkedStack<>();
        boolean inString = false;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Ignore delimiters inside double-quoted JSON string literals
            if (ch == '"' && (i == 0 || expression.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            if (inString) {
                continue;
            }

            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else if (ch == ')' || ch == '}' || ch == ']') {
                if (stack.isEmpty()) {
                    return new ValidationResult(false, "Unexpected closing '" + ch + "' at index " + i);
                }
                char open = stack.pop();
                if (!isMatchingPair(open, ch)) {
                    return new ValidationResult(false,
                            "Mismatched '" + ch + "' (expected closer for '" + open + "') at idx " + i);
                }
            }
        }

        if (inString) {
            return new ValidationResult(false, "Unterminated string literal detected.");
        }
        if (!stack.isEmpty()) {
            return new ValidationResult(false, "Unclosed opening delimiter '" + stack.peek() + "' remaining on stack.");
        }
        return new ValidationResult(true, "All delimiters properly nested and balanced.");
    }

    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '{' && close == '}')
                || (open == '[' && close == ']');
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 3: STACK (LIFO) - ARRAY, LINKED & EXPRESSION VALIDATOR");
        System.out.println("==========================================================================\n");

        // 1. ArrayStack Demo
        System.out.println("--- 1. Dynamic ArrayStack Mechanics (Contiguous LIFO Buffer) ---");
        ArrayStack<String> callStack = new ArrayStack<>(2);
        callStack.push("main()");
        callStack.push("authenticateRequest()");
        callStack.push("queryDatabase()"); // Triggers capacity 2 -> 4 resize
        System.out.printf("  Top Frame (peek) : %s | Size: %d | Backing Capacity: %d%n",
                callStack.peek(), callStack.size(), callStack.capacity());
        System.out.printf("  Popped Frame     : %s%n", callStack.pop());
        System.out.printf("  New Top (peek)   : %s | Size: %d%n", callStack.peek(), callStack.size());

        // 2. LinkedStack Demo
        System.out.println("\n--- 2. LinkedStack Mechanics (Pointer-Based O(1) Worst-Case Push) ---");
        LinkedStack<Integer> undoStack = new LinkedStack<>();
        undoStack.push(1001);
        undoStack.push(1002);
        undoStack.push(1003);
        System.out.printf("  Top Transaction  : TXN-%d | Size: %d%n", undoStack.peek(), undoStack.size());
        System.out.printf("  Rolled Back      : TXN-%d%n", undoStack.pop());
        System.out.printf("  Current Top      : TXN-%d%n", undoStack.peek());

        // 3. Enterprise Use Case: API Gateway Delimiter / JSON Validator
        System.out.println("\n--- 3. Enterprise Use Case: API Payload & Expression Delimiter Validator ---");
        String[] payloads = {
                "{\"orderId\": 501, \"items\": [{\"sku\": \"A1\", \"qty\": 2}]}",
                "((a + b) * [c - d] / {e + f})",
                "{\"items\": [{\"sku\": \"A1\"}",
                "{[}]",
                "compute((x + y))"
        };

        System.out.println("  +-------------------------------------------------------------+---------+----------------------------------------------------+");
        System.out.println("  | Input Payload / Expression                                  | Status  | Diagnostic Message                                 |");
        System.out.println("  +-------------------------------------------------------------+---------+----------------------------------------------------+");
        for (String payload : payloads) {
            ValidationResult res = validateDelimiters(payload);
            String shortExpr = payload.length() > 59 ? payload.substring(0, 56) + "..." : payload;
            System.out.printf("  | %-59s | %-7s | %-50s |%n",
                    shortExpr, res.valid ? "VALID" : "INVALID", res.message);
        }
        System.out.println("  +-------------------------------------------------------------+---------+----------------------------------------------------+");

        // 4. Big-O Summary
        System.out.println("\n--- 4. Big-O Time & Space Complexity Breakdown (Stack) ---");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | push(item) [Array]     | O(1)         | O(1) amort.  | O(n) resize  | O(1) amort |");
        System.out.println("  | push(item) [Linked]    | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | pop()                  | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | peek()                 | O(1)         | O(1)         | O(1)         | O(1)       |");
        System.out.println("  | Delimiter Validator    | O(n)         | O(n)         | O(n)         | O(n)       |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 3 COMPLETE: ALL STACK OPERATIONS & VALIDATOR VERIFIED");
        System.out.println("==========================================================================");
    }
}

