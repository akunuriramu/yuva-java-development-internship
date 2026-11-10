package week3;

import java.util.ArrayList;
import java.util.List;

/**
 * Week 3 - Module 5: Binary Search Tree (BST) from Scratch
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - Hierarchical Heap Nodes: Each TreeNode allocates payload plus `left` and `right` child
 *   pointers on the JVM Heap. Recursive traversals consume Thread Call Stack frames proportional
 *   to tree height h: O(h) space (O(log n) balanced, O(n) skewed).
 * - BST Invariant: For every node N, ALL keys in N.left < N.key < ALL keys in N.right.
 *   Checking only `N.left.key < N.key < N.right.key` is a classic placement bug!
 *   Correct validation enforces strict `(minBound, maxBound)` intervals recursively.
 * - Traversals:
 *   * In-Order (Left -> Root -> Right): Produces elements in strictly sorted ascending order.
 *   * Pre-Order (Root -> Left -> Right): Used for tree serialization / structural cloning.
 *   * Post-Order (Left -> Right -> Root): Used for bottom-up subtree aggregation / deletion.
 * - Enterprise Use Case: Ordered Price Indexing & Range Search in Database Engines.
 */
public class TreeDSDemo {

    public static class TreeNode {
        int key;
        String label;
        TreeNode left;
        TreeNode right;

