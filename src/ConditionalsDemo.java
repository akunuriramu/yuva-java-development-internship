/**
 * Week 1 - Topic 3: Conditional Statements in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - Decision-making statements control the flow of program execution based on boolean conditions.
 * - Key Conditional Constructs:
 *     1. Simple if: Executes block only when condition is true.
 *     2. if-else: Chooses between two alternative execution paths.
 *     3. if-else-if ladder: Evaluates multiple conditions sequentially until one is satisfied.
 *     4. Nested if: An if block placed inside another if block for multi-level validation.
 *     5. switch statement: Tests a variable against multiple equality cases.
 *        - Supports byte, short, char, int, enum, and String (Java 7+).
 *        - Important placement concepts: 'break' prevents fallthrough; 'default' handles unmatched cases.
 *        - Modern Java (Java 14+) supports enhanced switch expressions with '->' (no break needed).
 */
public class ConditionalsDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 3: CONDITIONAL STATEMENTS IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Simple if Statement
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Simple if Statement ---");
        int studentAttendance = 85; // percentage
        System.out.println("Current Attendance: " + studentAttendance + "%");
        if (studentAttendance >= 75) {
            System.out.println("Status: Eligible to appear for semester examination.");
        }

        // ---------------------------------------------------------------------
        // 2. if-else Statement
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. if-else Statement ---");
        int candidateAge = 19;
        System.out.println("Candidate Age: " + candidateAge);
        if (candidateAge >= 18) {
            System.out.println("Verification: Candidate is an adult and eligible for campus recruitment.");
        } else {
            System.out.println("Verification: Candidate is a minor.");
        }

        // ---------------------------------------------------------------------
        // 3. if-else-if Ladder
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. if-else-if Ladder (Grading Scale) ---");
        double testScore = 84.5;
        char letterGrade;
        String performanceRemark;

        if (testScore >= 90.0) {
            letterGrade = 'O'; // Outstanding
            performanceRemark = "Exceptional performance";
        } else if (testScore >= 80.0) {
            letterGrade = 'A'; // Excellent
            performanceRemark = "Very good mastery of concepts";
        } else if (testScore >= 70.0) {
            letterGrade = 'B';
            performanceRemark = "Good effort, scope for improvement";
        } else if (testScore >= 60.0) {
            letterGrade = 'C';
            performanceRemark = "Average understanding";
        } else if (testScore >= 50.0) {
            letterGrade = 'D';
            performanceRemark = "Pass threshold met";
        } else {
            letterGrade = 'F';
            performanceRemark = "Failed; requires remediation";
        }

        System.out.println("Score: " + testScore + " -> Grade: " + letterGrade + " (" + performanceRemark + ")");

        // ---------------------------------------------------------------------
        // 4. Nested if Statement
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Nested if Statement (Campus Interview Screening) ---");
        boolean clearedOnlineAptitude = true;
        double technicalRoundScore = 78.0;

        if (clearedOnlineAptitude) {
            System.out.println("Screening 1: Cleared Aptitude Assessment.");
            if (technicalRoundScore >= 70.0) {
                System.out.println("Screening 2: Cleared Technical Interview with score " + technicalRoundScore + " -> Selected for HR Round!");
            } else {
                System.out.println("Screening 2: Technical score " + technicalRoundScore + " is below cutoff 70.0 -> Better luck next time.");
            }
        } else {
            System.out.println("Screening 1: Aptitude Assessment not cleared.");
        }

        // ---------------------------------------------------------------------
        // 5. Classic switch Statement (With Break and Fallthrough Demo)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. Classic switch Statement ---");
        int departmentCode = 2;
        String departmentName;

        switch (departmentCode) {
            case 1:
                departmentName = "Computer Science & Engineering (CSE)";
                break;
            case 2:
                departmentName = "Information Technology (IT)";
                break;
            case 3:
                departmentName = "Electronics & Communication (ECE)";
                break;
            case 4:
                departmentName = "Data Science & AI";
                break;
            default:
                departmentName = "General Engineering Sciences";
                break;
        }
        System.out.println("Department Code " + departmentCode + " -> " + departmentName);

        // Demonstrating intentional switch fallthrough (Grouped Cases)
        char workDayType = 'T'; // 'T' = Tuesday
        System.out.print("Day code '" + workDayType + "' classification: ");
        switch (workDayType) {
            case 'M':
            case 'T':
            case 'W':
            case 'R':
            case 'F':
                System.out.println("Week Day (Classes & Lab in session)");
                break;
            case 'S':
            case 'U':
                System.out.println("Weekend (Study & Project Work)");
                break;
            default:
                System.out.println("Unknown day identifier");
                break;
        }

        // Modern Java Switch Expression (Java 14+ feature - placement edge)
        String role = "ADMIN";
        String accessLevel = switch (role) {
            case "ADMIN" -> "Full Administrative Access (Read/Write/Delete)";
            case "MANAGER" -> "Managerial Access (Read/Write)";
            case "DEVELOPER" -> "Developer Access (Read/Execute)";
            default -> "Guest Access (Read-Only)";
        };
        System.out.println("Enhanced Switch: Role '" + role + "' -> " + accessLevel);

        // ---------------------------------------------------------------------
        // 6. Practical Placement Use Case: Loan Eligibility & Credit Risk Engine
        // ---------------------------------------------------------------------
        System.out.println("\n--- 6. Practical Placement Use Case: Loan Eligibility Engine ---");
        int creditScore = 745;
        double annualIncome = 650000.00;
        int activeLoansCount = 1;

        System.out.printf("Applicant Credit Score: %d | Annual Income: Rs. %,.2f | Active Loans: %d%n",
                creditScore, annualIncome, activeLoansCount);

        if (creditScore >= 750) {
            if (activeLoansCount <= 2) {
                System.out.println("Decision: APPROVED - Tier 1 Premium Interest Rate (8.25% p.a.)");
            } else {
                System.out.println("Decision: CONDITIONALLY APPROVED - High active debt, Tier 2 Rate (9.5% p.a.)");
            }
        } else if (creditScore >= 650) {
            if (annualIncome >= 500000.00 && activeLoansCount <= 1) {
                System.out.println("Decision: APPROVED - Standard Rate (10.0% p.a.) with Guarantor requirement");
            } else {
                System.out.println("Decision: UNDERWRITING REVIEW REQUIRED - Additional documentation needed");
            }
        } else {
            System.out.println("Decision: REJECTED - Credit score below minimum threshold (650)");
        }
        System.out.println("============================================================\n");
    }
}
