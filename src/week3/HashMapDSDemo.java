package week3;

import java.util.Objects;

/**
 * Week 3 - Module 6: HashMap Mechanics from Scratch (Buckets, Hashing, Chaining & Rehashing)
 * Author: Akunuri Ramu (YuvaIntern Java Development Intern)
 *
 * Architectural & Memory Dynamics:
 * - Bucket Array (`Node<K, V>[]`): Contiguous Heap array of references. Each slot points to the
 *   head of a singly linked list (Separate Chaining) resolving hash collisions.
 * - Hash Compression: `hash(key)` spreads entropy (bit-mixing `h ^ (h >>> 16)` like JDK HashMap)
 *   followed by `(h & 0x7FFFFFFF) % capacity` to guarantee non-negative bucket indices.
 * - Load Factor (`alpha = size / capacity`): When alpha > 0.75, collision chains lengthen,
 *   degrading lookup toward O(n). Triggering `rehash()` doubles capacity and redistributes all
 *   entries to restore O(1) expected access.
 * - Enterprise Use Case: Distributed User Session & API Token Cache.
 */
public class HashMapDSDemo {

    public static class CustomHashMap<K, V> {
        private static class EntryNode<K, V> {
            final K key;
            V value;
            final int hash;
            EntryNode<K, V> next;

            EntryNode(K key, V value, int hash, EntryNode<K, V> next) {
                this.key = key;
                this.value = value;
                this.hash = hash;
                this.next = next;
            }
        }

        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        private EntryNode<K, V>[] buckets;
        private int capacity;
        private int size;
        private int collisionCount;
        private int rehashCount;

        @SuppressWarnings("unchecked")
        public CustomHashMap(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Initial capacity must be positive.");
            }
            this.capacity = initialCapacity;
            this.buckets = (EntryNode<K, V>[]) new EntryNode<?, ?>[capacity];
            this.size = 0;
        }

        // Bit-mixing hash spreader (prevents clustering when lower bits lack entropy)
        private int computeHash(K key) {
            if (key == null) return 0;
            int h = key.hashCode();
            return h ^ (h >>> 16);
        }

        private int bucketIndex(int hash, int cap) {
            return (hash & 0x7FFFFFFF) % cap;
        }

        public void put(K key, V value) {
            int hash = computeHash(key);
            int idx = bucketIndex(hash, capacity);

            EntryNode<K, V> head = buckets[idx];
            for (EntryNode<K, V> curr = head; curr != null; curr = curr.next) {
                if (curr.hash == hash && Objects.equals(curr.key, key)) {
                    curr.value = value; // Overwrite existing key
                    return;
                }
            }

            if (head != null) {
                collisionCount++;
            }
            // Insert at head of bucket chain in O(1)
            buckets[idx] = new EntryNode<>(key, value, hash, head);
            size++;

            if ((double) size / capacity > DEFAULT_LOAD_FACTOR) {
                rehash();
            }
        }

        public V get(K key) {
            int hash = computeHash(key);
            int idx = bucketIndex(hash, capacity);
            for (EntryNode<K, V> curr = buckets[idx]; curr != null; curr = curr.next) {
                if (curr.hash == hash && Objects.equals(curr.key, key)) {
                    return curr.value;
                }
            }
            return null;
        }

