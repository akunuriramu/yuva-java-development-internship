/**
 * Week 1 - Comprehensive Main Demonstration: Student Academic & Placement Performance Manager
 * Author: YuvaIntern Java Development Intern
 * 
 * Integrated Topics Demonstrated:
 * 1. Variables and Data Types (Primitives, Reference Strings, Widening & Narrowing Casts)
 * 2. Operators (Arithmetic, Relational, Logical, Unary, Compound, Ternary)
 * 3. Conditional Statements (if-else-if ladder, nested if, switch statement)
 * 4. Loops (for, while, do-while, enhanced for-each, break, continue)
 * 5. Arrays (1D arrays for student names and subjects, 2D matrix for marks)
 * 6. Exception Handling (try, catch, finally, throw, throws, custom InvalidMarkException)
 */

// Custom Exception for academic mark validation
class InvalidMarkException extends Exception {
    private final int invalidMark;

    public InvalidMarkException(String message, int invalidMark) {
        super(message);
        this.invalidMark = invalidMark;
    }

    public int getInvalidMark() {
        return invalidMark;
    }
}

public class StudentGradeManagerApp {

    // Global Academic Constants
    public static final String ACADEMY_NAME = "YuvaIntern Academic Development Center";
    public static final int PASS_MARK_THRESHOLD = 50;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("       " + ACADEMY_NAME.toUpperCase());
        System.out.println("       WEEK 1 CAPSTONE: STUDENT ACADEMIC & PLACEMENT MANAGER");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // PART 1: 1D & 2D Arrays Data Initialization (Topic 5)
        // ---------------------------------------------------------------------
        String[] subjects = {"Java Core", "Algorithms", "Database SQL", "Web Basics"};
        String[] studentNames = {"Aarav Sharma", "Diya Patel", "Kabir Mehta", "Ananya Rao"};
        int[] studentAttendance = {92, 84, 68, 95}; // in percentage
        boolean[] hasDisciplinaryWarning = {false, false, false, false};

        // 2D Array: Rows = Students (4), Columns = Subjects (4)
        int[][] rawMarks = {
            {88, 92, 85, 90}, // Aarav Sharma
            {76, 81, 79, 85}, // Diya Patel
            {45, 52, 48, 50}, // Kabir Mehta
            {95, 98, 94, 99}  // Ananya Rao
        };

        int studentCount = studentNames.length;
        int subjectCount = subjects.length;

        System.out.println("Initialized Records: " + studentCount + " students across " + subjectCount + " curriculum subjects.\n");

        // ---------------------------------------------------------------------
        // PART 2: Safe Data Validation using Exception Handling (Topic 6)
        // ---------------------------------------------------------------------
        System.out.println("--- STAGE 1: EXCEPTION HANDLING & DATA INTEGRITY VALIDATION ---");
        try {
            validateAllMarks(rawMarks);
            System.out.println(">> SUCCESS: All matrix marks successfully validated within legal range [0 - 100].");
        } catch (InvalidMarkException ex) {
            System.err.println(">> VALIDATION FAILURE: " + ex.getMessage() + " (Offending value: " + ex.getInvalidMark() + ")");
            return;
        } finally {
            System.out.println(">> DATA CHECK: Validation audit phase concluded.\n");
        }

        // ---------------------------------------------------------------------
        // PART 3: Loop Processing, Calculations, Operators, and Conditionals
        // ---------------------------------------------------------------------
        System.out.println("--- STAGE 2: PERFORMANCE EVALUATION & GRADE COMPUTATION ---");
        System.out.printf("%-4s %-15s %-10s %-8s %-6s %-12s %-15s%n",
                "ID", "Student Name", "Total (/400)", "Average", "Grade", "Status", "Scholarship");
        System.out.println("--------------------------------------------------------------------------");

        double classTotalMarks = 0;
        int topScorerIndex = 0;
        double highestAverage = -1.0;

