/**
 * Week 1 - Topic 1: Variables and Data Types in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - Java is a strongly-typed and statically-typed language (every variable must have a declared type before use).
 * - Primitive Data Types (8 types stored directly in stack memory):
 *     byte (1 byte), short (2 bytes), int (4 bytes), long (8 bytes)
 *     float (4 bytes, 6-7 decimal digits), double (8 bytes, 15-16 decimal digits)
 *     char (2 bytes, 16-bit Unicode character)
 *     boolean (1 bit logical true/false)
 * - Non-Primitive / Reference Data Types:
 *     Classes, Interfaces, Arrays, String (stored in heap memory, reference stored in stack).
 * - Type Casting:
 *     Widening (Implicit): Smaller type to larger type (no data loss).
 *     Narrowing (Explicit): Larger type to smaller type (potential data loss / overflow).
 * - Variable Scopes: Local, Instance, and Static variables.
 */
public class VariablesAndDataTypesDemo {

    // Static / Class variable: Shared across all instances of this class
    public static final String ACADEMIC_TRACK = "Java Software Development Internship";

    // Instance variable: Belongs to an object instance
    private String department = "Engineering & Technology";

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 1: VARIABLES AND DATA TYPES IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Primitive Data Types: Declaration, Memory Size, and Range Examples
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Primitive Data Types ---");

        // Integer primitives
        byte studentAge = 21;                           // 8 bits: -128 to 127
        short academicCredits = 120;                    // 16 bits: -32,768 to 32,767
        int enrollmentId = 20261001;                    // 32 bits: -2^31 to 2^31 - 1
        long nationalRegistrationNumber = 9876543210L;  // 64 bits: Note 'L' suffix for long literals

        // Floating-point primitives
        float semesterGpa = 8.75f;                      // 32 bits: Note 'f' suffix for float literals
        double cumulativeCgpa = 8.925432;               // 64 bits: Standard high-precision float

        // Character and Boolean primitives
        char performanceGrade = 'A';                   // 16 bits Unicode: single quotes
        boolean isEligibleForPlacement = true;          // JVM-dependent (HotSpot: 1 byte in array, 4 bytes stack)

        System.out.println("Byte (Age): " + studentAge + " [Size: 1 byte / 8 bits]");
        System.out.println("Short (Credits): " + academicCredits + " [Size: 2 bytes / 16 bits]");
        System.out.println("Int (Enrollment ID): " + enrollmentId + " [Size: 4 bytes / 32 bits]");
        System.out.println("Long (National ID): " + nationalRegistrationNumber + " [Size: 8 bytes / 64 bits]");
        System.out.println("Float (Semester GPA): " + semesterGpa + " [Size: 4 bytes]");
        System.out.println("Double (Cumulative CGPA): " + cumulativeCgpa + " [Size: 8 bytes]");
        System.out.println("Char (Grade): " + performanceGrade + " [Size: 2 bytes Unicode]");
        System.out.println("Boolean (Placement Eligible?): " + isEligibleForPlacement + " [Size: JVM-dependent (1 byte in array / 4 bytes stack)]");

        // Demonstrating Variable Scopes (Local vs Instance vs Static)
        VariablesAndDataTypesDemo demoInstance = new VariablesAndDataTypesDemo();
        System.out.println("\nVariable Scopes:");
        System.out.println(" - Local Variable (stack frame): studentAge = " + studentAge);
        System.out.println(" - Instance Variable (heap object): department = " + demoInstance.department);
        System.out.println(" - Static Variable (class metadata): ACADEMIC_TRACK = " + ACADEMIC_TRACK);

        // ---------------------------------------------------------------------
        // 2. Non-Primitive (Reference) Data Types: String Class
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Non-Primitive Types (String Reference) ---");
        String studentName = "Aarav Sharma";
        String collegeName = "Yuva National Institute of Technology";
        
        // Demonstrating String immutability and concatenation
        String welcomeMessage = "Student: " + studentName + " | College: " + collegeName;
        System.out.println(welcomeMessage);
        System.out.println("String Length: " + studentName.length() + " characters");
        System.out.println("Uppercase Transformation: " + studentName.toUpperCase());

        // ---------------------------------------------------------------------
        // 3. Type Casting (Conversion Between Types)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Type Casting Demonstrations ---");

        // A. Widening (Implicit) Casting: byte -> short -> int -> long -> float -> double
        int rawScore = 88;
        double convertedScore = rawScore; // Automatically converted from int to double
        System.out.println("Widening Casting (int -> double): " + rawScore + " -> " + convertedScore);

        // B. Narrowing (Explicit) Casting: double -> float -> long -> int -> short -> byte
        double expectedPackageInLakhs = 14.85;
        int roundedPackage = (int) expectedPackageInLakhs; // Truncates decimal part (.85 dropped)
        System.out.println("Narrowing Casting (double -> int): " + expectedPackageInLakhs + " -> " + roundedPackage + " (Decimal truncated)");

        // C. Explicit Casting with Potential Overflow (Placement Question Favorite!)
        int largeNumber = 130;
        byte truncatedByte = (byte) largeNumber; // Byte max value is 127; wraps around to negative
        System.out.println("Overflow Casting (int 130 -> byte): " + truncatedByte + " (Byte overflow wrap-around: 130 - 256 = -126)");

        // ---------------------------------------------------------------------
        // 4. Practical Placement Use Case: Employee Payroll Record Summary
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Practical Placement Use Case: Employee Payroll Record ---");
        String employeeName = "Pooja Verma";
        int employeeId = 5042;
        double baseSalary = 55000.00;
        double allowancePercent = 15.5; // in percentage
        double taxRate = 10.0;          // in percentage

        double calculatedAllowance = baseSalary * (allowancePercent / 100.0);
        double grossSalary = baseSalary + calculatedAllowance;
        double taxDeduction = grossSalary * (taxRate / 100.0);
        double netSalary = grossSalary - taxDeduction;

        System.out.printf("Employee Name   : %s%n", employeeName);
        System.out.printf("Employee ID     : %d%n", employeeId);
        System.out.printf("Base Salary     : Rs. %,.2f%n", baseSalary);
        System.out.printf("Allowance       : Rs. %,.2f (%.1f%%)%n", calculatedAllowance, allowancePercent);
        System.out.printf("Gross Salary    : Rs. %,.2f%n", grossSalary);
        System.out.printf("Tax Deduction   : Rs. %,.2f (%.1f%%)%n", taxDeduction, taxRate);
        System.out.printf("Net In-Hand Pay : Rs. %,.2f%n", netSalary);
        System.out.println("Track           : " + ACADEMIC_TRACK);
        System.out.println("============================================================\n");
    }
}
