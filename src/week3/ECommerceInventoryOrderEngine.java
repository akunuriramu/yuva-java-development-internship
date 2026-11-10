package week3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Week 3 - Integrated Capstone Program: High-Throughput E-Commerce Fulfillment & Inventory Engine
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural Synthesis of All 6 Core Data Structures (Implemented from Scratch):
 * 1. HashMap (Separate Chaining) : O(1) instant product catalog lookup by SKU.
 * 2. Binary Search Tree (BST)    : Price-indexed catalog supporting sorted traversal & O(log n + k) range queries.
 * 3. Circular Queue (FIFO)       : Strict arrival-order customer order fulfillment pipeline.
 * 4. Linked Stack (LIFO)         : Transactional rollback / undo history for inventory & price mutations.
 * 5. Singly Linked List          : Dynamic shipment transit checkpoint chain with mid-route reroute splicing.
 * 6. Fixed Array                 : High-speed contiguous warehouse bin auditing & discrepancy analytics.
 */
public class ECommerceInventoryOrderEngine {

    // =========================================================================
    // Domain Model: Product & Order
    // =========================================================================
    public static class Product {
        final String sku;
        final String name;
        double price;
        int stockQuantity;

        public Product(String sku, String name, double price, int stockQuantity) {
            this.sku = sku;
            this.name = name;
            this.price = price;
            this.stockQuantity = stockQuantity;
        }
    }

    public static class Order {
        final String orderId;
        final String customerName;
        final String sku;
        final int quantity;
        String status;

