/**
 * Week 1 - Topic 4: Loops and Iteration Statements in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - Loops enable repeated execution of a statement block until a termination condition is met.
 * - Loop Variations in Java:
 *     1. for loop: Counter-controlled, entry-controlled loop. Ideal when iteration count is known in advance.
 *     2. while loop: Entry-controlled loop. Checks condition before body execution. Ideal when iteration count is dynamic.
 *     3. do-while loop: Exit-controlled loop. Condition checked after execution. Guarantees at least ONE run!
 *     4. Enhanced for-each loop (Java 5+): Clean iteration over arrays and collections without index handling.
 * - Control Statements:
 *     - break: Immediately terminates the loop and transfers control outside.
 *     - continue: Skips remaining statements in the current iteration and advances to next cycle.
 */
public class LoopsDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 4: LOOPS AND ITERATION IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Standard for Loop (Counting and Summation)
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Standard for Loop ---");
        int sumOfNumbers = 0;
        System.out.print("Counting first 5 natural numbers: ");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + (i < 5 ? ", " : ""));
            sumOfNumbers += i;
        }
        System.out.println("\nSum of first 5 natural numbers = " + sumOfNumbers);

        // Nested for loop: Multiplication table snippet
        System.out.println("\nNested for loop (Multiplication Grid 1 to 3):");
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 3; col++) {
                System.out.printf("%2d x %2d = %2d   ", row, col, (row * col));
            }
            System.out.println();
        }

        // ---------------------------------------------------------------------
        // 2. while Loop (Entry-Controlled Loop)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. while Loop (Dynamic Convergence) ---");
        int targetSavings = 50000;
        int currentBalance = 10000;
        int monthlyDeposit = 8500;
        int monthCount = 0;

        System.out.printf("Starting Balance: Rs. %d | Target: Rs. %d | Monthly Deposit: Rs. %d%n",
                currentBalance, targetSavings, monthlyDeposit);

        while (currentBalance < targetSavings) {
            monthCount++;
            currentBalance += monthlyDeposit;
            System.out.printf("Month %d: Account Balance reached Rs. %d%n", monthCount, currentBalance);
        }
        System.out.printf("Target achieved in %d months! Final Balance: Rs. %d%n", monthCount, currentBalance);

        // ---------------------------------------------------------------------
        // 3. do-while Loop (Exit-Controlled Loop)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. do-while Loop (Guaranteed At Least 1 Run) ---");
        int retryAttempts = 0;
        int maxRetries = 3;
        boolean connectionEstablished = false;

        do {
            retryAttempts++;
            System.out.println("Connecting to Database Server... Attempt #" + retryAttempts);
            // Simulate successful connection on attempt 2
            if (retryAttempts == 2) {
                connectionEstablished = true;
                System.out.println("Handshake successful! Server responded.");
            }
        } while (!connectionEstablished && retryAttempts < maxRetries);

        // Also demonstrate condition initially false
        int zeroCount = 0;
        do {
            System.out.println("Proof of single execution even when condition is false (zeroCount > 100): zeroCount is " + zeroCount);
        } while (zeroCount > 100);

        // ---------------------------------------------------------------------
        // 4. Enhanced for-each Loop (Array Iteration)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Enhanced for-each Loop ---");
        String[] coreTechnologies = {"Java 22", "Spring Boot", "MySQL", "Docker", "Git/GitHub"};
        System.out.println("YuvaIntern Tech Stack Skills:");
        int skillIndex = 1;
        for (String tech : coreTechnologies) {
            System.out.println("  " + (skillIndex++) + ". " + tech);
        }

        // ---------------------------------------------------------------------
        // 5. Jump Statements: break and continue
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. Control Jump Statements: break & continue ---");
        
        // break demonstration
        System.out.print("Loop with break (terminates when value == 4): ");
        for (int k = 1; k <= 10; k++) {
            if (k == 4) {
                System.out.print("[BREAK at " + k + "]");
                break;
            }
            System.out.print(k + " ");
        }
        System.out.println();

        // continue demonstration
        System.out.print("Loop with continue (skips even numbers): ");
        for (int k = 1; k <= 10; k++) {
            if (k % 2 == 0) {
                continue; // Skip the rest of this iteration
            }
            System.out.print(k + " ");
        }
        System.out.println();

        // ---------------------------------------------------------------------
        // 6. Practical Placement Use Case: Inventory Batch Low-Stock Alert System
        // ---------------------------------------------------------------------
        System.out.println("\n--- 6. Practical Placement Use Case: Inventory Restock Monitor ---");
        String[] itemCatalog = {"Laptop", "Wireless Mouse", "USB-C Cable", "Mechanical Keyboard", "Monitor Stand"};
        int[] stockLevels = {18, 4, 0, 7, 2};
        int reorderThreshold = 5;

        System.out.printf("%-22s %-10s %-15s%n", "Product Name", "In Stock", "Stock Status");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < itemCatalog.length; i++) {
            String status;
            if (stockLevels[i] == 0) {
                status = "OUT OF STOCK!";
            } else if (stockLevels[i] <= reorderThreshold) {
                status = "LOW - REORDER";
            } else {
                status = "HEALTHY";
            }
            System.out.printf("%-22s %-10d %-15s%n", itemCatalog[i], stockLevels[i], status);
        }
        System.out.println("============================================================\n");
    }
}
