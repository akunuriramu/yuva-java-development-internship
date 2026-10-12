# YuvaIntern Engineer – Java Development
## Week 1: Java Syntax and Basic Operations

**Candidate Track:** Java Software Development Virtual Internship  
**Project Task:** Week 1 Milestone – Java Syntax, Data Types, Control Structures, Arrays, Exception Handling & Integrated Capstone  
**GitHub Repository:** [https://github.com/akunuriramu/yuva-java-development-internship](https://github.com/akunuriramu/yuva-java-development-internship)  
**Target Environment:** Java SE 22+ (Verified on `Java 22.0.1 64-Bit HotSpot VM`)  
**Host OS:** Windows 11 (PowerShell & CMD compatible)  

---

## 📌 Project Overview

This repository contains the complete implementation for **Week 1 Task: Java Syntax and Basic Operations** of the **YuvaIntern Virtual Internship**. Designed specifically for software placement preparation, this project bridges core academic computer science theory with production-style, compilable Java source code.

Each concept is structured with:
- A clear definition and memory mechanics explanation (Stack vs Heap).
- A standalone, compilable Java source program.
- 100% verified terminal output recorded from actual execution.
- High-frequency campus placement interview questions and real-world business use cases.

---

## 📁 Project Directory Structure

```text
YuvaIntern_Java_Development/
├── docs/
│   └── Task1_Java_Syntax_and_Basic_Operations.docx   # Professional Executive Word Report
├── src/
│   ├── VariablesAndDataTypesDemo.java               # Topic 1: Primitives, Reference Types & Casting
│   ├── OperatorsDemo.java                           # Topic 2: Arithmetic, Logical, Unary & Bitwise
│   ├── ConditionalsDemo.java                        # Topic 3: if, if-else, Ladder & Modern Switch
│   ├── LoopsDemo.java                               # Topic 4: for, while, do-while & Jump Statements
│   ├── ArraysDemo.java                              # Topic 5: 1D & 2D Arrays, Stats & Search Algorithms
│   ├── ExceptionHandlingDemo.java                   # Topic 6: try-catch, finally, throw, throws & Custom Exception
│   └── StudentGradeManagerApp.java                  # Integrated Capstone Application (All Topics Combined)
├── bin/                                             # Compiled Bytecode (.class files)
├── build_report.py                                  # Automated Python generator for Word Documentation
├── Task1_Java_Syntax_and_Basic_Operations.docx       # Root copy of the Executive Word Report
└── README.md                                        # Project Documentation & Terminal Run Guide
```

---

## 🛠️ Prerequisites & Verification

Check that Java is installed and available in your system `PATH`:

```powershell
java -version
javac -version
```

**Verified Test Environment Output:**
```text
java version "22.0.1" 2024-04-16
Java(TM) SE Runtime Environment (build 22.0.1+8-16)
Java HotSpot(TM) 64-Bit Server VM (build 22.0.1+8-16, mixed mode, sharing)
javac 22.0.1
```

---

## 🚀 How to Compile and Run the Java Programs

Open PowerShell or Command Prompt inside the `YuvaIntern_Java_Development` directory.

### Step 1: Compile All Java Files into `bin/` Directory

```powershell
# Create the bin output folder if it doesn't already exist
mkdir bin -Force

# Compile all source files in the src/ folder
javac -d bin src/*.java
```

*(This compiles all 7 source files with zero errors and zero warnings).*

---

### Step 2: Run Individual Topic Demonstrations

#### 1. Topic 1: Variables and Data Types
Demonstrates the 8 primitive data types, memory sizes, String reference immutability, widening/narrowing casting, and byte overflow.
```powershell
java -cp bin VariablesAndDataTypesDemo
```

#### 2. Topic 2: Operators in Java
Demonstrates arithmetic operations, relational checks, short-circuit logical evaluation (`&&`, `||`), unary pre/post-increment subtleties, ternary expressions, bitwise manipulations, and e-commerce cart calculation.
```powershell
java -cp bin OperatorsDemo
```

#### 3. Topic 3: Conditional Statements
Demonstrates simple `if`, `if-else`, `if-else-if` ladder, nested `if`, classic `switch` with fallthrough grouping, modern Java arrow switch expressions, and a credit-scoring loan approval engine.
```powershell
java -cp bin ConditionalsDemo
```

#### 4. Topic 4: Loops and Iterations
Demonstrates counter-controlled `for` loops, entry-controlled `while` loops, exit-controlled `do-while` loops (guaranteed 1 run), enhanced `for-each` loops, `break`/`continue` jump statements, and an automated low-stock inventory monitor.
```powershell
java -cp bin LoopsDemo
```

#### 5. Topic 5: Arrays in Java
Demonstrates 1D array creation, linear traversal, calculation of sum, average, min, and max, $O(N)$ linear search, 2D matrix manipulation, and subject-wise classroom averages.
```powershell
java -cp bin ArraysDemo
```

#### 6. Topic 6: Error Handling and Exceptions
Demonstrates the `Throwable` hierarchy, handling `ArithmeticException` (division by zero) and `ArrayIndexOutOfBoundsException`, multiple catch ordering rules, `finally` cleanup block guarantees, `throw` vs `throws`, and a custom `InsufficientBalanceException` in an ATM withdrawal engine.
```powershell
java -cp bin ExceptionHandlingDemo
```

---

### Step 3: Run the Integrated Capstone Demonstration

The capstone application brings all Week 1 concepts into an integrated, production-style student grade management and scholarship eligibility engine:

```powershell
java -cp bin StudentGradeManagerApp
```

**Key Systems Combined in the Capstone:**
1. **Variables & Types:** Primitive counters, double GPAs, character grades, and String arrays.
2. **Operators:** Arithmetic totals, relational checks, and ternary pass/fail evaluation.
3. **Conditionals:** Letter grade classification ladder and modern switch routing.
4. **Loops:** Iterating students and subjects via nested for, while, and do-while loops.
5. **Arrays:** Parallel 1D metadata arrays and a 2D matrix of subject marks.
6. **Exception Handling:** Custom `InvalidMarkException` auditing all mark inputs before calculations run.

---

## 📊 Summary of Concepts & Placement Insights

| Topic | Java Source File | Core Placement Concept | Practical Placement Use Case |
|---|---|---|---|
| **Variables & Types** | `VariablesAndDataTypesDemo.java` | Stack vs Heap, Implicit vs Explicit casting, Byte overflow wrap-around (`(byte)130 -> -126`) | Employee Payroll & Tax Calculator |
| **Operators** | `OperatorsDemo.java` | Short-circuit evaluation (`&&`, `\|\|`), Pre vs Post increment (`(a++) + (++b) + (++a)`), Bitwise shifts | E-Commerce Cart & Discount Engine |
| **Conditionals** | `ConditionalsDemo.java` | `if-else-if` hierarchy, switch fallthrough mechanics, enhanced switch expressions | Bank Loan Eligibility & Credit Risk Engine |
| **Loops** | `LoopsDemo.java` | Entry vs Exit controlled loops, do-while single execution guarantee, break vs continue | Inventory Low-Stock Monitor & Alert Engine |
| **Arrays** | `ArraysDemo.java` | Contiguous memory allocation, $O(1)$ indexed access, 2D matrix traversal, Linear search | Multi-Subject Class Grade Matrix & Topper Tracker |
| **Exception Handling** | `ExceptionHandlingDemo.java` | Checked vs Unchecked exceptions, Catch block hierarchy order, Finally block guarantee, Custom Exceptions | Bank ATM Cash Withdrawal & Validation System |
| **Integrated Capstone** | `StudentGradeManagerApp.java` | Synthesis of all 6 core syntax modules into a cohesive production console application | Student Academic & Placement Performance Manager |

---

## 📄 Formal Documentation Report

A Word report named **`Task1_Java_Syntax_and_Basic_Operations.docx`** has been compiled and placed inside the `docs/` folder (and in the project root). It contains:
- **Title Page & Executive Metadata:** Formally aligned with YuvaIntern criteria.
- **Project Objectives & Scope:** Academic and placement motivations.
- **Java Syntax & Architectural Foundations:** JDK vs JRE vs JVM comparison and bytecode execution pipeline.
- **All 6 Topic Analyses:** Definitions, memory models, detailed code snippets, verified verbatim terminal outputs, and business use cases.
- **Integrated Capstone Documentation:** Complete architectural walkthrough and verified output.
- **Verification & Test Matrix:** 100% genuine compilation and execution records (Exit Code: 0).
- **Placement Readiness Cheatsheet:** Top 8 high-frequency Java syntax interview traps asked by tier-1 tech recruiters.
- **Conclusion & Citations:** Formal references including the Oracle Java SE 22 Language Specification.

To regenerate the documentation report at any time:
```powershell
python build_report.py
```

---

## 👨‍💻 Author Information
- **Internship Program:** YuvaIntern Virtual Internship – Engineer (Java Development)
- **Milestone:** Week 1 Task – Java Syntax and Basic Operations
- **Verification Status:** Verified Locally (7 of 7 Programs Compiled and Tested Successfully)
