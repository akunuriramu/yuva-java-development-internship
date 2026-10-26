package week2;

/**
 * Week 2 - Topic 6: Importance of OOP: Procedural vs. Object-Oriented Software Design
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - Procedural Programming focuses on procedures (functions) operating on raw, loose data structures.
 *   As systems grow, procedural code degrades into "spaghetti code" marked by high coupling,
 *   low cohesion, scattered business rules, and widespread ripple effects.
 * - Object-Oriented Programming binds data and behavior together inside cohesive models.
 * - Comparative Software Engineering Dimensions:
 *     1. Maintainability: Encapsulation confines bugs to single class boundaries.
 *     2. Extensibility (Open/Closed Principle): New features are added via new subclasses without
 *        altering existing, production-tested code.
 *     3. Code Reusability: Common mechanics are centralized in base classes and shared interfaces.
 *     4. Testability: Isolated objects with dependency injection are trivial to mock and unit test.
 */
public class ImportanceOfOOPDemo {

    // =========================================================================
    // SECTION A: The Procedural Approach (Spaghetti Architecture Demonstration)
    // =========================================================================
    public static class ProceduralLoanSystem {
        // Loose parallel arrays: fragile index alignment, zero encapsulation!
        public static String[] applicantNames = {"Ramesh Gupta", "Sita Murthy", "Vikram Malhotra"};
        public static String[] loanTypes = {"HOME", "PERSONAL", "EDUCATION"};
        public static double[] principalAmounts = {3500000.0, 400000.0, 1200000.0};
        public static int[] creditScores = {780, 640, 720};

        // Procedural procedure with giant branching (violates Open/Closed Principle)
        public static void processProceduralLoans() {
            System.out.println("  [Procedural Processing via Parallel Arrays & Giant Switch/If Ladder]:");
            for (int i = 0; i < applicantNames.length; i++) {
                double interestRate = 0.0;
                boolean approved = false;

                // Fragile logic: Must edit this function every time a new loan type is introduced!
                if ("HOME".equals(loanTypes[i])) {
                    if (creditScores[i] >= 750) {
                        interestRate = 8.5;
                        approved = true;
                    } else {
                        interestRate = 9.5;
                        approved = (creditScores[i] >= 650);
                    }
                } else if ("PERSONAL".equals(loanTypes[i])) {
                    if (creditScores[i] >= 700) {
                        interestRate = 12.0;
                        approved = true;
                    } else {
                        interestRate = 14.5;
                        approved = (creditScores[i] >= 620);
                    }
                } else if ("EDUCATION".equals(loanTypes[i])) {
                    interestRate = 7.5;
                    approved = (creditScores[i] >= 600);
                }

                double emi = approved ? (principalAmounts[i] * (interestRate / 100.0)) / 12.0 : 0.0;

                System.out.printf("    Applicant: %-16s | Type: %-9s | Rate: %4.1f%% | Approved: %-5s | Approx Monthly Int: Rs. %,.2f%n",
                        applicantNames[i], loanTypes[i], interestRate, (approved ? "YES" : "NO"), emi);
            }
        }
    }

    // =========================================================================
    // SECTION B: The Object-Oriented Approach (Clean, Extensible Domain Model)
    // =========================================================================
    
    // Encapsulated Domain Object
    public static class LoanApplicant {
        private final String applicantName;
        private final int creditScore;
        private final double requestedAmount;

        public LoanApplicant(String applicantName, int creditScore, double requestedAmount) {
            this.applicantName = applicantName;
            this.creditScore = creditScore;
            this.requestedAmount = requestedAmount;
        }

        public String getApplicantName() { return applicantName; }
        public int getCreditScore() { return creditScore; }
        public double getRequestedAmount() { return requestedAmount; }
    }

    // Polymorphic Strategy Hierarchy (Adheres to Open/Closed Principle)
    public static abstract class LoanProduct {
        protected String productName;

        public LoanProduct(String productName) {
            this.productName = productName;
        }

        public abstract boolean evaluateApproval(LoanApplicant applicant);
        public abstract double determineInterestRate(LoanApplicant applicant);