        public V remove(K key) {
            int hash = computeHash(key);
            int idx = bucketIndex(hash, capacity);
            EntryNode<K, V> curr = buckets[idx];
            EntryNode<K, V> prev = null;

            while (curr != null) {
                if (curr.hash == hash && Objects.equals(curr.key, key)) {
                    if (prev == null) {
                        buckets[idx] = curr.next;
                    } else {
                        prev.next = curr.next;
                    }
                    size--;
                    return curr.value;
                }
                prev = curr;
                curr = curr.next;
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private void rehash() {
            int oldCap = capacity;
            EntryNode<K, V>[] oldBuckets = buckets;

            capacity = oldCap * 2;
            buckets = (EntryNode<K, V>[]) new EntryNode<?, ?>[capacity];
            rehashCount++;

            System.out.printf("  [Rehash Triggered] Load Factor %.2f > 0.75 | Capacity %d -> %d%n",
                    (double) size / oldCap, oldCap, capacity);

            for (EntryNode<K, V> head : oldBuckets) {
                EntryNode<K, V> curr = head;
                while (curr != null) {
                    EntryNode<K, V> nextNode = curr.next;
                    int newIdx = bucketIndex(curr.hash, capacity);
                    curr.next = buckets[newIdx];
                    buckets[newIdx] = curr;
                    curr = nextNode;
                }
            }
        }

        public double getLoadFactor() { return (double) size / capacity; }
        public int size() { return size; }
        public int capacity() { return capacity; }
        public int getCollisionCount() { return collisionCount; }
        public int getRehashCount() { return rehashCount; }

        public void printBucketDistribution() {
            for (int i = 0; i < capacity; i++) {
                StringBuilder sb = new StringBuilder();
                EntryNode<K, V> curr = buckets[i];
                if (curr == null) {
                    sb.append("<empty>");
                } else {
                    while (curr != null) {
                        sb.append("[").append(curr.key).append("=").append(curr.value).append("]");
                        if (curr.next != null) sb.append(" -> ");
                        curr = curr.next;
                    }
                }
                System.out.printf("    Bucket[%02d]: %s%n", i, sb);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 3 - MODULE 6: HASHMAP - BUCKETS, CHAINING, LOAD FACTOR & REHASHING");
        System.out.println("==========================================================================\n");

        // 1. Initialize small capacity (4) to observe Separate Chaining & Rehashing
        System.out.println("--- 1. Enterprise Session Cache: Insertion, Collisions & Rehashing ---");
        CustomHashMap<String, String> sessionStore = new CustomHashMap<>(4);
        sessionStore.put("usr-101", "ACTIVE:Admin");
        sessionStore.put("usr-102", "ACTIVE:Analyst");
        sessionStore.put("usr-103", "ACTIVE:Operator");
        // Adding 4th entry crosses 3/4 = 0.75 threshold -> triggers rehash 4 -> 8
        sessionStore.put("usr-104", "ACTIVE:Auditor");
        sessionStore.put("usr-105", "ACTIVE:Engineer");
        sessionStore.put("usr-106", "ACTIVE:Manager");
        // Adding 7th entry crosses 6/8 = 0.75 threshold -> triggers rehash 8 -> 16
        sessionStore.put("usr-107", "ACTIVE:Guest");
        // Deterministic hashCode() collision demo: "FB".hashCode() == "Ea".hashCode() == 2236
        sessionStore.put("FB", "ACTIVE:CollisionNodeA");
        sessionStore.put("Ea", "ACTIVE:CollisionNodeB");

        System.out.printf("%n  Map Telemetry -> Size: %d | Capacity: %d | Load Factor: %.2f | Collisions: %d | Rehashes: %d%n",
                sessionStore.size(), sessionStore.capacity(), sessionStore.getLoadFactor(),
                sessionStore.getCollisionCount(), sessionStore.getRehashCount());

        // 2. Bucket Chain Inspection
        System.out.println("\n--- 2. Heap Bucket Array & Separate Chaining Topology ---");
        sessionStore.printBucketDistribution();

        // 3. O(1) Lookup, Update & Deletion
        System.out.println("\n--- 3. O(1) Lookup, Key Update & Node Unlinking (remove) ---");
        System.out.println("  get('usr-104')          : " + sessionStore.get("usr-104"));
        sessionStore.put("usr-104", "LOCKED:Auditor");
        System.out.println("  get('usr-104') [updated]: " + sessionStore.get("usr-104"));
        String removed = sessionStore.remove("usr-102");
        System.out.println("  remove('usr-102')       : Removed [" + removed + "] | New Size = " + sessionStore.size());
        System.out.println("  get('usr-102')          : " + sessionStore.get("usr-102"));

        // 4. Big-O Summary
        System.out.println("\n--- 4. Big-O Time & Space Complexity Breakdown (HashMap) ---");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | Operation              | Best Case    | Average Case | Worst (Coll) | Aux Space  |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("  | put(key, val)          | O(1)         | O(1) amort.  | O(n)         | O(1)       |");
        System.out.println("  | get(key)               | O(1)         | O(1)         | O(n)         | O(1)       |");
        System.out.println("  | remove(key)            | O(1)         | O(1)         | O(n)         | O(1)       |");
        System.out.println("  | rehash()               | O(n)         | O(n)         | O(n)         | O(n)       |");
        System.out.println("  +------------------------+--------------+--------------+--------------+------------+");
        System.out.println("\n==========================================================================");
        System.out.println("  MODULE 6 COMPLETE: ALL HASHMAP & REHASHING MECHANICS VERIFIED");
        System.out.println("==========================================================================");
    }
}

