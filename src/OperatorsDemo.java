/**
 * Week 1 - Topic 2: Operators in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - Operators are special symbols that perform operations on variables and values (operands).
 * - Types of Operators in Java:
 *     1. Arithmetic (+, -, *, /, %): Basic mathematical calculations; note integer division behavior.
 *     2. Relational / Comparison (==, !=, >, <, >=, <=): Returns boolean result.
 *     3. Logical (&&, ||, !): Logical AND, OR, NOT. Supports Short-Circuit evaluation.
 *     4. Assignment (=, +=, -=, *=, /=, %=): Assigning values with optional operations.
 *     5. Unary (++a, a++, --a, a--, +a, -a, !flag): Operates on single operand; pre vs post increment differences.
 *     6. Ternary (condition ? expr1 : expr2): Compact inline if-else expression.
 *     7. Bitwise (&, |, ^, ~, <<, >>): Bit-level manipulation; frequently asked in coding interviews.
 */
public class OperatorsDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 2: OPERATORS IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Arithmetic Operators
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Arithmetic Operators ---");
        int numA = 25;
        int numB = 4;

        System.out.println("numA = " + numA + ", numB = " + numB);
        System.out.println("Addition (numA + numB)       : " + (numA + numB));
        System.out.println("Subtraction (numA - numB)    : " + (numA - numB));
        System.out.println("Multiplication (numA * numB) : " + (numA * numB));
        System.out.println("Integer Division (numA / numB): " + (numA / numB) + " (Quotient, decimal truncated)");
        System.out.println("Floating Division ((double)numA / numB): " + ((double) numA / numB));
        System.out.println("Modulus (numA % numB)        : " + (numA % numB) + " (Remainder)");

        // ---------------------------------------------------------------------
        // 2. Relational / Comparison Operators
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Relational (Comparison) Operators ---");
        int score1 = 85;
        int score2 = 92;

        System.out.println("score1 = " + score1 + ", score2 = " + score2);
        System.out.println("score1 == score2 : " + (score1 == score2));
        System.out.println("score1 != score2 : " + (score1 != score2));
        System.out.println("score1 > score2  : " + (score1 > score2));
        System.out.println("score1 < score2  : " + (score1 < score2));
        System.out.println("score1 >= 85     : " + (score1 >= 85));
        System.out.println("score2 <= 90     : " + (score2 <= 90));

        // ---------------------------------------------------------------------
        // 3. Logical Operators & Short-Circuit Evaluation
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Logical Operators & Short-Circuit Behavior ---");
        boolean hasBTechDegree = true;
        boolean hasMinimumCgpa = true;
        boolean hasBacklogs = false;

        boolean isPlacementEligible = (hasBTechDegree && hasMinimumCgpa) && !hasBacklogs;
        System.out.println("Degree: " + hasBTechDegree + ", CGPA OK: " + hasMinimumCgpa + ", Backlogs: " + hasBacklogs);
        System.out.println("Placement Eligible ((Degree && CGPA) && !Backlogs): " + isPlacementEligible);

        // Demonstrating Short-Circuit Evaluation:
        // In (false && ++x > 0), the second operand is NEVER evaluated!
        int x = 10;
        boolean shortCircuitAnd = (false && (++x > 10));
        System.out.println("Short-Circuit AND test: condition was false, x remains = " + x + " (No increment occurred)");

        int y = 20;
        boolean shortCircuitOr = (true || (++y > 20));
        System.out.println("Short-Circuit OR test : condition was true, y remains = " + y + " (No increment occurred)");

        // ---------------------------------------------------------------------
        // 4. Unary Operators (Pre vs Post Increment)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Unary Operators (Pre vs Post Increment) ---");
        int count = 5;
        System.out.println("Initial count: " + count);
        System.out.println("Post-increment (count++): " + (count++) + " (Evaluated first, then incremented)");
        System.out.println("Value of count now: " + count);
        System.out.println("Pre-increment  (++count): " + (++count) + " (Incremented first, then evaluated)");
        System.out.println("Value of count now: " + count);

        // Tricky placement expression
        int a = 4;
        int b = 6;
        int complexResult = (a++) + (++b) + (++a); // 4 + 7 + 6 = 17
        System.out.println("Tricky placement expression: (a++) + (++b) + (++a) with a=4, b=6 -> " + complexResult);

        // ---------------------------------------------------------------------
        // 5. Assignment & Compound Assignment Operators
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. Assignment & Compound Assignment Operators ---");
        int balance = 1000;
        System.out.println("Initial balance: " + balance);
        balance += 250; // balance = balance + 250
        System.out.println("After balance += 250 : " + balance);
        balance -= 150; // balance = balance - 150
        System.out.println("After balance -= 150 : " + balance);
        balance *= 2;   // balance = balance * 2
        System.out.println("After balance *= 2   : " + balance);
        balance /= 4;   // balance = balance / 4
        System.out.println("After balance /= 4   : " + balance);
        balance %= 300; // balance = balance % 300
        System.out.println("After balance %= 300 : " + balance);

        // ---------------------------------------------------------------------
        // 6. Ternary Operator
        // ---------------------------------------------------------------------
        System.out.println("\n--- 6. Ternary Operator ---");
        int studentMarks = 72;
        String examResult = (studentMarks >= 40) ? "PASSED WITH FLYING COLORS" : "NEEDS RE-EXAM";
        System.out.println("Marks: " + studentMarks + " -> Result: " + examResult);

        // Nested Ternary
        String gradeBand = (studentMarks >= 90) ? "Grade A" :
                           (studentMarks >= 75) ? "Grade B" :
                           (studentMarks >= 60) ? "Grade C" : "Grade D";
        System.out.println("Grade Band (Nested Ternary): " + gradeBand);

        // ---------------------------------------------------------------------
        // 7. Bitwise Operators (Interview Focus)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 7. Bitwise Operators (Interview Focus) ---");
        int bitA = 5;  // Binary: 0101
        int bitB = 3;  // Binary: 0011

        System.out.println("bitA = " + bitA + " (0101), bitB = " + bitB + " (0011)");
        System.out.println("Bitwise AND (bitA & bitB): " + (bitA & bitB) + " (Binary: 0001 -> 1)");
        System.out.println("Bitwise OR  (bitA | bitB): " + (bitA | bitB) + " (Binary: 0111 -> 7)");
        System.out.println("Bitwise XOR (bitA ^ bitB): " + (bitA ^ bitB) + " (Binary: 0110 -> 6)");
        System.out.println("Bitwise NOT (~bitA)      : " + (~bitA) + " (~5 = -(5+1) = -6)");
        System.out.println("Left Shift  (bitA << 1)  : " + (bitA << 1) + " (5 * 2^1 = 10)");
        System.out.println("Right Shift (bitA >> 1)  : " + (bitA >> 1) + " (5 / 2^1 = 2)");

        // ---------------------------------------------------------------------
        // 8. Practical Placement Use Case: E-Commerce Cart & Discount Engine
        // ---------------------------------------------------------------------
        System.out.println("\n--- 8. Practical Placement Use Case: E-Commerce Cart Engine ---");
        double orderSubtotal = 3200.00;
        boolean isPrimeMember = true;
        boolean hasCouponCode = true;

        // Eligibility using logical operators
        boolean getsSpecialDiscount = (orderSubtotal > 2500.00 && isPrimeMember) || hasCouponCode;
        double discountRate = getsSpecialDiscount ? 0.20 : 0.05; // 20% vs 5%
        double discountAmount = orderSubtotal * discountRate;
        double finalPayable = orderSubtotal - discountAmount;

        System.out.printf("Cart Subtotal       : Rs. %,.2f%n", orderSubtotal);
        System.out.printf("Prime Member?       : %s%n", isPrimeMember);
        System.out.printf("Coupon Applied?     : %s%n", hasCouponCode);
        System.out.printf("Discount Applied    : %.0f%%%n", (discountRate * 100));
        System.out.printf("Discount Savings    : Rs. %,.2f%n", discountAmount);
        System.out.printf("Final Order Total   : Rs. %,.2f%n", finalPayable);
        System.out.println("============================================================\n");
    }
}