        public TreeNode(int key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    public static class BinarySearchTree {
        private TreeNode root;

        public void insert(int key, String label) {
            root = insertRecursive(root, key, label);
        }

        private TreeNode insertRecursive(TreeNode node, int key, String label) {
            if (node == null) {
                return new TreeNode(key, label);
            }
            if (key < node.key) {
                node.left = insertRecursive(node.left, key, label);
            } else if (key > node.key) {
                node.right = insertRecursive(node.right, key, label);
            } else {
                node.label = label; // Update metadata for duplicate key
            }
            return node;
        }

        public TreeNode search(int key) {
            return searchRecursive(root, key);
        }

        private TreeNode searchRecursive(TreeNode node, int key) {
            if (node == null || node.key == key) {
                return node;
            }
            return (key < node.key)
                    ? searchRecursive(node.left, key)
                    : searchRecursive(node.right, key);
        }

        public List<Integer> inOrder() {
            List<Integer> result = new ArrayList<>();
            inOrderRec(root, result);
            return result;
        }

        private void inOrderRec(TreeNode node, List<Integer> out) {
            if (node == null) return;
            inOrderRec(node.left, out);
            out.add(node.key);
            inOrderRec(node.right, out);
        }

        public List<Integer> preOrder() {
            List<Integer> result = new ArrayList<>();
            preOrderRec(root, result);
            return result;
        }

        private void preOrderRec(TreeNode node, List<Integer> out) {
            if (node == null) return;
            out.add(node.key);
            preOrderRec(node.left, out);
            preOrderRec(node.right, out);
        }

        public List<Integer> postOrder() {
            List<Integer> result = new ArrayList<>();
            postOrderRec(root, result);
            return result;
        }

        private void postOrderRec(TreeNode node, List<Integer> out) {
            if (node == null) return;
            postOrderRec(node.left, out);
            postOrderRec(node.right, out);
            out.add(node.key);
        }

        // Height defined as maximum number of edges/nodes; here node-depth height (empty = 0)
        public int calculateHeight() {
            return heightRec(root);
        }

        private int heightRec(TreeNode node) {
            if (node == null) return 0;
            return 1 + Math.max(heightRec(node.left), heightRec(node.right));
        }

        // Range Query [min, max] exploiting BST pruning: skips entire subtrees out of range
        public List<String> rangeQuery(int minKey, int maxKey) {
            List<String> matches = new ArrayList<>();
            rangeQueryRec(root, minKey, maxKey, matches);
            return matches;
        }

        private void rangeQueryRec(TreeNode node, int minKey, int maxKey, List<String> matches) {
            if (node == null) return;
            if (node.key > minKey) {
                rangeQueryRec(node.left, minKey, maxKey, matches);
            }
            if (node.key >= minKey && node.key <= maxKey) {
                matches.add(node.key + " (" + node.label + ")");
            }
            if (node.key < maxKey) {
                rangeQueryRec(node.right, minKey, maxKey, matches);
            }
        }

        public TreeNode getRoot() { return root; }
    }

    /**
     * Placement-Critical BST Property Validator:
     * Uses Long.MIN_VALUE and Long.MAX_VALUE bounds to prevent Integer.MIN/MAX edge bugs
     * and catch deep-subtree violations (e.g., 55 placed in left subtree of 50).
     */
    public static boolean isValidBST(TreeNode root) {
        return validateBounds(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean validateBounds(TreeNode node, long minBound, long maxBound) {
        if (node == null) return true;
        if (node.key <= minBound || node.key >= maxBound) {
            return false;
        }
        return validateBounds(node.left, minBound, node.key)
                && validateBounds(node.right, node.key, maxBound);
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 5: BINARY SEARCH TREE (BST), TRAVERSALS & VALIDATION");
        System.out.println("==========================================================================\n");

        BinarySearchTree priceIndex = new BinarySearchTree();
        int[] prices = {500, 250, 750, 120, 350, 620, 900, 300};
        String[] skus = {"Monitor", "Keyboard", "Laptop", "Mouse", "Headset", "Tablet", "Workstation", "Webcam"};

        System.out.println("--- 1. Recursive BST Insertion & Hierarchical Structure ---");
        for (int i = 0; i < prices.length; i++) {
            priceIndex.insert(prices[i], skus[i]);
        }
        System.out.println("  Inserted 8 catalog price nodes into BST (Root = 500: Monitor).");
        System.out.printf("  Tree Height      : %d levels%n", priceIndex.calculateHeight());
        System.out.printf("  BST Valid Check  : %b%n", isValidBST(priceIndex.getRoot()));

        // 2. Traversals
        System.out.println("\n--- 2. Depth-First Traversals (In-Order, Pre-Order, Post-Order) ---");
        System.out.println("  In-Order (Sorted): " + priceIndex.inOrder());
        System.out.println("  Pre-Order (Root) : " + priceIndex.preOrder());
        System.out.println("  Post-Order (Leaf): " + priceIndex.postOrder());

        // 3. Search & Enterprise Range Query
        System.out.println("\n--- 3. Enterprise Use Case: Indexed Search & Price Range Query ---");
        TreeNode hit = priceIndex.search(620);
        TreeNode miss = priceIndex.search(410);
        System.out.println("  Search key=620   : " + (hit != null ? "FOUND -> " + hit.label : "NOT FOUND"));
        System.out.println("  Search key=410   : " + (miss != null ? "FOUND -> " + miss.label : "NOT FOUND"));
        System.out.println("  Range [250..650] : " + priceIndex.rangeQuery(250, 650));

        // 4. Placement Edge Case: Corrupted Subtree Detection
        System.out.println("\n--- 4. Placement Trap: Deep Subtree BST Violation Check ---");
        TreeNode trapRoot = new TreeNode(50, "Root");
        trapRoot.left = new TreeNode(30, "Left");
        trapRoot.right = new TreeNode(70, "Right");
        // Violation: 55 > 50, so it CANNOT reside in the left subtree of 50 even though 55 > 30!
        trapRoot.left.right = new TreeNode(55, "InvalidDeepNode");
        System.out.println("  Injected node(55) as right child of node(30) under root(50).");
        System.out.println("  Naive local check would pass (30 < 55), but Global Bound Validator returns: "
                + isValidBST(trapRoot));

        // 5. Big-O Summary
        System.out.println("\n--- 5. Big-O Time & Space Complexity Breakdown (BST) ---");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Operation              | Best Case    | Average Case | Worst (Skew) | Aux Space  |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Search / Lookup        | O(1) root    | O(log n)     | O(n)         | O(h) stack |");
        System.out.println("  | Recursive Insert       | O(1) root    | O(log n)     | O(n)         | O(h) stack |");
        System.out.println("  | In/Pre/Post Traversal  | O(n)         | O(n)         | O(n)         | O(h) stack |");
        System.out.println("  | Calculate Height       | O(n)         | O(n)         | O(n)         | O(h) stack |");
        System.out.println("  | Validate BST Property  | O(n)         | O(n)         | O(n)         | O(h) stack |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 5 COMPLETE: ALL BST TRAVERSALS & INVARIANTS VERIFIED");
        System.out.println("==========================================================================");
    }
}

