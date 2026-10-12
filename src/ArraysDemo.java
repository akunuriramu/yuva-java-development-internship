/**
 * Week 1 - Topic 5: Arrays in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - An Array is an indexed, homogeneous collection of fixed-size elements stored in contiguous memory.
 * - Array Characteristics:
 *     - 0-indexed (indices run from 0 to length - 1).
 *     - Fixed size: Once allocated, length cannot be dynamically grown or shrunk.
 *     - Property: array.length returns total element capacity.
 *     - Stored as objects on the Heap in Java.
 * - Single-Dimensional (1D) Arrays: Linear list of elements.
 * - Multi-Dimensional (2D/Matrix) Arrays: Array of arrays, representing rows and columns.
 * - Fundamental Placement Algorithms:
 *     - Traversal (forward, backward, enhanced for-each)
 *     - Aggregation (sum, average)
 *     - Extremum Search (maximum and minimum element discovery)
 *     - Linear Search (locating an element index by value)
 */
public class ArraysDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 5: ARRAYS IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Single-Dimensional (1D) Array: Declaration and Initialization
        // ---------------------------------------------------------------------
        System.out.println("--- 1. 1D Array Declaration & Initialization ---");
        // Syntax 1: Dynamic allocation followed by assignment
        int[] scores = new int[5];
        scores[0] = 88;
        scores[1] = 92;
        scores[2] = 79;
        scores[3] = 95;
        scores[4] = 84;

        // Syntax 2: Direct array literal initialization
        String[] subjects = {"Data Structures", "Operating Systems", "Computer Networks", "DBMS", "Java OOP"};

        System.out.println("Array Length (subjects.length): " + subjects.length);
        System.out.println("First element (index 0): " + subjects[0] + " -> " + scores[0]);
        System.out.println("Last element  (index 4): " + subjects[subjects.length - 1] + " -> " + scores[scores.length - 1]);

        // ---------------------------------------------------------------------
        // 2. Fundamental Array Operations: Sum, Average, Min, and Max
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Array Statistical Operations (Sum, Avg, Min, Max) ---");
        int sum = 0;
        int maxScore = scores[0];
        int minScore = scores[0];
        int maxIndex = 0;
        int minIndex = 0;

        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];

            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxIndex = i;
            }

            if (scores[i] < minScore) {
                minScore = scores[i];
                minIndex = i;
            }
        }

        double averageScore = (double) sum / scores.length;

        System.out.println("Total Marks Obtained : " + sum + " / 500");
        System.out.printf("Average Score        : %.2f%%%n", averageScore);
        System.out.println("Highest Score        : " + maxScore + " in " + subjects[maxIndex]);
        System.out.println("Lowest Score         : " + minScore + " in " + subjects[minIndex]);

        // ---------------------------------------------------------------------
        // 3. Linear Search Algorithm on Arrays
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Linear Search in an Array ---");
        int targetScore = 95;
        int foundIndex = -1;

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == targetScore) {
                foundIndex = i;
                break; // Found target, terminate search early
            }
        }

        if (foundIndex != -1) {
            System.out.println("Search Success: Target score " + targetScore + " found at index " + foundIndex +
                    " (" + subjects[foundIndex] + ")");
        } else {
            System.out.println("Search Result : Target score " + targetScore + " not found in array.");
        }

        // ---------------------------------------------------------------------
        // 4. Two-Dimensional (2D) Arrays: Matrix Representation
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Multi-Dimensional (2D) Arrays ---");
        // Rows: Students (3 students)
        // Cols: Subject Marks (3 subjects: Math, Science, English)
        int[][] classroomMarks = {
            {85, 90, 88}, // Student 0
            {78, 82, 80}, // Student 1
            {92, 95, 96}  // Student 2
        };

        String[] studentNames = {"Rohan Sharma", "Priya Patel", "Vikram Sen"};
        String[] subjectNames = {"Math", "Science", "English"};

        System.out.println("Matrix Traversal: 3 Students x 3 Subjects");
        System.out.printf("%-15s %-8s %-8s %-8s %-10s%n", "Student", subjectNames[0], subjectNames[1], subjectNames[2], "Total");
        System.out.println("---------------------------------------------------------");

        for (int row = 0; row < classroomMarks.length; row++) {
            int rowTotal = 0;
            System.out.printf("%-15s ", studentNames[row]);
            for (int col = 0; col < classroomMarks[row].length; col++) {
                int mark = classroomMarks[row][col];
                rowTotal += mark;
                System.out.printf("%-8d ", mark);
            }
            System.out.printf("%-10d%n", rowTotal);
        }

        // ---------------------------------------------------------------------
        // 5. Practical Placement Use Case: Batch Test Performance Matrix
        // ---------------------------------------------------------------------
        System.out.println("\n--- 5. Practical Placement Use Case: Subject-wise Class Averages ---");
        for (int col = 0; col < subjectNames.length; col++) {
            int subjectTotal = 0;
            for (int row = 0; row < classroomMarks.length; row++) {
                subjectTotal += classroomMarks[row][col];
            }
            double subjectAvg = (double) subjectTotal / classroomMarks.length;
            System.out.printf("Subject: %-10s | Class Average: %.2f / 100%n", subjectNames[col], subjectAvg);
        }
        System.out.println("============================================================\n");
    }
}