        public void evaluateAndPrint(LoanApplicant applicant) {
            boolean approved = evaluateApproval(applicant);
            double rate = determineInterestRate(applicant);
            double monthlyInterest = approved ? (applicant.getRequestedAmount() * (rate / 100.0)) / 12.0 : 0.0;

            System.out.printf("    Applicant: %-16s | Product: %-10s | Rate: %4.1f%% | Approved: %-5s | Approx Monthly Int: Rs. %,.2f%n",
                    applicant.getApplicantName(), this.productName, rate, (approved ? "YES" : "NO"), monthlyInterest);
        }
    }

    // Subclass 1: HomeLoan
    public static class HomeLoan extends LoanProduct {
        public HomeLoan() { super("Home Loan"); }

        @Override
        public boolean evaluateApproval(LoanApplicant applicant) {
            return applicant.getCreditScore() >= 650;
        }

        @Override
        public double determineInterestRate(LoanApplicant applicant) {
            return (applicant.getCreditScore() >= 750) ? 8.5 : 9.5;
        }
    }

    // Subclass 2: PersonalLoan
    public static class PersonalLoan extends LoanProduct {
        public PersonalLoan() { super("Personal"); }

        @Override
        public boolean evaluateApproval(LoanApplicant applicant) {
            return applicant.getCreditScore() >= 620;
        }

        @Override
        public double determineInterestRate(LoanApplicant applicant) {
            return (applicant.getCreditScore() >= 700) ? 12.0 : 14.5;
        }
    }

    // Subclass 3: EducationLoan
    public static class EducationLoan extends LoanProduct {
        public EducationLoan() { super("Education"); }

        @Override
        public boolean evaluateApproval(LoanApplicant applicant) {
            return applicant.getCreditScore() >= 600;
        }

        @Override
        public double determineInterestRate(LoanApplicant applicant) {
            return 7.5; // Subsidized flat rate
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 6: IMPORTANCE OF OOP (PROCEDURAL VS. OOP DESIGN)");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Procedural Demonstration
        // ---------------------------------------------------------------------
        System.out.println("--- 1. The Procedural Spaghetti Pattern ---");
        System.out.println("Defects: Parallel arrays (risk of index desynchronization), rigid switch logic,");
        System.out.println("         zero information hiding, any schema change breaks all procedural functions.\n");
        ProceduralLoanSystem.processProceduralLoans();

        // ---------------------------------------------------------------------
        // 2. Object-Oriented Demonstration
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. The Modular Object-Oriented Pattern ---");
        System.out.println("Benefits: Encapsulated applicants, polymorphic loan products, Open/Closed Principle.");
        System.out.println("          Adding a 'VehicleLoan' requires ZERO changes to existing classes!\n");

        LoanApplicant[] applicants = new LoanApplicant[] {
            new LoanApplicant("Ramesh Gupta", 780, 3500000.0),
            new LoanApplicant("Sita Murthy", 640, 400000.0),
            new LoanApplicant("Vikram Malhotra", 720, 1200000.0)
        };

        LoanProduct[] products = new LoanProduct[] {
            new HomeLoan(),
            new PersonalLoan(),
            new EducationLoan()
        };

        for (int i = 0; i < applicants.length; i++) {
            products[i].evaluateAndPrint(applicants[i]);
        }

        // ---------------------------------------------------------------------
        // 3. Side-by-Side Architectural Evaluation Matrix
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Architectural Comparison: Procedural vs. OOP ---");
        System.out.println("  +-------------------+-----------------------------------+-----------------------------------+");
        System.out.println("  | Criterion         | Procedural Programming            | Object-Oriented Programming       |");
        System.out.println("  +-------------------+-----------------------------------+-----------------------------------+");
        System.out.println("  | Maintainability   | Low (Spaghetti branching; ripple  | High (Encapsulated classes; bugs  |");
        System.out.println("  |                   | effects on any code change)       | isolated to local boundaries)     |");
        System.out.println("  | Extensibility     | Requires modifying core if-else   | Open/Closed: Add new classes      |");
        System.out.println("  |                   | statements across all functions   | without touching existing code    |");
        System.out.println("  | Code Reuse        | Copy-paste duplication            | Inheritance & polymorphic reuse   |");
        System.out.println("  | Testability       | Difficult; global state causes    | Excellent; loose coupling enables |");
        System.out.println("  |                   | hidden side effects               | unit testing with mock objects    |");
        System.out.println("  +-------------------+-----------------------------------+-----------------------------------+");

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 6 DEMONSTRATION COMPLETE: ARCHITECTURAL PARADIGM CONTRASTED");
        System.out.println("==========================================================================\n");
    }
}