        public Order(String orderId, String customerName, String sku, int quantity) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.sku = sku;
            this.quantity = quantity;
            this.status = "QUEUED";
        }
    }

    // =========================================================================
    // DS 1: Custom HashMap (O(1) SKU Lookup with Separate Chaining)
    // =========================================================================
    public static class SkuHashMap {
        private static class BucketNode {
            final String key;
            Product value;
            BucketNode next;

            BucketNode(String key, Product value, BucketNode next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }

        private BucketNode[] buckets;
        private int capacity;
        private int size;

        public SkuHashMap(int initialCapacity) {
            this.capacity = initialCapacity;
            this.buckets = new BucketNode[capacity];
        }

        private int indexFor(String key) {
            int h = key.hashCode();
            h ^= (h >>> 16);
            return (h & 0x7FFFFFFF) % capacity;
        }

        public void put(String sku, Product product) {
            int idx = indexFor(sku);
            for (BucketNode curr = buckets[idx]; curr != null; curr = curr.next) {
                if (Objects.equals(curr.key, sku)) {
                    curr.value = product;
                    return;
                }
            }
            buckets[idx] = new BucketNode(sku, product, buckets[idx]);
            size++;
            if ((double) size / capacity > 0.75) {
                rehash();
            }
        }

        public Product get(String sku) {
            int idx = indexFor(sku);
            for (BucketNode curr = buckets[idx]; curr != null; curr = curr.next) {
                if (Objects.equals(curr.key, sku)) {
                    return curr.value;
                }
            }
            return null;
        }

        private void rehash() {
            BucketNode[] oldBuckets = buckets;
            capacity *= 2;
            buckets = new BucketNode[capacity];
            for (BucketNode head : oldBuckets) {
                BucketNode curr = head;
                while (curr != null) {
                    BucketNode next = curr.next;
                    int idx = indexFor(curr.key);
                    curr.next = buckets[idx];
                    buckets[idx] = curr;
                    curr = next;
                }
            }
        }

        public int size() { return size; }
        public int capacity() { return capacity; }
        public double loadFactor() { return (double) size / capacity; }
    }

    // =========================================================================
    // DS 2: Binary Search Tree (Price-Indexed Product Range Queries)
    // =========================================================================
    public static class PriceIndexBST {
        private static class BSTNode {
            final Product product;
            BSTNode left, right;
            BSTNode(Product product) { this.product = product; }
        }

        private BSTNode root;

        public void insert(Product p) {
            root = insertRec(root, p);
        }

        private BSTNode insertRec(BSTNode node, Product p) {
            if (node == null) return new BSTNode(p);
            if (p.price < node.product.price) {
                node.left = insertRec(node.left, p);
            } else {
                node.right = insertRec(node.right, p);
            }
            return node;
        }

        public List<Product> getSortedCatalog() {
            List<Product> result = new ArrayList<>();
            inOrder(root, result);
            return result;
        }

        private void inOrder(BSTNode node, List<Product> out) {
            if (node == null) return;
            inOrder(node.left, out);
            out.add(node.product);
            inOrder(node.right, out);
        }

        public List<Product> queryPriceRange(double minPrice, double maxPrice) {
            List<Product> result = new ArrayList<>();
            rangeRec(root, minPrice, maxPrice, result);
            return result;
        }

        private void rangeRec(BSTNode node, double min, double max, List<Product> out) {
            if (node == null) return;
            if (node.product.price > min) rangeRec(node.left, min, max, out);
            if (node.product.price >= min && node.product.price <= max) out.add(node.product);
            if (node.product.price < max) rangeRec(node.right, min, max, out);
        }
    }

    // =========================================================================
    // DS 3: Circular Queue (FIFO Order Fulfillment Pipeline)
    // =========================================================================
    public static class OrderPipelineQueue {
        private final Order[] ring;
        private int front = 0, rear = -1, size = 0;

        public OrderPipelineQueue(int capacity) {
            this.ring = new Order[capacity];
        }

        public void enqueue(Order order) {
            if (size == ring.length) throw new IllegalStateException("Order pipeline saturated!");
            rear = (rear + 1) % ring.length;
            ring[rear] = order;
            size++;
        }

        public Order dequeue() {
            if (size == 0) return null;
            Order o = ring[front];
            ring[front] = null;
            front = (front + 1) % ring.length;
            size--;
            return o;
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }
    }

    // =========================================================================
    // DS 4: Linked Stack (Transactional Undo / Rollback History)
    // =========================================================================
    public static class TransactionAction {
        final String actionType;
        final String sku;
        final int previousStock;
        final double previousPrice;

        TransactionAction(String actionType, String sku, int previousStock, double previousPrice) {
            this.actionType = actionType;
            this.sku = sku;
            this.previousStock = previousStock;
            this.previousPrice = previousPrice;
        }
    }

    public static class RollbackStack {
        private static class StackNode {
            final TransactionAction action;
            final StackNode next;
            StackNode(TransactionAction action, StackNode next) {
                this.action = action;
                this.next = next;
            }
        }

        private StackNode top;
        private int size;

        public void push(TransactionAction action) {
            top = new StackNode(action, top);
            size++;
        }

        public TransactionAction pop() {
            if (top == null) return null;
            TransactionAction a = top.action;
            top = top.next;
            size--;
            return a;
        }

        public boolean isEmpty() { return top == null; }
        public int size() { return size; }
    }

    // =========================================================================
    // DS 5: Singly Linked List (Dynamic Shipment Transit Checkpoints)
    // =========================================================================
    public static class ShipmentRouteList {
        private static class CheckpointNode {
            final String hubName;
            final String timestamp;
            CheckpointNode next;

            CheckpointNode(String hubName, String timestamp) {
                this.hubName = hubName;
                this.timestamp = timestamp;
            }
        }

        private CheckpointNode head;
        private CheckpointNode tail;
        private int count;

        public void appendCheckpoint(String hubName, String timestamp) {
            CheckpointNode node = new CheckpointNode(hubName, timestamp);
            if (head == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            count++;
        }

        public void insertRerouteAt(int index, String hubName, String timestamp) {
            if (index <= 0) {
                CheckpointNode node = new CheckpointNode(hubName, timestamp);
                node.next = head;
                head = node;
                if (tail == null) tail = node;
                count++;
                return;
            }
            if (index >= count) {
                appendCheckpoint(hubName, timestamp);
                return;
            }
            CheckpointNode prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            CheckpointNode node = new CheckpointNode(hubName, timestamp);
            node.next = prev.next;
            prev.next = node;
            count++;
        }

        public void printRoute(String shipmentId) {
            System.out.println("  Transit Route for [" + shipmentId + "] (" + count + " hops):");
            CheckpointNode curr = head;
            int hop = 1;
            while (curr != null) {
                System.out.printf("    Hop %d: %-30s @ %s%s%n",
                        hop++, curr.hubName, curr.timestamp, (curr.next != null ? "  -->" : "  [DELIVERED]"));
                curr = curr.next;
            }
        }
    }

    // =========================================================================
    // Engine Orchestration
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("====================================================================================");
        System.out.println("     YUVAINTERN E-COMMERCE FULFILLMENT & INVENTORY ENGINE (WEEK 3 CAPSTONE)");
        System.out.println("  Integrated Architecture: HashMap + BST + Queue + Stack + LinkedList + Array");
        System.out.println("====================================================================================\n");

        SkuHashMap catalogMap = new SkuHashMap(4);
        PriceIndexBST priceBst = new PriceIndexBST();
        OrderPipelineQueue orderQueue = new OrderPipelineQueue(10);
        RollbackStack undoStack = new RollbackStack();

        // 1. Fixed Array Batch Inventory Audit (DS #6: Contiguous Array)
        System.out.println("--- 1. Fixed Array: Warehouse Batch Inventory Ingestion & Audit ---");
        Product[] batchAuditArray = new Product[] {
                new Product("SKU-101", "Mechanical Keyboard Pro", 129.99, 45),
                new Product("SKU-102", "USB-C Docking Hub",        79.50, 80),
                new Product("SKU-103", "4K IPS Studio Monitor",   449.00, 18),
                new Product("SKU-104", "Ergonomic Vertical Mouse", 54.00, 60),
                new Product("SKU-105", "NVMe Gen4 SSD 2TB",       189.00, 32),
                new Product("SKU-106", "Noise-Cancel Headset",    249.50, 25)
        };

        double totalBatchValuation = 0.0;
        int totalUnits = 0;
        System.out.println("  +----------+--------------------------+------------+----------+------------------+");
        System.out.println("  | SKU      | Product Name             | Unit Price | In Stock | Batch Valuation  |");
        System.out.println("  +----------+--------------------------+------------+----------+------------------+");
        for (int i = 0; i < batchAuditArray.length; i++) {
            Product p = batchAuditArray[i];
            double val = p.price * p.stockQuantity;
            totalBatchValuation += val;
            totalUnits += p.stockQuantity;
            catalogMap.put(p.sku, p);
            priceBst.insert(p);
            System.out.printf("  | %-8s | %-24s | $%9.2f | %8d | $%15.2f |%n",
                    p.sku, p.name, p.price, p.stockQuantity, val);
        }
        System.out.println("  +----------+--------------------------+------------+----------+------------------+");
        System.out.printf("  Array Audit Summary: %d SKUs | %d Total Units | Total Valuation: $%,.2f%n",
                batchAuditArray.length, totalUnits, totalBatchValuation);

        // 2. HashMap O(1) SKU Lookup (DS #1)
        System.out.println("\n--- 2. Custom HashMap: O(1) Instant SKU Catalog Lookup ---");
        System.out.printf("  HashMap Telemetry -> Entries: %d | Buckets: %d | Load Factor: %.2f%n",
                catalogMap.size(), catalogMap.capacity(), catalogMap.loadFactor());
        String[] querySkus = {"SKU-103", "SKU-105", "SKU-999"};
        for (String sku : querySkus) {
            Product found = catalogMap.get(sku);
            if (found != null) {
                System.out.printf("  Lookup [%s] -> HIT: %-24s | Price: $%.2f | Stock: %d%n",
                        sku, found.name, found.price, found.stockQuantity);
            } else {
                System.out.printf("  Lookup [%s] -> MISS: SKU not registered in catalog.%n", sku);
            }
        }

        // 3. Binary Search Tree Sorted Catalog & Price Range Query (DS #2)
        System.out.println("\n--- 3. Binary Search Tree: Price-Sorted Catalog & Range Query [$100 - $260] ---");
        List<Product> rangeHits = priceBst.queryPriceRange(100.00, 260.00);
        System.out.println("  +----------+--------------------------+------------+");
        System.out.println("  | SKU      | Product Name             | Price      |");
        System.out.println("  +----------+--------------------------+------------+");
        for (Product p : rangeHits) {
            System.out.printf("  | %-8s | %-24s | $%9.2f |%n", p.sku, p.name, p.price);
        }
        System.out.println("  +----------+--------------------------+------------+");

        // 4. Circular Queue FIFO Order Fulfillment (DS #3) + Stack Audit Recording (DS #4)
        System.out.println("\n--- 4. Circular Queue: FIFO Order Fulfillment Pipeline ---");
        orderQueue.enqueue(new Order("ORD-5001", "Aarav Mehta",  "SKU-101", 3));
        orderQueue.enqueue(new Order("ORD-5002", "Priya Nair",   "SKU-103", 2));
        orderQueue.enqueue(new Order("ORD-5003", "Karthik Rao",  "SKU-105", 40)); // Exceeds stock (32) -> Rejected
        orderQueue.enqueue(new Order("ORD-5004", "Sneha Iyer",   "SKU-102", 5));

        System.out.println("  +----------+--------------+----------+-----+------------+--------------+");
        System.out.println("  | Order ID | Customer     | SKU      | Qty | Status     | Post-Stock   |");
        System.out.println("  +----------+--------------+----------+-----+------------+--------------+");
        while (!orderQueue.isEmpty()) {
            Order o = orderQueue.dequeue();
            Product p = catalogMap.get(o.sku);
            if (p != null && p.stockQuantity >= o.quantity) {
                undoStack.push(new TransactionAction("FULFILL_" + o.orderId, p.sku, p.stockQuantity, p.price));
                p.stockQuantity -= o.quantity;
                o.status = "FULFILLED";
            } else {
                o.status = "BACKORDER";
            }
            System.out.printf("  | %-8s | %-12s | %-8s | %3d | %-10s | %12d |%n",
                    o.orderId, o.customerName, o.sku, o.quantity, o.status, (p != null ? p.stockQuantity : 0));
        }
        System.out.println("  +----------+--------------+----------+-----+------------+--------------+");

        // 5. Stack LIFO Transactional Rollback (DS #4)
        System.out.println("\n--- 5. Linked Stack: LIFO Transactional Undo / Rollback History ---");
        Product monitor = catalogMap.get("SKU-103");
        undoStack.push(new TransactionAction("PROMO_PRICE_CUT", monitor.sku, monitor.stockQuantity, monitor.price));
        monitor.price = 399.00;
        System.out.printf("  Applied Promo Price Cut on SKU-103 -> New Price: $%.2f (Undo Stack Depth: %d)%n",
                monitor.price, undoStack.size());

        // Rollback last 2 actions
        for (int i = 0; i < 2 && !undoStack.isEmpty(); i++) {
            TransactionAction last = undoStack.pop();
            Product target = catalogMap.get(last.sku);
            target.stockQuantity = last.previousStock;
            target.price = last.previousPrice;
            System.out.printf("  [ROLLBACK] Reverted %-16s on %s -> Restored Stock: %d | Price: $%.2f%n",
                    last.actionType, last.sku, target.stockQuantity, target.price);
        }

        // 6. Linked List Dynamic Shipment Checkpoints (DS #5)
        System.out.println("\n--- 6. Singly Linked List: Dynamic Shipment Transit Checkpoints ---");
        ShipmentRouteList route = new ShipmentRouteList();
        route.appendCheckpoint("Bengaluru Central Hub", "2026-11-10 08:15");
        route.appendCheckpoint("Hyderabad Regional Sort", "2026-11-10 14:40");
        route.appendCheckpoint("Mumbai Final Mile Depot", "2026-11-11 06:30");
        // Dynamic mid-route reroute insertion in O(1) pointer splice once positioned
        route.insertRerouteAt(2, "Pune Customs Clearance Hub", "2026-11-10 21:05");
        route.printRoute("SHP-ORD-5001");

        System.out.println("\n====================================================================================");
        System.out.println("  WEEK 3 CAPSTONE EXECUTION COMPLETE: ALL 6 DATA STRUCTURES OPERATIONAL");
        System.out.println("====================================================================================");
    }
}

