package week2;

/**
 * Week 2 - Topic 4: Compile-Time vs. Runtime Polymorphism, Dynamic Dispatch, and Covariant Returns
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - Polymorphism ("Many Forms") allows a single interface or reference to manipulate different underlying types.
 * 
 * 1. Compile-Time Polymorphism (Static Binding / Method Overloading):
 *    - Resolved at compile-time by matching method signatures (Name + Parameter Types/Count/Order).
 *    - Return type alone DOES NOT differentiate overloaded methods.
 *    - Automatic Type Promotion: If an exact type match is absent, Java promotes primitive types
 *      (byte -> short/char -> int -> long -> float -> double).
 * 
 * 2. Runtime Polymorphism (Dynamic Binding / Method Overriding & Dynamic Method Dispatch):
 *    - Resolved at runtime based on the actual runtime object on the Heap, regardless of the reference type on the Stack.
 *    - JVM executes this via Virtual Method Table (vtable) lookups.
 *    - Covariant Return Types: An overriding subclass method may return a more specific subtype
 *      of the superclass return type (Java 5+).
 *    - Method Hiding vs. Overriding: Static methods cannot be overridden; declaring a static method
 *      with the same signature in a child class merely "hides" the parent method (resolved at compile time).
 */
public class PolymorphismDemo {

    // =========================================================================
    // Receipt Classes Demonstrating Covariant Return Types
    // =========================================================================
    public static class TransactionReceipt {
        protected String receiptId;
        protected double amountPaid;

        public TransactionReceipt(String receiptId, double amountPaid) {
            this.receiptId = receiptId;
            this.amountPaid = amountPaid;
        }

        public String getSummary() {
            return String.format("Standard Receipt #%s for Rs. %,.2f", this.receiptId, this.amountPaid);
        }
    }

    public static class DigitalUpiReceipt extends TransactionReceipt {
        private String upiRefId;

        public DigitalUpiReceipt(String receiptId, double amountPaid, String upiRefId) {
            super(receiptId, amountPaid);
            this.upiRefId = upiRefId;
        }

        @Override
        public String getSummary() {
            return String.format("Digital UPI Receipt #%s [UPI-Ref: %s] for Rs. %,.2f",
                    this.receiptId, this.upiRefId, this.amountPaid);
        }
    }

    // =========================================================================
    // 1. Base Payment Channel (Runtime Polymorphism Superclass)
    // =========================================================================
    public static class PaymentGateway {
        protected String channelName;

        public PaymentGateway(String channelName) {
            this.channelName = channelName;
        }

        // Virtual method to be overridden dynamically
        public TransactionReceipt processPayment(double amount) {
            System.out.printf("  [Base Gateway] Processing generic settlement of Rs. %,.2f via %s%n", amount, this.channelName);
            return new TransactionReceipt("GEN-" + System.currentTimeMillis() % 10000, amount);
        }

        // Static method to demonstrate Method Hiding (NOT overriding)
        public static void printGatewayProtocol() {
            System.out.println("  [STATIC Protocol] Base Gateway: ISO-8583 Banking Message Standard");
        }
    }

    // =========================================================================
    // 2. Subclass 1: CreditCardGateway
    // =========================================================================
    public static class CreditCardGateway extends PaymentGateway {
        private double processingFeePercent;

        public CreditCardGateway(double processingFeePercent) {
            super("Credit Card Network (Visa/Mastercard)");
            this.processingFeePercent = processingFeePercent;
        }

        @Override
        public TransactionReceipt processPayment(double amount) {
            double fee = amount * (this.processingFeePercent / 100.0);
            double total = amount + fee;
            System.out.printf("  [CreditCard Gateway] Amount: Rs. %,.2f + Fee (%.1f%%): Rs. %,.2f -> Total Debited: Rs. %,.2f%n",
                    amount, this.processingFeePercent, fee, total);
            return new TransactionReceipt("CC-" + System.currentTimeMillis() % 10000, total);
        }

        // Hiding (not overriding) the static method
        public static void printGatewayProtocol() {
            System.out.println("  [STATIC Protocol] CreditCard Gateway: PCI-DSS Level 1 Secure Tokenization");
        }
    }

    // =========================================================================
    // 3. Subclass 2: UpiPaymentGateway with Covariant Return Type
    // =========================================================================
    public static class UpiPaymentGateway extends PaymentGateway {
        private String vpaAddress;

        public UpiPaymentGateway(String vpaAddress) {
            super("Unified Payments Interface (UPI 2.0)");
            this.vpaAddress = vpaAddress;
        }

        // COVARIANT RETURN TYPE: Returns DigitalUpiReceipt instead of generic TransactionReceipt
        @Override
        public DigitalUpiReceipt processPayment(double amount) {
            System.out.printf("  [UPI Gateway] Instant debit from VPA: %s for Rs. %,.2f (Zero Merchant Fee)%n",
                    this.vpaAddress, amount);
            String mockUpiRef = "UPI" + (1000000000L + (long)(Math.random() * 9000000000L));
            return new DigitalUpiReceipt("UPI-" + System.currentTimeMillis() % 10000, amount, mockUpiRef);
        }