        // Loop 1: Traditional for loop iterating through each student
        for (int i = 0; i < studentCount; i++) {
            int studentTotal = 0;

            // Loop 2: Inner for loop calculating subject marks sum
            for (int j = 0; j < subjectCount; j++) {
                studentTotal += rawMarks[i][j]; // Compound assignment operator
            }

            // Type casting: Explicit widening to double for exact floating-point division
            double average = (double) studentTotal / subjectCount;
            classTotalMarks += average;

            if (average > highestAverage) {
                highestAverage = average;
                topScorerIndex = i;
            }

            // Topic 3: if-else-if Ladder for Letter Grade
            char letterGrade;
            if (average >= 90.0) {
                letterGrade = 'A';
            } else if (average >= 80.0) {
                letterGrade = 'B';
            } else if (average >= 70.0) {
                letterGrade = 'C';
            } else if (average >= 50.0) {
                letterGrade = 'D';
            } else {
                letterGrade = 'F';
            }

            // Topic 2: Ternary Operator for Pass/Fail status
            String academicStatus = (average >= PASS_MARK_THRESHOLD) ? "PASSED" : "FAILED";

            // Topic 2 & 3: Logical Operators & Nested if for Scholarship Eligibility
            boolean attendanceEligible = (studentAttendance[i] >= 85);
            boolean scoreEligible = (average >= 85.0);
            boolean disciplineClean = !hasDisciplinaryWarning[i];

            boolean isScholarshipAwarded = (attendanceEligible && scoreEligible) && disciplineClean;
            String scholarshipStatus = isScholarshipAwarded ? "ELIGIBLE (50%)" : "NOT ELIGIBLE";

            System.out.printf("%-4d %-15s %-10d %-8.2f %-6c %-12s %-15s%n",
                    (i + 1), studentNames[i], studentTotal, average, letterGrade, academicStatus, scholarshipStatus);
        }

        // ---------------------------------------------------------------------
        // PART 4: Summary Statistics & Department Switch Statement (Topic 3 & 4)
        // ---------------------------------------------------------------------
        double classOverallAverage = classTotalMarks / studentCount;
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("Class Overall Average : %.2f%%%n", classOverallAverage);
        System.out.printf("Top Performing Student: %s (Average: %.2f%%)%n%n",
                studentNames[topScorerIndex], highestAverage);

        // Switch Statement: Administrative Next-Steps Routing based on Student Grade
        System.out.println("--- STAGE 3: ACADEMIC ADVISORY ROUTING (SWITCH STATEMENT) ---");
        char topStudentGrade = (highestAverage >= 90.0) ? 'A' : 'B';

        switch (topStudentGrade) {
            case 'A':
                System.out.println("Advisory for Grade A: Fast-track recommendation for YuvaIntern Advanced R&D Project!");
                break;
            case 'B':
                System.out.println("Advisory for Grade B: Nominated for Standard Industry Certification Track.");
                break;
            case 'C':
            case 'D':
                System.out.println("Advisory: Peer mentoring and tutorial assistance assigned.");
                break;
            default:
                System.out.println("Advisory: Intensive remedial workshop assigned.");
                break;
        }

        // ---------------------------------------------------------------------
        // PART 5: while and do-while Demonstrations (Topic 4)
        // ---------------------------------------------------------------------
        System.out.println("\n--- STAGE 4: SIMULATION LOOPS (WHILE & DO-WHILE) ---");
        
        // while loop: Simulating batch report generation counter
        int processedReports = 0;
        System.out.print("Compiling individual student report cards: ");
        while (processedReports < studentCount) {
            processedReports++;
            System.out.print("[Card #" + processedReports + " Done] ");
        }
        System.out.println();

        // do-while loop: Simulating automated database cache synchronization
        int syncAttempts = 0;
        boolean syncSuccess = false;
        do {
            syncAttempts++;
            if (syncAttempts == 1) {
                syncSuccess = true;
                System.out.println("Database Sync Status: Synced successfully on attempt #" + syncAttempts);
            }
        } while (!syncSuccess && syncAttempts < 3);

        // ---------------------------------------------------------------------
        // PART 6: Enhanced for-each Loop: Subject Curriculum Print (Topic 4 & 5)
        // ---------------------------------------------------------------------
        System.out.println("\n--- STAGE 5: CURRICULUM MODULE REVIEW (ENHANCED FOR-EACH) ---");
        System.out.println("Modules audited during Week 1 training:");
        for (String subject : subjects) {
            System.out.println("  * " + subject);
        }

        System.out.println("\n==========================================================================");
        System.out.println("  DEMONSTRATION RUN FINISHED WITHOUT RUNTIME ERRORS OR ANOMALIES");
        System.out.println("==========================================================================");
    }

    /**
     * Helper method to validate all array elements.
     * Throws InvalidMarkException if any mark is less than 0 or greater than 100.
     */
    public static void validateAllMarks(int[][] marksMatrix) throws InvalidMarkException {
        for (int r = 0; r < marksMatrix.length; r++) {
            for (int c = 0; c < marksMatrix[r].length; c++) {
                int mark = marksMatrix[r][c];
                if (mark < 0 || mark > 100) {
                    throw new InvalidMarkException("Mark out of valid range (0-100) at row " + r + ", col " + c, mark);
                }
            }
        }
    }
}
