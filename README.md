# YuvaIntern Engineer – Java Development Virtual Internship

**Repository Owner:** Akunuri Ramu  
**GitHub Repository:** [https://github.com/akunuriramu/yuva-java-development-internship](https://github.com/akunuriramu/yuva-java-development-internship)  
**Target Environment:** Java SE 22+ (Verified on `Java 22.0.1 64-Bit HotSpot VM`)  
**Operating System:** Windows 11 (PowerShell & CMD compatible)  

---

## 📌 Project Overview

This repository houses the complete, production-grade technical deliverables for the **YuvaIntern Virtual Internship – Engineer (Java Development)**. Designed to meet both academic rigor and campus placement recruitment standards, every module bridges core computer science theory with compilable, benchmarked Java source code and executive documentation.

### Completed Internship Milestones:
- **Week 1:** Java Syntax, Basic Operations, Control Structures, Arrays & Exception Handling
- **Week 2:** Object-Oriented Programming (OOP) in Java – 4 Pillars, Memory Dynamics & Autonomous Fleet Capstone
- **Week 3:** Data Structures in Java – From-Scratch Implementations, Complexity Analysis & E-Commerce Fulfillment Capstone

---

## 📁 Repository Directory Structure

```text
YuvaIntern_Java_Development/
├── docs/
│   ├── Task1_Java_Syntax_and_Basic_Operations.docx         # Week 1 Executive Word Report
│   ├── Task2_Object_Oriented_Programming_Java.docx         # Week 2 Executive Word Report
│   └── Task3_Data_Structures_in_Java.docx                  # Week 3 Executive Word Report
├── src/
│   ├── VariablesAndDataTypesDemo.java                     # Week 1: Primitives, Reference Types & Casting
│   ├── OperatorsDemo.java                                 # Week 1: Arithmetic, Logical, Unary & Bitwise
│   ├── ConditionalsDemo.java                              # Week 1: if, if-else, Ladder & Modern Switch
│   ├── LoopsDemo.java                                     # Week 1: for, while, do-while & Jump Statements
│   ├── ArraysDemo.java                                    # Week 1: 1D & 2D Arrays, Stats & Search Algorithms
│   ├── ExceptionHandlingDemo.java                         # Week 1: try-catch, finally, throw, throws & Custom Exception
│   ├── StudentGradeManagerApp.java                        # Week 1 Capstone: Student Performance System
│   ├── week2/                                             # WEEK 2: OBJECT-ORIENTED PROGRAMMING
│   │   ├── ClassesAndObjectsDemo.java                     # Topic 1: Blueprints, Heap Objects & Memory Lifecycle
│   │   ├── EncapsulationDemo.java                         # Topic 2: Data Hiding, Validation & Immutability
│   │   ├── InheritanceDemo.java                           # Topic 3: IS-A, Chaining & Diamond Problem
│   │   ├── PolymorphismDemo.java                          # Topic 4: Dynamic Dispatch & Covariant Returns
│   │   ├── AbstractionDemo.java                           # Topic 5: Abstract Classes, Templates & Interfaces
│   │   ├── ImportanceOfOOPDemo.java                       # Topic 6: Procedural vs. OOP Architecture Comparison
│   │   └── EnterpriseFleetManagementApp.java              # Week 2 Capstone: Autonomous & Electric Fleet System
│   └── week3/                                             # WEEK 3: DATA STRUCTURES IN JAVA
│       ├── ArraysDSDemo.java                              # Module 1: Dynamic Arrays, Two-Pointer, Kadane & Sliding Window
│       ├── LinkedListDSDemo.java                          # Module 2: Singly & Doubly Linked Lists + Floyd's Cycle Detection
│       ├── StackDSDemo.java                               # Module 3: Array & Linked Stacks + Delimiter Validator
│       ├── QueueDSDemo.java                               # Module 4: Circular Ring Buffer & Linked Queue + Dispatch Engine
│       ├── TreeDSDemo.java                                # Module 5: Binary Search Tree, DFS Traversals & Range Queries
│       ├── HashMapDSDemo.java                             # Module 6: Custom HashMap, Separate Chaining & Rehashing
│       └── ECommerceInventoryOrderEngine.java             # Week 3 Capstone: E-Commerce Fulfillment & Inventory Engine
├── bin/                                                   # Compiled Bytecode (.class files, gitignored)
├── build_report.py                                        # Week 1 Python docx Report Generator
├── build_week2_report.py                                  # Week 2 Python docx Report Generator
├── build_week3_report.py                                  # Week 3 Python docx Report Generator
├── Task1_Java_Syntax_and_Basic_Operations.docx             # Root copy of Week 1 Report
├── Task2_Object_Oriented_Programming_Java.docx             # Root copy of Week 2 Report
├── Task3_Data_Structures_in_Java.docx                      # Root copy of Week 3 Report
├── .gitignore                                             # Excludes compiled binaries and IDE cache
└── README.md                                              # Master Technical Documentation & Execution Guide
```

---

## 🛠️ Prerequisites & Verification

Ensure that the Java Development Kit (JDK 22+) is configured on your system `PATH`:

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

# 🚀 WEEK 3: DATA STRUCTURES IN JAVA

### Step 1: Compile All Week 3 Modules

```powershell
# Create bin directory if needed
mkdir bin -Force

# Compile all Week 3 source files
javac -d bin src/week3/*.java
```

*(Compiles 7 source files with zero errors and zero warnings).*

---

### Step 2: Run Individual Week 3 Demonstrations

```powershell
java -cp bin week3.ArraysDSDemo
java -cp bin week3.LinkedListDSDemo
java -cp bin week3.StackDSDemo
java -cp bin week3.QueueDSDemo
java -cp bin week3.TreeDSDemo
java -cp bin week3.HashMapDSDemo
```

### Step 3: Run the Week 3 Capstone Application

```powershell
java -cp bin week3.ECommerceInventoryOrderEngine
```

**Scenario:** High-Throughput E-Commerce Fulfillment & Inventory Engine
- **Fixed Array:** Contiguous warehouse batch inventory auditing and valuation.
- **Custom HashMap:** `O(1)` SKU catalog lookup with separate chaining and dynamic rehashing.
- **Binary Search Tree:** Price-indexed catalog supporting sorted traversal and range queries (`$100 - $260`).
- **Circular Queue:** Strict FIFO customer order fulfillment pipeline.
- **Linked Stack:** Transactional LIFO undo/rollback history for inventory and pricing mutations.
- **Singly Linked List:** Dynamic shipment transit checkpoints with mid-route customs reroute splicing.

## 📊 Week 3 Data Structures & Complexity Summary

| Data Structure | Java Source File | Core Concepts Demonstrated | Practical Enterprise Use Case |
|---|---|---|---|
| **Arrays & Dynamic Arrays** | `ArraysDSDemo.java` | Contiguous Heap layout, 2x geometric resize, Two-Pointer reversal, Kadane & Sliding Window | Financial P&L & API Telemetry Analytics |
| **Singly & Doubly Linked Lists** | `LinkedListDSDemo.java` | Heap node pointers, head/tail/k-th splicing, Floyd's Tortoise & Hare cycle detection | Distributed Pipeline & Navigation History |
| **Stacks (LIFO)** | `StackDSDemo.java` | Dynamic `ArrayStack`, `LinkedStack`, reference loitering cleanup | API Gateway JSON / Delimiter Validator |
| **Queues (FIFO)** | `QueueDSDemo.java` | Circular Ring Buffer (`% capacity`), `LinkedQueue` head/tail pointers | Customer Support Ticket Dispatch Engine |
| **Binary Search Trees** | `TreeDSDemo.java` | Recursive insert/search, DFS traversals, height, range query, global-bound BST validation | Ordered Price Indexing & Range Search |
| **HashMaps** | `HashMapDSDemo.java` | Bit-mixing hash, separate chaining (`FB`/`Ea` collision test), 0.75 load factor rehashing | Distributed Session & Token Cache |
| **Integrated Capstone** | `ECommerceInventoryOrderEngine.java` | Full synthesis of all 6 from-scratch data structures with tabular `printf` telemetry | High-Throughput E-Commerce Fulfillment |

---

# 🚀 WEEK 2: OBJECT-ORIENTED PROGRAMMING IN JAVA

### Step 1: Compile All Week 2 Modules

```powershell
# Create bin directory if needed
mkdir bin -Force

# Compile all Week 2 source files
javac -d bin src/week2/*.java
```

*(Compiles 7 source files with zero errors and zero warnings).*

---

### Step 2: Run Individual Week 2 Demonstrations

#### 1. Classes, Objects, Constructors & Memory Dynamics
Demonstrates class blueprints vs. heap object allocation, constructor chaining with `this(...)`, the copy constructor pattern, and garbage collection root reachability (nullification, reassignment, island of isolation).
```powershell
java -cp bin week2.ClassesAndObjectsDemo
```

#### 2. Encapsulation, Data Integrity & Immutability
Demonstrates private fields, accessor/mutator validation filters, and production-grade immutable class design with defensive copying of mutable objects (`Date`, `List`).
```powershell
java -cp bin week2.EncapsulationDemo
```

#### 3. Inheritance, Constructor Chaining & The Diamond Problem
Demonstrates single, multilevel, and hierarchical inheritance, parent-to-child constructor chaining execution order, `super()` / `super.method()`, and why Java forbids multiple class inheritance.
```powershell
java -cp bin week2.InheritanceDemo
```

#### 4. Polymorphism, Dynamic Dispatch & Covariant Returns
Demonstrates compile-time method overloading with automatic type promotion, runtime method overriding via Dynamic Method Dispatch (`vtable` resolution), covariant return types, and static method hiding traps.
```powershell
java -cp bin week2.PolymorphismDemo
```

#### 5. Abstraction, Template Method Pattern & Interfaces
Demonstrates abstract classes with state retention, the Template Method design pattern, and interfaces with `default`, `static`, and `private` methods alongside an enterprise architectural decision matrix.
```powershell
java -cp bin week2.AbstractionDemo
```

#### 6. Importance of OOP: Procedural Spaghetti vs. Modular OOP
Presents a side-by-side architectural contrast evaluating maintainability, extensibility (Open/Closed Principle), code reuse, and unit testability between procedural parallel arrays and modular OOP classes.
```powershell
java -cp bin week2.ImportanceOfOOPDemo
```

---

### Step 3: Run the Week 2 Capstone Application

```powershell
java -cp bin week2.EnterpriseFleetManagementApp
```

**Scenario:** Multi-Tier Electric & Autonomous Logistics Fleet System
- **Encapsulation:** Abstract `Vehicle` with private fields (`vehicleId`, `vin`, `model`, `currentBatteryLevel`), immutable 17-character VIN, and battery validation guards.
- **Abstraction:** `AutonomousNavigable` (LiDAR, GPS routing) and `Rechargeable` (grid DC fast charging) capability interfaces.
- **Inheritance:** `ElectricTruck`, `DeliveryVan`, and `AutonomousDrone` specializing payload dynamics and consumption curves.
- **Polymorphism:** Uniform `List<Vehicle>` executing Dynamic Method Dispatch across overridden range calculations, mission dispatch, and maintenance projections.
- **Placement Edge Cases:** Java 16+ Pattern Matching for `instanceof`, `final` audit logging, and Deep vs. Shallow object cloning with nested `Waypoint` instances.

---

## 📊 Week 2 Concept & Placement Summary

| Topic | Java Source File | Core Architectural Concept | Practical Industry Use Case |
|---|---|---|---|
| **Classes & Objects** | `ClassesAndObjectsDemo.java` | Stack reference vs. Heap instance, copy constructor, GC reachability & Island of Isolation | Cloud Virtual Machine Provisioning |
| **Encapsulation** | `EncapsulationDemo.java` | Private data hiding, validation filters, immutable class rules & defensive copies | Banking KYC & Immutable Audit Records |
| **Inheritance** | `InheritanceDemo.java` | Single/multilevel/hierarchical inheritance, constructor chaining order, Diamond Problem | Enterprise Engineering Staff Hierarchy |
| **Polymorphism** | `PolymorphismDemo.java` | Overloading type promotion, Dynamic Dispatch via vtables, Covariant Returns, Static method hiding | Multi-Channel Payment Processing Gateway |
| **Abstraction** | `AbstractionDemo.java` | Abstract classes, Template Method Pattern, Java 8/9 default & private interface methods | Cloud Object Storage & Security Provider |
| **Importance of OOP** | `ImportanceOfOOPDemo.java` | Procedural parallel arrays vs. OOP domain entities (Maintainability, Extensibility, Testability) | Banking Loan Underwriting & Risk Engine |
| **Integrated Capstone** | `EnterpriseFleetManagementApp.java` | Full synthesis of the 4 pillars, Java 16 pattern matching, deep cloning, and tabular telemetry | Autonomous & Electric Logistics Fleet System |

---

# 🚀 WEEK 1: JAVA SYNTAX & BASIC OPERATIONS

### Compile and Run Week 1 Modules

```powershell
# Compile Week 1 source files
javac -d bin src/*.java

# Run individual demonstrations
java -cp bin VariablesAndDataTypesDemo
java -cp bin OperatorsDemo
java -cp bin ConditionalsDemo
java -cp bin LoopsDemo
java -cp bin ArraysDemo
java -cp bin ExceptionHandlingDemo

# Run Week 1 Capstone
java -cp bin StudentGradeManagerApp
```

---

## 📄 Formal Documentation Reports

Three executive Word documents are available in the repository:
1. **Week 1 Report:** [docs/Task1_Java_Syntax_and_Basic_Operations.docx](docs/Task1_Java_Syntax_and_Basic_Operations.docx) (or root mirror: `Task1_Java_Syntax_and_Basic_Operations.docx`).
2. **Week 2 Report:** [docs/Task2_Object_Oriented_Programming_Java.docx](docs/Task2_Object_Oriented_Programming_Java.docx) (or root mirror: `Task2_Object_Oriented_Programming_Java.docx`).
3. **Week 3 Report:** [docs/Task3_Data_Structures_in_Java.docx](docs/Task3_Data_Structures_in_Java.docx) (or root mirror: `Task3_Data_Structures_in_Java.docx`).

### To Regenerate Documentation Reports via Python:
```powershell
# Regenerate Week 1 Report
python build_report.py

# Regenerate Week 2 Report
python build_week2_report.py

# Regenerate Week 3 Report
python build_week3_report.py
```

---

## 👨‍💻 Author & Internship Information
- **Intern:** Akunuri Ramu
- **Track:** Java Software Development Virtual Internship
- **Organization:** YuvaIntern
- **Verification Status:** 100% Tested Locally on Oracle JDK 22 (All 21 Programs Passed with Exit Code 0)