        // Hiding the static method
        public static void printGatewayProtocol() {
            System.out.println("  [STATIC Protocol] UPI Gateway: NPCI Real-Time IMPS Switch Protocol");
        }
    }

    // =========================================================================
    // 4. Service Calculator Demonstrating Compile-Time Polymorphism (Overloading)
    // =========================================================================
    public static class DiscountCalculator {
        // Overload 1: Flat monetary discount
        public double calculateDiscount(double subtotal, double flatDiscount) {
            System.out.println("  [Overload 1: (double, double)] Applying flat discount of Rs. " + flatDiscount);
            return Math.max(0.0, subtotal - flatDiscount);
        }

        // Overload 2: Percentage discount with integer percentage
        public double calculateDiscount(double subtotal, int percentOff) {
            System.out.println("  [Overload 2: (double, int)] Applying integer percentage discount of " + percentOff + "%");
            double discountAmount = subtotal * (percentOff / 100.0);
            return subtotal - discountAmount;
        }

        // Overload 3: Percentage discount with loyalty coupon code
        public double calculateDiscount(double subtotal, int percentOff, String couponCode) {
            System.out.printf("  [Overload 3: (double, int, String)] Coupon '%s' applied: %d%% + Extra 5%% Loyalty Bonus%n",
                    couponCode, percentOff);
            double totalPercent = (percentOff + 5.0) / 100.0;
            return subtotal - (subtotal * totalPercent);
        }

        // Type Promotion Demonstration: Overload expecting double
        public void processRewardPoints(double points) {
            System.out.println("  [Type Promotion Target (double)] Invoked with double value: " + points);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 4: COMPILE-TIME & RUNTIME POLYMORPHISM ARCHITECTURE");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Compile-Time Polymorphism: Method Overloading & Type Promotion
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Compile-Time Polymorphism (Method Overloading) ---");
        DiscountCalculator calc = new DiscountCalculator();

        double cartTotal = 5000.00;
        System.out.printf("Initial Cart Subtotal: Rs. %,.2f%n%n", cartTotal);

        double res1 = calc.calculateDiscount(cartTotal, 750.00);           // Matches Overload 1
        double res2 = calc.calculateDiscount(cartTotal, 15);               // Matches Overload 2
        double res3 = calc.calculateDiscount(cartTotal, 15, "FESTIVE2026"); // Matches Overload 3

        System.out.printf("Results -> Flat: Rs. %,.2f | Percent: Rs. %,.2f | Coupon: Rs. %,.2f%n", res1, res2, res3);

        System.out.println("\nAutomatic Type Promotion Demonstration:");
        int integerPoints = 250;
        // Compiler promotes 'int' (32 bits) to 'double' (64 bits) automatically
        calc.processRewardPoints(integerPoints);

        // ---------------------------------------------------------------------
        // 2. Runtime Polymorphism: Dynamic Method Dispatch (Virtual Call)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Runtime Polymorphism: Dynamic Method Dispatch ---");
        System.out.println("Polymorphic Array: Superclass reference array holding heterogeneous subclasses:");

        PaymentGateway[] gateways = new PaymentGateway[] {
            new PaymentGateway("Standard Wire Gateway"),
            new CreditCardGateway(2.5),
            new UpiPaymentGateway("merchant@okaxis")
        };

        double orderAmount = 12000.00;

        for (PaymentGateway gateway : gateways) {
            System.out.println("\nExecuting via Reference Type 'PaymentGateway':");
            // Dynamic dispatch: JVM inspects the vtable of the concrete Heap object
            TransactionReceipt receipt = gateway.processPayment(orderAmount);
            System.out.println("  Generated Receipt -> " + receipt.getSummary());
        }

        // ---------------------------------------------------------------------
        // 3. Covariant Return Type Verification
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Covariant Return Type Verification ---");
        UpiPaymentGateway upiGateway = new UpiPaymentGateway("student@okhdfcbank");
        // Direct call returns specific subclass 'DigitalUpiReceipt' without manual typecasting!
        DigitalUpiReceipt specificReceipt = upiGateway.processPayment(3500.00);
        System.out.println("Specific Subclass Receipt: " + specificReceipt.getSummary());

        // ---------------------------------------------------------------------
        // 4. Method Hiding vs. Overriding (Static Method Placement Trap)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Placement Trap: Method Hiding vs. Method Overriding ---");
        PaymentGateway polyRef = new CreditCardGateway(1.8);

        System.out.println("Calling overridden instance method on 'polyRef' (Dynamic Binding at Runtime):");
        polyRef.processPayment(1000.00); // Dispatches to CreditCardGateway.processPayment()

        System.out.println("\nCalling hidden static method on 'polyRef' (Static Binding at Compile Time):");
        // Static method resolution is based on REFERENCE TYPE (PaymentGateway), NOT Heap object!
        polyRef.printGatewayProtocol(); // Prints Base Gateway protocol!

        System.out.println("Direct Class Invocation on CreditCardGateway:");
        CreditCardGateway.printGatewayProtocol(); // Prints CreditCard protocol!

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 4 DEMONSTRATION COMPLETE: DYNAMIC DISPATCH CONFIRMED");
        System.out.println("==========================================================================\n");
    }
}
