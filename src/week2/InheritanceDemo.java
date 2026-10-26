package week2;

/**
 * Week 2 - Topic 3: Inheritance, Constructor Chaining, and the Diamond Problem
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - Inheritance models the "IS-A" relationship between a superclass (parent) and subclass (child).
 * - Code Reusability: Subclasses inherit all non-private fields and methods from ancestors.
 * - Constructor Chaining Order: When a subclass object is instantiated, the superclass constructor
 *   executes FIRST, traversing up to java.lang.Object before the subclass constructor executes.
 * - 'super' Keyword:
 *     1. super(...): Explicitly invokes a parent class constructor (must be the first line in child constructor).
 *     2. super.method(): Invokes the parent version of an overridden method.
 * - Why Java Forbids Multiple Class Inheritance (The Diamond Problem):
 *     If class D extends both B and C (which both inherit from A and override method m()), calling d.m()
 *     creates method resolution ambiguity. Java eliminates this by enforcing single class inheritance.
 */
public class InheritanceDemo {

    // =========================================================================
    // 1. Base Class: Employee (Top of Hierarchy)
    // =========================================================================
    public static class Employee {
        protected String employeeId;
        protected String fullName;
        protected double baseSalary;

        public Employee(String employeeId, String fullName, double baseSalary) {
            System.out.println("    -> [Constructor 1: Employee] Initializing base employee: " + fullName);
            this.employeeId = employeeId;
            this.fullName = fullName;
            this.baseSalary = baseSalary;
        }

        public double calculateAnnualCompensation() {
            return this.baseSalary * 12.0;
        }

        public void printRoleProfile() {
            System.out.printf("    [Base Employee] ID: %s | Name: %s | Base Salary: Rs. %,.2f/mo%n",
                    this.employeeId, this.fullName, this.baseSalary);
        }
    }

    // =========================================================================
    // 2. Single Inheritance: SoftwareEngineer extends Employee
    // =========================================================================
    public static class SoftwareEngineer extends Employee {
        protected String primaryLanguage;
        protected int pullRequestsMerged;

        public SoftwareEngineer(String employeeId, String fullName, double baseSalary, String primaryLanguage) {
            // Invoking parent constructor via super()
            super(employeeId, fullName, baseSalary);
            System.out.println("    -> [Constructor 2: SoftwareEngineer] Specializing technical stack: " + primaryLanguage);
            this.primaryLanguage = primaryLanguage;
            this.pullRequestsMerged = 0;
        }

        public void recordPullRequest() {
            this.pullRequestsMerged++;
            System.out.println("    [Activity] PR merged by " + this.fullName + ". Total PRs: " + this.pullRequestsMerged);
        }

        @Override
        public double calculateAnnualCompensation() {
            // Reusing parent logic via super.calculateAnnualCompensation() plus engineering bonus
            double baseAnnual = super.calculateAnnualCompensation();
            double techBonus = 75000.00; // Annual tech stipend
            return baseAnnual + techBonus;
        }

        @Override
        public void printRoleProfile() {
            super.printRoleProfile(); // Parent role details
            System.out.printf("    |--> Primary Language: %s | PRs Merged: %d | Annual Comp: Rs. %,.2f%n",
                    this.primaryLanguage, this.pullRequestsMerged, this.calculateAnnualCompensation());
        }
    }

    // =========================================================================
    // 3. Multilevel Inheritance: TechLead extends SoftwareEngineer
    // =========================================================================
    public static class TechLead extends SoftwareEngineer {
        private int teamSize;
        private double leadershipAllowance;

        public TechLead(String employeeId, String fullName, double baseSalary, String primaryLanguage,
                        int teamSize, double leadershipAllowance) {
            // Chaining to SoftwareEngineer constructor (which in turn chains to Employee)
            super(employeeId, fullName, baseSalary, primaryLanguage);
            System.out.println("    -> [Constructor 3: TechLead] Configuring leadership team size: " + teamSize);
            this.teamSize = teamSize;
            this.leadershipAllowance = leadershipAllowance;
        }

        @Override
        public double calculateAnnualCompensation() {
            // Multilevel reuse: SoftwareEngineer annual comp + leadership allowance
            return super.calculateAnnualCompensation() + (this.leadershipAllowance * 12.0);
        }

        @Override
        public void printRoleProfile() {
            super.printRoleProfile();
            System.out.printf("         |--> Tech Lead Scope: Managing %d Engineers | Leadership Allowance: Rs. %,.2f/mo%n",
                    this.teamSize, this.leadershipAllowance);
        }
    }

    // =========================================================================
    // 4. Hierarchical Inheritance: EngineeringManager extends Employee directly
    // =========================================================================
    public static class EngineeringManager extends Employee {
        private String departmentName;
        private double quarterlyBudgetLakhs;

        public EngineeringManager(String employeeId, String fullName, double baseSalary,
                                  String departmentName, double quarterlyBudgetLakhs) {
            super(employeeId, fullName, baseSalary);
            System.out.println("    -> [Constructor: EngineeringManager] Configuring department: " + departmentName);
            this.departmentName = departmentName;
            this.quarterlyBudgetLakhs = quarterlyBudgetLakhs;
        }

        @Override
        public double calculateAnnualCompensation() {
            double baseAnnual = super.calculateAnnualCompensation();
            double executiveBonus = baseAnnual * 0.25; // 25% executive incentive
            return baseAnnual + executiveBonus;
        }

        @Override
        public void printRoleProfile() {
            super.printRoleProfile();
            System.out.printf("    |--> Management Scope: %s | Quarterly Budget: Rs. %,.2f Lakhs | Annual Comp: Rs. %,.2f%n",
                    this.departmentName, this.quarterlyBudgetLakhs, this.calculateAnnualCompensation());
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 3: INHERITANCE, CONSTRUCTOR CHAINING & DIAMOND PROBLEM");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Constructor Chaining Order Demonstration
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Constructor Execution Order in Multilevel Inheritance ---");
        System.out.println("Instantiating TechLead object (Notice parent-to-child execution order):");
        TechLead lead = new TechLead("TL-2045", "Kavita Reddy", 120000.00, "Java / Spring Boot", 8, 25000.00);
        System.out.println("\nTechLead Role Profile:");
        lead.printRoleProfile();

        // ---------------------------------------------------------------------
        // 2. Single and Hierarchical Inheritance Verification
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Single vs. Hierarchical Inheritance Demonstrations ---");
        System.out.println("Instantiating SoftwareEngineer (Single Inheritance):");
        SoftwareEngineer dev = new SoftwareEngineer("SE-8831", "Arjun Nair", 75000.00, "Java / Microservices");
        dev.recordPullRequest();
        dev.recordPullRequest();
        dev.printRoleProfile();

        System.out.println("\nInstantiating EngineeringManager (Hierarchical Inheritance from Employee):");
        EngineeringManager mgr = new EngineeringManager("EM-1002", "Siddharth Rao", 185000.00, "Cloud Platform Engineering", 45.0);
        mgr.printRoleProfile();

        // ---------------------------------------------------------------------
        // 3. Architectural Analysis: Why Java Forbids Multiple Class Inheritance
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. Placement Focus: The Diamond Problem in C++ vs. Java Solution ---");
        System.out.println("The Diamond Problem Topology:");
        System.out.println("             [Class A: performAction()]");
        System.out.println("             /                       \\");
        System.out.println("   [Class B: overrides action]   [Class C: overrides action]");
        System.out.println("             \\                       /");
        System.out.println("           [Class D: extends B, C ???]");
        System.out.println("\nAmbiguity Issue: If Class D called 'super.performAction()', should it invoke B or C?");
        System.out.println("Memory Issue   : If Class A had instance fields, Class D would inherit two copies of A's fields!");
        System.out.println("Java's Design Decision:");
        System.out.println("  1. Java strictly allows single inheritance for classes ('extends' exactly one superclass).");
        System.out.println("  2. Java permits multiple inheritance of TYPE and CONTRACT via Interfaces ('implements').");
        System.out.println("  3. For default method conflicts across multiple interfaces, Java forces the child class");
        System.out.println("     to explicitly override and disambiguate the method call using 'InterfaceName.super.method()'.");

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 3 DEMONSTRATION COMPLETE: INHERITANCE VERIFIED");
        System.out.println("==========================================================================\n");
    }
}
