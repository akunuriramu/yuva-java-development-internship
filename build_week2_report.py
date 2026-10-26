import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

def create_document():
    doc = docx.Document()

    # 1. Page Margins Setup (1.0 inch on all sides)
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        section.page_width = Inches(8.5)
        section.page_height = Inches(11.0)

    # 2. Executive Corporate Palette Constants
    COLOR_PRIMARY_NAVY = RGBColor(27, 54, 93)      # #1B365D - Deep Corporate Navy
    COLOR_SECONDARY_BLUE = RGBColor(46, 91, 136)   # #2E5B88 - Slate Blue
    COLOR_ACCENT_TEAL = RGBColor(14, 116, 144)     # #0E7490 - Modern Accent
    COLOR_CHARCOAL = RGBColor(34, 34, 34)          # #222222 - Crisp readable body
    COLOR_MUTED = RGBColor(100, 116, 139)          # #64748B - Muted gray
    HEX_HEADER_BG = "1B365D"
    HEX_LIGHT_ROW = "F8FAFC"
    HEX_CODE_BG = "F1F5F9"
    HEX_CONSOLE_BG = "0F172A"                       # Dark slate terminal
    HEX_BORDER = "CBD5E1"

    # Default typography
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Calibri'
    normal_style.font.size = Pt(11)
    normal_style.font.color.rgb = COLOR_CHARCOAL
    normal_style.paragraph_format.line_spacing = 1.15
    normal_style.paragraph_format.space_after = Pt(6)

    def set_cell_background(cell, hex_color):
        shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
        cell._tc.get_or_add_tcPr().append(shading)

    def set_cell_margins(cell, top=120, bottom=120, left=160, right=160):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def set_table_borders(table, color="CBD5E1", sz="4"):
        tblPr = table._tbl.tblPr
        borders = parse_xml(
            f'<w:tblBorders {nsdecls("w")}>'
            f'<w:top w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>'
            f'<w:bottom w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>'
            f'<w:left w:val="none"/>'
            f'<w:right w:val="none"/>'
            f'<w:insideH w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>'
            f'<w:insideV w:val="none"/>'
            f'</w:tblBorders>'
        )
        tblPr.append(borders)

    def add_custom_heading(text, level):
        h = doc.add_paragraph()
        run = h.add_run(text)
        run.bold = True
        run.font.name = 'Calibri'
        if level == 1:
            h.paragraph_format.space_before = Pt(16)
            h.paragraph_format.space_after = Pt(8)
            run.font.size = Pt(18)
            run.font.color.rgb = COLOR_PRIMARY_NAVY
        elif level == 2:
            h.paragraph_format.space_before = Pt(12)
            h.paragraph_format.space_after = Pt(6)
            run.font.size = Pt(14)
            run.font.color.rgb = COLOR_SECONDARY_BLUE
        elif level == 3:
            h.paragraph_format.space_before = Pt(8)
            h.paragraph_format.space_after = Pt(4)
            run.font.size = Pt(12)
            run.font.color.rgb = COLOR_ACCENT_TEAL
        return h

    def add_callout(text, label="PRINCIPAL ARCHITECT INSIGHT"):
        table = doc.add_table(rows=1, cols=1)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        table.columns[0].width = Inches(6.5)
        cell = table.cell(0, 0)
        set_cell_background(cell, "EFF6FF")
        set_cell_margins(cell, top=140, bottom=140, left=200, right=200)

        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:left w:val="single" w:sz="24" w:space="0" w:color="2563EB"/><w:top w:val="none"/><w:right w:val="none"/><w:bottom w:val="none"/></w:tcBorders>')
        tcPr.append(borders)

        p = cell.paragraphs[0]
        p.paragraph_format.line_spacing = 1.15
        p.paragraph_format.space_after = Pt(2)
        r_lbl = p.add_run(f"[{label}] ")
        r_lbl.bold = True
        r_lbl.font.size = Pt(10)
        r_lbl.font.color.rgb = RGBColor(37, 99, 235)

        r_txt = p.add_run(text)
        r_txt.font.size = Pt(10.5)
        r_txt.font.color.rgb = RGBColor(30, 41, 59)
        doc.add_paragraph()

    def add_code_block(code_text, filename=""):
        table = doc.add_table(rows=1, cols=1)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        table.columns[0].width = Inches(6.5)
        cell = table.cell(0, 0)
        set_cell_background(cell, HEX_CODE_BG)
        set_cell_margins(cell, top=140, bottom=140, left=180, right=180)

        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:left w:val="single" w:sz="16" w:space="0" w:color="0E7490"/><w:top w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/><w:right w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/></w:tcBorders>')
        tcPr.append(borders)

        p = cell.paragraphs[0]
        p.paragraph_format.line_spacing = 1.05
        p.paragraph_format.space_after = Pt(2)

        if filename:
            r_fn = p.add_run(f"// File: {filename}\n")
            r_fn.font.name = 'Consolas'
            r_fn.font.size = Pt(9)
            r_fn.bold = True
            r_fn.font.color.rgb = RGBColor(14, 116, 144)

        r_code = p.add_run(code_text)
        r_code.font.name = 'Consolas'
        r_code.font.size = Pt(9)
        r_code.font.color.rgb = RGBColor(30, 41, 59)
        doc.add_paragraph()

    def add_terminal_output(output_text, title="Verified Execution Output (Terminal)"):
        table = doc.add_table(rows=1, cols=1)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        table.columns[0].width = Inches(6.5)
        cell = table.cell(0, 0)
        set_cell_background(cell, HEX_CONSOLE_BG)
        set_cell_margins(cell, top=140, bottom=140, left=180, right=180)

        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:left w:val="single" w:sz="16" w:space="0" w:color="22C55E"/><w:top w:val="single" w:sz="4" w:space="0" w:color="334155"/><w:right w:val="single" w:sz="4" w:space="0" w:color="334155"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="334155"/></w:tcBorders>')
        tcPr.append(borders)

        p = cell.paragraphs[0]
        p.paragraph_format.line_spacing = 1.05
        p.paragraph_format.space_after = Pt(2)

        r_hdr = p.add_run(f"$ {title}\n")
        r_hdr.font.name = 'Consolas'
        r_hdr.font.size = Pt(8.5)
        r_hdr.bold = True
        r_hdr.font.color.rgb = RGBColor(34, 197, 94)

        r_out = p.add_run(output_text.strip())
        r_out.font.name = 'Consolas'
        r_out.font.size = Pt(8.5)
        r_out.font.color.rgb = RGBColor(226, 232, 240)
        doc.add_paragraph()

    # =========================================================================
    # 1. TITLE PAGE
    # =========================================================================
    p_org = doc.add_paragraph()
    p_org.paragraph_format.space_before = Pt(36)
    p_org.paragraph_format.space_after = Pt(6)
    r_org = p_org.add_run("YUVAINTERN ENGINEER VIRTUAL INTERNSHIP")
    r_org.font.size = Pt(13)
    r_org.bold = True
    r_org.font.color.rgb = COLOR_SECONDARY_BLUE

    p_track = doc.add_paragraph()
    p_track.paragraph_format.space_after = Pt(28)
    r_track = p_track.add_run("TRACK: JAVA SOFTWARE DEVELOPMENT | WEEK 2 TECHNICAL MILESTONE")
    r_track.font.size = Pt(11)
    r_track.bold = True
    r_track.font.color.rgb = COLOR_ACCENT_TEAL

    p_title = doc.add_paragraph()
    p_title.paragraph_format.space_after = Pt(10)
    r_title = p_title.add_run("Task 2: Object-Oriented Programming in Java")
    r_title.font.size = Pt(26)
    r_title.bold = True
    r_title.font.color.rgb = COLOR_PRIMARY_NAVY

    p_sub = doc.add_paragraph()
    p_sub.paragraph_format.space_after = Pt(36)
    r_sub = p_sub.add_run("Comprehensive Technical Dossier, Core OOP Pillars, Memory Lifecycle, Dynamic Method Dispatch, Capstone Enterprise System, and Placement Analysis")
    r_sub.font.size = Pt(13)
    r_sub.italic = True
    r_sub.font.color.rgb = COLOR_MUTED

    # Metadata Box Table
    meta_table = doc.add_table(rows=7, cols=2)
    meta_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    meta_table.columns[0].width = Inches(2.2)
    meta_table.columns[1].width = Inches(4.3)
    set_table_borders(meta_table, color="94A3B8", sz="6")

    meta_data = [
        ("Intern Role / Designation", "YuvaIntern Student Engineer – Java Development"),
        ("Project Milestone", "Week 2: Object-Oriented Programming (OOP) in Java"),
        ("GitHub Repository", "https://github.com/akunuriramu/yuva-java-development-internship"),
        ("Java Platform / JDK", "Java SE Development Kit (build 22.0.1+8-16, 64-Bit HotSpot VM)"),
        ("Development Environment", "Windows 11 x64, PowerShell, Antigravity IDE"),
        ("Verification Methodology", "100% Real Terminal Compilation & Execution (Zero Emulation)"),
        ("Architecture & Core Topics", "Classes & Objects, Encapsulation, Inheritance, Polymorphism, Abstraction, Importance of OOP, Enterprise Fleet Capstone")
    ]

    for row_idx, (lbl, val) in enumerate(meta_data):
        row = meta_table.rows[row_idx]
        c0, c1 = row.cells[0], row.cells[1]
        set_cell_background(c0, "F8FAFC")
        set_cell_background(c1, "FFFFFF")
        set_cell_margins(c0, top=100, bottom=100, left=140, right=140)
        set_cell_margins(c1, top=100, bottom=100, left=140, right=140)

        p0 = c0.paragraphs[0]
        p0.paragraph_format.space_after = Pt(2)
        r0 = p0.add_run(lbl)
        r0.bold = True
        r0.font.size = Pt(10)
        r0.font.color.rgb = COLOR_PRIMARY_NAVY

        p1 = c1.paragraphs[0]
        p1.paragraph_format.space_after = Pt(2)
        r1 = p1.add_run(val)
        r1.font.size = Pt(10)
        r1.font.color.rgb = COLOR_CHARCOAL

    doc.add_page_break()

    # =========================================================================
    # 2. EXECUTIVE OBJECTIVE & SCOPE
    # =========================================================================
    add_custom_heading("1. Executive Objective and Project Scope", level=1)
    
    doc.add_paragraph(
        "The primary objective of the Week 2 milestone in the YuvaIntern Java Development Virtual Internship "
        "is to attain complete, production-grade mastery over the Object-Oriented Programming (OOP) paradigm in Java. "
        "Modern enterprise software engineering—encompassing frameworks like Spring Boot, Hibernate, Apache Kafka, and cloud-native microservices—"
        "relies fundamentally on the four pillars of OOP: Encapsulation, Inheritance, Polymorphism, and Abstraction. "
        "This project bridges academic definitions with the architectural realities of JVM memory management, virtual method dispatch tables (vtables), "
        "immutable data contracts, and design patterns."
    )

    doc.add_paragraph(
        "Key Deliverables Achieved in this Milestone:\n"
        "• Implementation of six standalone, compilable Java source modules covering each core OOP concept.\n"
        "• Development of an end-to-end integrated Capstone Program (Enterprise Autonomous & Electric Logistics Fleet System).\n"
        "• In-depth architectural analysis comparing procedural programming with object-oriented paradigms.\n"
        "• Deep-dive into memory mechanics: Stack references, Heap object payloads, Metaspace class metadata, and Garbage Collection (GC) reachability.\n"
        "• 100% genuine compilation and execution verification on Java SE 22 (javac 22.0.1) under Windows 11."
    )

    add_callout(
        "Top-tier software engineering interviews (at firms like Google, Amazon, Microsoft, and Oracle) test whether "
        "candidates understand what happens under the hood: why Java forbids multiple class inheritance, how dynamic method dispatch "
        "uses vtables, how to construct thread-safe immutable classes using defensive copying, and why copy constructors are preferred over clone(). "
        "This technical dossier provides definitive, verified answers to these placement-critical topics.",
        label="ARCHITECTURAL PLACEMENT FOCUS"
    )

    # =========================================================================
    # 3. ARCHITECTURAL DEEP-DIVE: THE 4 PILLARS & JVM MEMORY
    # =========================================================================
    add_custom_heading("2. Architectural Deep-Dive: The 4 Pillars of OOP & Memory Dynamics", level=1)

    doc.add_paragraph(
        "Object-Oriented Programming is not merely a syntactic style; it is an organizational paradigm that models real-world domain entities "
        "as software objects containing state (data fields) and behavior (methods). The four core pillars work synchronously:"
    )

    pillars_table = doc.add_table(rows=5, cols=3)
    pillars_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    pillars_table.columns[0].width = Inches(1.3)
    pillars_table.columns[1].width = Inches(2.2)
    pillars_table.columns[2].width = Inches(3.0)
    set_table_borders(pillars_table)

    headers = ["Pillar", "Core Architectural Concept", "Enterprise Benefit & Production Role"]
    for i, h in enumerate(headers):
        cell = pillars_table.rows[0].cells[i]
        set_cell_background(cell, HEX_HEADER_BG)
        set_cell_margins(cell, top=100, bottom=100, left=120, right=120)
        p = cell.paragraphs[0]
        r = p.add_run(h)
        r.bold = True
        r.font.size = Pt(10)
        r.font.color.rgb = RGBColor(255, 255, 255)

    pillars_rows = [
        ("Encapsulation", "Information hiding via private fields and validated accessor/mutator methods; immutable contracts.", "Protects business invariants, prevents invalid state transitions, and isolates changes within class boundaries."),
        ("Inheritance", "IS-A hierarchy enabling code reuse, constructor chaining, and specialization of ancestor behavior.", "Eliminates redundant boilerplate code, models domain hierarchies, and establishes polymorphic base types."),
        ("Polymorphism", "Compile-time (overloading) and runtime (overriding via Dynamic Method Dispatch and vtables).", "Enables loose coupling and Open/Closed Principle compliance: clients invoke base contracts while behavior varies at runtime."),
        ("Abstraction", "Contractual interfaces and partial abstract classes (Template Method Pattern).", "Hides implementation complexities behind standardized APIs; decouples system components for modular testing and scaling.")
    ]

    for row_idx, data in enumerate(pillars_rows, start=1):
        row = pillars_table.rows[row_idx]
        bg = HEX_LIGHT_ROW if row_idx % 2 == 1 else "FFFFFF"
        for col_idx, text in enumerate(data):
            cell = row.cells[col_idx]
            set_cell_background(cell, bg)
            set_cell_margins(cell, top=90, bottom=90, left=120, right=120)
            p = cell.paragraphs[0]
            p.paragraph_format.space_after = Pt(2)
            r = p.add_run(text)
            r.font.size = Pt(9.5)
            if col_idx == 0:
                r.bold = True
                r.font.color.rgb = COLOR_PRIMARY_NAVY

    doc.add_paragraph()

    add_custom_heading("2.1 JVM Memory Architecture during OOP Execution", level=2)
    doc.add_paragraph(
        "To understand OOP execution in Java, one must trace how the JVM allocates and manages memory across three primary areas:\n"
        "1. Thread Stack: Stores primitive local variables and object references (memory addresses). Each thread has its own private stack. "
        "When a method is invoked, a new stack frame is pushed; when the method returns, the frame is popped.\n"
        "2. JVM Heap: The shared memory pool where all object instances and arrays reside. An object in the Heap consists of an Object Header "
        "(Mark Word for locking/hashcode and Klass Word pointing to class metadata) followed by instance field values.\n"
        "3. Metaspace (Native Memory): Stores class definitions, method bytecode, static variables, and Virtual Method Tables (vtables). "
        "Replaced the old PermGen in Java 8.\n"
        "4. Virtual Method Table (vtable): A lookup table created by the JVM for every class with virtual methods. When a polymorphic method call "
        "like vehicle.dispatchMission() occurs, the JVM reads the object's vtable pointer on the Heap and dispatches to the concrete method address in O(1) time."
    )

    # =========================================================================
    # 4. TOPIC 1: CLASSES AND OBJECTS
    # =========================================================================
    add_custom_heading("3. Topic 1: Classes, Objects, Constructors, and Memory Lifecycle", level=1)
    
    doc.add_paragraph(
        "A Class is a compile-time blueprint defining the structure and behavior of a domain concept, while an Object is a physical "
        "runtime instance dynamically allocated on the JVM Heap. The reference variable on the Stack merely holds the 64-bit memory address "
        "pointing to the Heap object."
    )

    doc.add_paragraph(
        "Key Engineering Mechanics Demonstrated:\n"
        "• Default vs Parameterized Constructors: Parameterized constructors guarantee that an object is instantiated in a valid state.\n"
        "• Constructor Chaining via 'this(...)': Allows multiple constructors to delegate to a master constructor, centralizing initialization.\n"
        "• Copy Constructor Pattern: The industry-preferred alternative to Java's flawed Object.clone() mechanism, providing clean, explicit object cloning.\n"
        "• Garbage Collection (GC) Eligibility: An object becomes eligible for GC the instant it is no longer reachable from any active GC Root "
        "(via reference nullification, reassignment, or the famous 'Island of Isolation' where objects only point to each other)."
    )

    add_custom_heading("3.1 Source Code Implementation (ClassesAndObjectsDemo.java)", level=2)
    with open("src/week2/ClassesAndObjectsDemo.java", "r") as f:
        code_t1 = f.read()
    add_code_block(code_t1, filename="src/week2/ClassesAndObjectsDemo.java")

    add_custom_heading("3.2 Verified Terminal Output", level=2)
    out_t1 = """==========================================================================
  WEEK 2 - TOPIC 1: CLASSES, OBJECTS, CONSTRUCTORS & MEMORY DYNAMICS
==========================================================================

--- 1. Class Blueprint vs. Heap Object Instantiation ---
Memory Concept: 'CloudServerInstance server1' allocates a reference on Stack.
                'new CloudServerInstance(...)' allocates object memory on Heap.

  [Status] Server instance srv-prod-web01 is now RUNNING.

Creating server via No-Arg constructor (demonstrating constructor chaining):
  [Constructor Log] No-argument constructor invoked -> Delegated via this(...)

Creating server via Copy Constructor:
  [Constructor Log] Copy constructor invoked -> Cloned state from srv-prod-web01

Fleet Specifications Summary:
  +--------------------+------------------+---------+-------------+---------------+
  | Instance ID        | Workload Profile | Cores   | RAM Size    | Current State |
  +--------------------+------------------+---------+-------------+---------------+
  | srv-prod-web01     | compute-optimized |  8 vCPUs |  16.0 GB RAM | State: RUNNING |
  | srv-default-000    | general-compute  |  2 vCPUs |   4.0 GB RAM | State: STOPPED |
  | srv-prod-web01-replica | compute-optimized |  8 vCPUs |  16.0 GB RAM | State: STOPPED |
  +--------------------+------------------+---------+-------------+---------------+
  Total Server Objects Created in Metaspace Registry: 3

--- 2. Stack Reference Equality vs Heap Identity ---
server1 memory reference equals serverRef? (server1 == serverRef): true (Points to SAME Heap memory)
server1 memory reference equals server3?   (server1 == server3):   false (Independent Heap allocations)

--- 3. Garbage Collection (GC) Lifecycle & Root Reachability ---
Scenario A - Reference Nullification:
  tempServer allocated at Heap. Reachable from current Stack Frame.
  tempServer set to null -> Heap object 'srv-temp-999' is now ELIGIBLE for Garbage Collection.

Scenario B - Reference Reassignment:
  Initial: 'alpha' points to srv-alpha, 'beta' points to srv-beta.
  After 'alpha = beta' -> Original Heap object 'srv-alpha' is now ELIGIBLE for GC.

Scenario C - Island of Isolation:
  nodeA and nodeB cross-reference each other on Heap.
  Stack references severed (nodeA = null, nodeB = null).
  Result: Node A and Node B reference each other, but NEITHER is reachable from GC Root.
  Island of Isolation formed -> BOTH nodes are eligible for Garbage Collection!
  System.gc() requested to JVM (Non-blocking GC execution hint passed).

==========================================================================
  TOPIC 1 DEMONSTRATION COMPLETE: ZERO RUNTIME ANOMALIES
=========================================================================="""
    add_terminal_output(out_t1, title="java -cp bin week2.ClassesAndObjectsDemo")

    # =========================================================================
    # 5. TOPIC 2: ENCAPSULATION
    # =========================================================================
    add_custom_heading("4. Topic 2: Encapsulation, Data Integrity & Immutability", level=1)

    doc.add_paragraph(
        "Encapsulation is the bundling of data (fields) and methods that operate on that data into a single cohesive unit, "
        "while restricting direct access to internal state from outside the class boundary. This is achieved via private access modifiers "
        "and public accessor/mutator methods containing business invariant guards."
    )

    doc.add_paragraph(
        "Architectural Rules for Immutable Classes (Effective Java Item 17):\n"
        "1. Declare the class as final to prevent malicious subclassing and method overriding.\n"
        "2. Declare all fields as private and final to enforce immutability at the compiler level.\n"
        "3. Provide no mutator methods (no setters).\n"
        "4. Enforce Defensive Copying: If the class holds references to mutable objects (such as Date, arrays, or List), "
        "make defensive copies in both the constructor and the getter methods so callers cannot alter internal state through external references."
    )

    add_callout(
        "Why is Defensive Copying essential in Immutable Classes? If a class accepts a mutable java.util.Date or List in its constructor "
        "and simply assigns this.date = date, the caller retains the external reference. If the caller subsequently calls date.setTime(0), "
        "the 'immutable' class's internal state is mutated! Defensive copying allocates a new clone, severing external reference aliasing.",
        label="SECURITY DEFECT PREVENTION"
    )

    add_custom_heading("4.1 Source Code Implementation (EncapsulationDemo.java)", level=2)
    with open("src/week2/EncapsulationDemo.java", "r") as f:
        code_t2 = f.read()
    add_code_block(code_t2, filename="src/week2/EncapsulationDemo.java")

    add_custom_heading("4.2 Verified Terminal Output", level=2)
    out_t2 = """==========================================================================
  WEEK 2 - TOPIC 2: ENCAPSULATION, DATA INTEGRITY & IMMUTABILITY
==========================================================================

--- 1. Encapsulated Bank Account with Data Validation Filters ---
  | Account: SB-10928475  | Holder: Rohan Verma     | KYC: PENDING  | Balance: Rs.  25,000.00 |

Testing Validation Guards:
  [Deposit Failed] Deposit amount must be positive. Received: Rs. -500.0
  [Withdraw Restricted] High-value transaction requires VERIFIED KYC status.

Submitting KYC verification token:
  [KYC Compliance] Account SB-10928475 verified successfully.
  [Withdraw Success] Rs. 15,000.00 withdrawn. Updated Balance: Rs. 10,000.00
  [Withdraw Rejected] Insufficient funds! Requested: Rs. 20,000.00, Available: Rs. 10,000.00
  [Validation Rejected] New name must have at least 3 characters. Update ignored.
  [Update] Account holder updated from 'Rohan Verma' to 'Rohan V. Sharma'.

Final Encapsulated Account State:
  | Account: SB-10928475  | Holder: Rohan V. Sharma | KYC: VERIFIED | Balance: Rs.  10,000.00 |

--- 2. Immutable Class Design & Defensive Copying Verification ---
Initial Immutable Record:
  AuditRecord[TX=TXN-998877, Amount=Rs.4,500.00, Date=Mon Oct 26 12:32:19 IST 2026, Tags=[UPI-TRANSFER, MERCHANT-SETTLEMENT]]

[Attack 1] Mutating external 'txDate' reference (shifting to year 1970)...
External Date is now: Thu Jan 01 05:30:00 IST 1970
Immutable Record Date: Mon Oct 26 12:32:19 IST 2026 (Unchanged due to constructor defensive copy!)

[Attack 2] Adding malicious tag to external 'tags' list...
External tags list: [UPI-TRANSFER, MERCHANT-SETTLEMENT, MALICIOUS-INJECTED-TAG]
Immutable Record Tags: [UPI-TRANSFER, MERCHANT-SETTLEMENT] (Unchanged due to constructor defensive copy!)

[Attack 3] Attempting to mutate List returned from getter...
  [Caught Expected Exception]: Cannot mutate unmodifiable list returned by getter!

Final State of Immutable Audit Record (Completely Intact):
  AuditRecord[TX=TXN-998877, Amount=Rs.4,500.00, Date=Mon Oct 26 12:32:19 IST 2026, Tags=[UPI-TRANSFER, MERCHANT-SETTLEMENT]]

==========================================================================
  TOPIC 2 DEMONSTRATION COMPLETE: DATA INTEGRITY PRESERVED
=========================================================================="""
    add_terminal_output(out_t2, title="java -cp bin week2.EncapsulationDemo")

    # =========================================================================
    # 6. TOPIC 3: INHERITANCE
    # =========================================================================
    add_custom_heading("5. Topic 3: Inheritance, Constructor Chaining & The Diamond Problem", level=1)

    doc.add_paragraph(
        "Inheritance establishes an 'IS-A' relationship between an existing class (superclass) and a derived class (subclass). "
        "It fosters modular software design by centralizing common domain attributes and methods in ancestors while enabling "
        "subclasses to specialize behavior."
    )

    doc.add_paragraph(
        "Key Engineering Mechanics Demonstrated:\n"
        "• Single Inheritance: Subclass derives from exactly one superclass (SoftwareEngineer extends Employee).\n"
        "• Multilevel Inheritance: Subclass derives from an ancestor through an intermediate derived class (TechLead extends SoftwareEngineer extends Employee).\n"
        "• Hierarchical Inheritance: Multiple subclasses derive independently from a common parent (SoftwareEngineer and EngineeringManager both extend Employee).\n"
        "• Constructor Chaining Order: When an object is instantiated, the constructor of java.lang.Object executes first, followed by each descending superclass constructor in order, and finally the concrete child constructor.\n"
        "• 'super' Keyword: Used to invoke parent constructors (super(...)) and to call overridden parent methods (super.method()).\n"
        "• Why Java Forbids Multiple Class Inheritance (The Diamond Problem): If class D could extend both B and C (which both override method m() from A), "
        "the compiler cannot determine which method implementation D should inherit. Java solves this cleanly by restricting classes to single inheritance "
        "while allowing multiple interface implementation."
    )

    add_custom_heading("5.1 Source Code Implementation (InheritanceDemo.java)", level=2)
    with open("src/week2/InheritanceDemo.java", "r") as f:
        code_t3 = f.read()
    add_code_block(code_t3, filename="src/week2/InheritanceDemo.java")

    add_custom_heading("5.2 Verified Terminal Output", level=2)
    out_t3 = """==========================================================================
  WEEK 2 - TOPIC 3: INHERITANCE, CONSTRUCTOR CHAINING & DIAMOND PROBLEM
==========================================================================

--- 1. Constructor Execution Order in Multilevel Inheritance ---
Instantiating TechLead object (Notice parent-to-child execution order):
    -> [Constructor 1: Employee] Initializing base employee: Kavita Reddy
    -> [Constructor 2: SoftwareEngineer] Specializing technical stack: Java / Spring Boot
    -> [Constructor 3: TechLead] Configuring leadership team size: 8

TechLead Role Profile:
    [Base Employee] ID: TL-2045 | Name: Kavita Reddy | Base Salary: Rs. 120,000.00/mo
    |--> Primary Language: Java / Spring Boot | PRs Merged: 0 | Annual Comp: Rs. 1,815,000.00
         |--> Tech Lead Scope: Managing 8 Engineers | Leadership Allowance: Rs. 25,000.00/mo

--- 2. Single vs. Hierarchical Inheritance Demonstrations ---
Instantiating SoftwareEngineer (Single Inheritance):
    -> [Constructor 1: Employee] Initializing base employee: Arjun Nair
    -> [Constructor 2: SoftwareEngineer] Specializing technical stack: Java / Microservices
    [Activity] PR merged by Arjun Nair. Total PRs: 1
    [Activity] PR merged by Arjun Nair. Total PRs: 2
    [Base Employee] ID: SE-8831 | Name: Arjun Nair | Base Salary: Rs. 75,000.00/mo
    |--> Primary Language: Java / Microservices | PRs Merged: 2 | Annual Comp: Rs. 975,000.00

Instantiating EngineeringManager (Hierarchical Inheritance from Employee):
    -> [Constructor 1: Employee] Initializing base employee: Siddharth Rao
    -> [Constructor: EngineeringManager] Configuring department: Cloud Platform Engineering
    [Base Employee] ID: EM-1002 | Name: Siddharth Rao | Base Salary: Rs. 185,000.00/mo
    |--> Management Scope: Cloud Platform Engineering | Quarterly Budget: Rs. 45.00 Lakhs | Annual Comp: Rs. 2,775,000.00

--- 3. Placement Focus: The Diamond Problem in C++ vs. Java Solution ---
The Diamond Problem Topology:
             [Class A: performAction()]
             /                       \\
   [Class B: overrides action]   [Class C: overrides action]
             \\                       /
           [Class D: extends B, C ???]

Ambiguity Issue: If Class D called 'super.performAction()', should it invoke B or C?
Memory Issue   : If Class A had instance fields, Class D would inherit two copies of A's fields!
Java's Design Decision:
  1. Java strictly allows single inheritance for classes ('extends' exactly one superclass).
  2. Java permits multiple inheritance of TYPE and CONTRACT via Interfaces ('implements').
  3. For default method conflicts across multiple interfaces, Java forces the child class
     to explicitly override and disambiguate the method call using 'InterfaceName.super.method()'.

==========================================================================
  TOPIC 3 DEMONSTRATION COMPLETE: INHERITANCE VERIFIED
=========================================================================="""
    add_terminal_output(out_t3, title="java -cp bin week2.InheritanceDemo")

    # =========================================================================
    # 7. TOPIC 4: POLYMORPHISM
    # =========================================================================
    add_custom_heading("6. Topic 4: Polymorphism, Dynamic Method Dispatch & Covariant Returns", level=1)

    doc.add_paragraph(
        "Polymorphism ('many forms') empowers a single uniform interface or reference variable to invoke varying behaviors "
        "depending on the underlying concrete type. Java supports two fundamental forms of polymorphism:"
    )

    doc.add_paragraph(
        "1. Compile-Time Polymorphism (Static Binding / Method Overloading):\n"
        "• Occurs within a single class when multiple methods share the same name but possess different parameter lists (count, types, or order).\n"
        "• Return type alone DOES NOT differentiate overloaded methods; changing the return type without modifying parameters causes a compiler error.\n"
        "• Automatic Type Promotion: If an exact signature match is not found, the compiler widens primitive arguments (e.g. int -> double).\n\n"
        "2. Runtime Polymorphism (Dynamic Binding / Method Overriding & Dynamic Dispatch):\n"
        "• Occurs across an inheritance hierarchy when a subclass redefines an inherited virtual method using the exact same signature.\n"
        "• Dynamic Method Dispatch: The JVM resolves the method invocation at runtime by examining the actual object located on the Heap, "
        "rather than the reference type declared on the Thread Stack.\n"
        "• Covariant Return Types (Java 5+): An overriding subclass method may return a more specific subtype than the return type declared in the superclass.\n"
        "• Method Hiding Trap: Static methods cannot be overridden. Declaring a static method with the same signature in a subclass merely 'hides' "
        "the parent method; resolution is determined at compile-time by the reference type."
    )

    add_custom_heading("6.1 Source Code Implementation (PolymorphismDemo.java)", level=2)
    with open("src/week2/PolymorphismDemo.java", "r") as f:
        code_t4 = f.read()
    add_code_block(code_t4, filename="src/week2/PolymorphismDemo.java")

    add_custom_heading("6.2 Verified Terminal Output", level=2)
    out_t4 = """==========================================================================
  WEEK 2 - TOPIC 4: COMPILE-TIME & RUNTIME POLYMORPHISM ARCHITECTURE
==========================================================================

--- 1. Compile-Time Polymorphism (Method Overloading) ---
Initial Cart Subtotal: Rs. 5,000.00

  [Overload 1: (double, double)] Applying flat discount of Rs. 750.0
  [Overload 2: (double, int)] Applying integer percentage discount of 15%
  [Overload 3: (double, int, String)] Coupon 'FESTIVE2026' applied: 15% + Extra 5% Loyalty Bonus
Results -> Flat: Rs. 4,250.00 | Percent: Rs. 4,250.00 | Coupon: Rs. 4,000.00

Automatic Type Promotion Demonstration:
  [Type Promotion Target (double)] Invoked with double value: 250.0

--- 2. Runtime Polymorphism: Dynamic Method Dispatch ---
Polymorphic Array: Superclass reference array holding heterogeneous subclasses:

Executing via Reference Type 'PaymentGateway':
  [Base Gateway] Processing generic settlement of Rs. 12,000.00 via Standard Wire Gateway
  Generated Receipt -> Standard Receipt #GEN-3106 for Rs. 12,000.00

Executing via Reference Type 'PaymentGateway':
  [CreditCard Gateway] Amount: Rs. 12,000.00 + Fee (2.5%): Rs. 300.00 -> Total Debited: Rs. 12,300.00
  Generated Receipt -> Standard Receipt #CC-3113 for Rs. 12,300.00

Executing via Reference Type 'PaymentGateway':
  [UPI Gateway] Instant debit from VPA: merchant@okaxis for Rs. 12,000.00 (Zero Merchant Fee)
  Generated Receipt -> Digital UPI Receipt #UPI-3114 [UPI-Ref: UPI8843023953] for Rs. 12,000.00

--- 3. Covariant Return Type Verification ---
  [UPI Gateway] Instant debit from VPA: student@okhdfcbank for Rs. 3,500.00 (Zero Merchant Fee)
Specific Subclass Receipt: Digital UPI Receipt #UPI-3115 [UPI-Ref: UPI1146671882] for Rs. 3,500.00

--- 4. Placement Trap: Method Hiding vs. Method Overriding ---
Calling overridden instance method on 'polyRef' (Dynamic Binding at Runtime):
  [CreditCard Gateway] Amount: Rs. 1,000.00 + Fee (1.8%): Rs. 18.00 -> Total Debited: Rs. 1,018.00

Calling hidden static method on 'polyRef' (Static Binding at Compile Time):
  [STATIC Protocol] Base Gateway: ISO-8583 Banking Message Standard
Direct Class Invocation on CreditCardGateway:
  [STATIC Protocol] CreditCard Gateway: PCI-DSS Level 1 Secure Tokenization

==========================================================================
  TOPIC 4 DEMONSTRATION COMPLETE: DYNAMIC DISPATCH CONFIRMED
=========================================================================="""
    add_terminal_output(out_t4, title="java -cp bin week2.PolymorphismDemo")

    # =========================================================================
    # 8. TOPIC 5: ABSTRACTION
    # =========================================================================
    add_custom_heading("7. Topic 5: Abstraction, Template Method Pattern & Interface Evolution", level=1)

    doc.add_paragraph(
        "Abstraction focuses on exposing 'what' an object does while hiding the underlying complexity of 'how' it accomplishes it. "
        "Java provides two complementary vehicles for abstraction: Abstract Classes and Interfaces."
    )

    doc.add_paragraph(
        "1. Abstract Classes (Partial Abstraction & State Retention):\n"
        "• Cannot be instantiated directly. May combine abstract methods with concrete implementations.\n"
        "• Retain instance fields and possess constructors invoked during subclass constructor chaining.\n"
        "• Template Method Design Pattern: The base class defines the invariant execution workflow inside a final method, "
        "delegating specific customizable steps to abstract methods implemented by subclasses.\n\n"
        "2. Interfaces (Contractual Abstraction & Multiple Type Inheritance):\n"
        "• Represent a strict capability contract ('CAN-DO' relationship).\n"
        "• All fields are implicitly public static final constants.\n"
        "• Java 8+ Enhancements: Introduced default methods (providing backward-compatible default implementations) and static utility methods.\n"
        "• Java 9+ Enhancements: Introduced private helper methods to encapsulate common logic across default methods."
    )

    add_custom_heading("7.1 Source Code Implementation (AbstractionDemo.java)", level=2)
    with open("src/week2/AbstractionDemo.java", "r") as f:
        code_t5 = f.read()
    add_code_block(code_t5, filename="src/week2/AbstractionDemo.java")

    add_custom_heading("7.2 Verified Terminal Output", level=2)
    out_t5 = """==========================================================================
  WEEK 2 - TOPIC 5: ABSTRACTION, TEMPLATE METHOD & INTERFACE CONTRACTS
==========================================================================

--- 1. Interface Static Utility & Default Methods ---
Interface Static Method Check (256-bit Key Sufficient?): true

--- 2. Template Method Pattern Execution ---
  [Interface Default] Compliance Check Passed: Protocol conforms to AES-256-GCM
  [Interface Private Helper] Zero-knowledge key rotation audit logged.

  === Starting Template Method: uploadObject() for user_telemetry_2026.parquet ===
  [AWS Connect] Authenticating IAM SigV4 into S3 bucket 'production-analytics-bucket' [Region: ap-south-1 (Mumbai)]...
  [AWS S3 PutObject] Uploaded 'user_telemetry_2026.parquet' (24576 bytes) with SHA-256 integrity hash.
  [Audit Trail] Action: UPLOAD:user_telemetry_2026.parquet | Operator: eng-admin-01    | Timestamp: 1792998171680
  [Base Cleanup] Session disconnected from bucket 'production-analytics-bucket' on Amazon Web Services (AWS S3).
  === Template Method Completed Successfully ===
  [Interface Default] Compliance Check Passed: Protocol conforms to AES-256-GCM
  [Interface Private Helper] Zero-knowledge key rotation audit logged.

  === Starting Template Method: uploadObject() for financial_audit_q3.csv ===
  [Azure Connect] Establishing SAS token session with Account 'corpstorageeast' -> Container 'backup-archive-container'...
  [Azure BlockBlob] Written block list for 'financial_audit_q3.csv' (57344 bytes) across Geo-Redundant Tier.
  [Audit Trail] Action: UPLOAD:financial_audit_q3.csv | Operator: finance-sec-09  | Timestamp: 1792998171682
  [Base Cleanup] Session disconnected from bucket 'backup-archive-container' on Microsoft Azure Blob Storage.
  === Template Method Completed Successfully ===

--- 3. Enterprise Architectural Trade-Off Analysis ---
  +---------------------------+-----------------------------------+-----------------------------------+
  | Architectural Dimension   | Abstract Class                    | Interface                         |
  +---------------------------+-----------------------------------+-----------------------------------+
  | State Retention           | Retains instance state (fields)   | Stateless (constants only)        |
  | Inheritance Model         | Single inheritance (extends 1)    | Multiple inheritance (implements N|
  | Constructor Lifecycle     | Has constructors (via super())    | No constructors                   |
  | Design Pattern Alignment  | Template Method Pattern           | Strategy, Observer, Factory       |
  | Primary Relationship      | Strict IS-A hierarchy             | Flexible CAN-DO capability        |
  +---------------------------+-----------------------------------+-----------------------------------+

==========================================================================
  TOPIC 5 DEMONSTRATION COMPLETE: CONTRACTUAL ABSTRACTION VERIFIED
=========================================================================="""
    add_terminal_output(out_t5, title="java -cp bin week2.AbstractionDemo")

    # =========================================================================
    # 9. TOPIC 6: IMPORTANCE OF OOP
    # =========================================================================
    add_custom_heading("8. Topic 6: Importance of OOP: Procedural Spaghetti vs. Modular OOP", level=1)

    doc.add_paragraph(
        "To truly appreciate the value of Object-Oriented Programming, one must observe how applications behave under procedural design "
        "versus object-oriented design when subjected to real-world software maintenance and feature expansion."
    )

    doc.add_paragraph(
        "1. The Procedural Failure Mode ('Spaghetti Code'):\n"
        "• Data is stored in disconnected global or parallel arrays (e.g. applicantNames[], loanTypes[], creditScores[]). "
        "If one array is sorted or re-indexed without synchronizing the others, catastrophic data corruption occurs.\n"
        "• Business rules are scattered across monolithic if-else-if or switch statements. Introducing a new loan type requires "
        "modifying and re-testing every procedural function across the entire codebase (violating the Open/Closed Principle).\n\n"
        "2. The Object-Oriented Resolution:\n"
        "• Encapsulation binds applicant attributes into a cohesive LoanApplicant object.\n"
        "• Polymorphism models loan types as independent LoanProduct subclasses (HomeLoan, PersonalLoan, EducationLoan).\n"
        "• Adding a new product (e.g. VehicleLoan) requires simply authoring a new subclass without modifying a single line of existing production code!"
    )

    add_custom_heading("8.1 Source Code Implementation (ImportanceOfOOPDemo.java)", level=2)
    with open("src/week2/ImportanceOfOOPDemo.java", "r") as f:
        code_t6 = f.read()
    add_code_block(code_t6, filename="src/week2/ImportanceOfOOPDemo.java")

    add_custom_heading("8.2 Verified Terminal Output", level=2)
    out_t6 = """==========================================================================
  WEEK 2 - TOPIC 6: IMPORTANCE OF OOP (PROCEDURAL VS. OOP DESIGN)
==========================================================================

--- 1. The Procedural Spaghetti Pattern ---
Defects: Parallel arrays (risk of index desynchronization), rigid switch logic,
         zero information hiding, any schema change breaks all procedural functions.

  [Procedural Processing via Parallel Arrays & Giant Switch/If Ladder]:
    Applicant: Ramesh Gupta     | Type: HOME      | Rate:  8.5% | Approved: YES   | Approx Monthly Int: Rs. 24,791.67
    Applicant: Sita Murthy      | Type: PERSONAL  | Rate: 14.5% | Approved: YES   | Approx Monthly Int: Rs. 4,833.33
    Applicant: Vikram Malhotra  | Type: EDUCATION | Rate:  7.5% | Approved: YES   | Approx Monthly Int: Rs. 7,500.00

--- 2. The Modular Object-Oriented Pattern ---
Benefits: Encapsulated applicants, polymorphic loan products, Open/Closed Principle.
          Adding a 'VehicleLoan' requires ZERO changes to existing classes!

    Applicant: Ramesh Gupta     | Product: Home Loan  | Rate:  8.5% | Approved: YES   | Approx Monthly Int: Rs. 24,791.67
    Applicant: Sita Murthy      | Product: Personal   | Rate: 14.5% | Approved: YES   | Approx Monthly Int: Rs. 4,833.33
    Applicant: Vikram Malhotra  | Product: Education  | Rate:  7.5% | Approved: YES   | Approx Monthly Int: Rs. 7,500.00

--- 3. Architectural Comparison: Procedural vs. OOP ---
  +-------------------+-----------------------------------+-----------------------------------+
  | Criterion         | Procedural Programming            | Object-Oriented Programming       |
  +-------------------+-----------------------------------+-----------------------------------+
  | Maintainability   | Low (Spaghetti branching; ripple  | High (Encapsulated classes; bugs  |
  |                   | effects on any code change)       | isolated to local boundaries)     |
  | Extensibility     | Requires modifying core if-else   | Open/Closed: Add new classes      |
  |                   | statements across all functions   | without touching existing code    |
  | Code Reuse        | Copy-paste duplication            | Inheritance & polymorphic reuse   |
  | Testability       | Difficult; global state causes    | Excellent; loose coupling enables |
  |                   | hidden side effects               | unit testing with mock objects    |
  +-------------------+-----------------------------------+-----------------------------------+

==========================================================================
  TOPIC 6 DEMONSTRATION COMPLETE: ARCHITECTURAL PARADIGM CONTRASTED
=========================================================================="""
    add_terminal_output(out_t6, title="java -cp bin week2.ImportanceOfOOPDemo")

    # =========================================================================
    # 10. DEDICATED ANALYSIS: INDUSTRY ADOPTION OF OOP
    # =========================================================================
    add_custom_heading("9. Industry Adoption and Practical Significance of OOP in Modern Java", level=1)

    doc.add_paragraph(
        "The object-oriented paradigm forms the architectural backbone of modern enterprise software. The following industry "
        "pillars demonstrate why OOP dominates production systems:"
    )

    doc.add_paragraph(
        "• Spring Framework & Dependency Injection: Spring manages enterprise application beans as encapsulated objects, injecting dependencies "
        "via interfaces (e.g. PaymentService interface injected into OrderController). This decoupling enables seamless swapping of implementations "
        "and automated unit testing with Mockito.\n"
        "• Object-Relational Mapping (Hibernate / JPA): Relational database tables are mapped directly into encapsulated Java entity objects "
        "(e.g., @Entity class User), translating relational tuples into rich object graphs with validated getters and setters.\n"
        "• Domain-Driven Design (DDD): Complex enterprise domains (banking, logistics, healthcare) are organized into Bounded Contexts and Aggregates, "
        "where domain entities encapsulate invariants and business logic rather than relying on anemic procedural data structures.\n"
        "• Clean Architecture & SOLID Principles: OOP directly enables the SOLID design principles:\n"
        "    - Single Responsibility Principle (SRP): Each class encapsulates one cohesive business concern.\n"
        "    - Open/Closed Principle (OCP): Classes are open for extension (via inheritance/polymorphism) but closed for modification.\n"
        "    - Liskov Substitution Principle (LSP): Subclasses must be substitutable for their superclasses without breaking client behavior.\n"
        "    - Interface Segregation Principle (ISP): Clients should not be forced to depend on interfaces they do not use.\n"
        "    - Dependency Inversion Principle (DIP): High-level modules depend on abstractions (interfaces), not concrete low-level implementations."
    )

    # =========================================================================
    # 11. CAPSTONE APPLICATION DOCUMENTATION
    # =========================================================================
    add_custom_heading("10. Integrated Capstone: Enterprise Autonomous & Electric Fleet System", level=1)

    doc.add_paragraph(
        "To demonstrate the cohesive synthesis of all OOP concepts in an enterprise setting, an autonomous logistics fleet management "
        "system was architected in EnterpriseFleetManagementApp.java. The system models a multimodal corporate fleet consisting of "
        "Electric Semi-Trucks, Urban Delivery Vans, and Express Autonomous Drones."
    )

    doc.add_paragraph(
        "Architectural Integration Matrix:\n"
        "• Encapsulation: Abstract class Vehicle encapsulates vehicleId, VIN, model, currentBatteryLevel, and batteryCapacityKwh. "
        "The 17-character VIN is immutable, and battery updates enforce strict boundary constraints.\n"
        "• Abstraction: The AutonomousNavigable interface exposes navigateTo() and getSensorTelemetry(), while the Rechargeable interface "
        "exposes chargeBattery() and getEstimatedChargingTimeMinutes().\n"
        "• Inheritance: ElectricTruck, DeliveryVan, and AutonomousDrone inherit base telemetry and specialize operational consumption curves.\n"
        "• Polymorphism: A heterogeneous List<Vehicle> is traversed uniformly. The JVM dispatches calculateOperationalRangeKm(), dispatchMission(), "
        "and computeEstimatedMaintenanceCost() to the concrete subclass implementations at runtime.\n"
        "• Placement Advanced Features: Implements Java 16+ Pattern Matching for instanceof, final audit methods, and Deep vs. Shallow Cloning "
        "with nested Waypoint objects."
    )

    add_custom_heading("10.1 Capstone Source Code (EnterpriseFleetManagementApp.java)", level=2)
    with open("src/week2/EnterpriseFleetManagementApp.java", "r") as f:
        code_cap = f.read()
    add_code_block(code_cap, filename="src/week2/EnterpriseFleetManagementApp.java")

    add_custom_heading("10.2 Capstone Verified Terminal Output", level=2)
    out_cap = """====================================================================================
       YUVAINTERN LOGISTICS SYSTEMS - AUTONOMOUS & ELECTRIC FLEET MANAGER
       WEEK 2 CAPSTONE DEMONSTRATION: FULL OBJECT-ORIENTED ARCHITECTURE
====================================================================================

--- 1. Fleet Provisioning & State Initialization ---
Successfully provisioned 4 multimodal autonomous & electric fleet assets.

--- 2. Fleet Telemetry & Dynamic Operational Range Matrix ---
+----------+------------------------+------------+---------------+----------------+----------------+
| Asset ID | Vehicle Model          | Class Type | Current Batt. | Est. Range     | Annual Maint.  |
+----------+------------------------+------------+---------------+----------------+----------------+
| TRK-101  | Tata Prima EV Heavy    | Semi-Truck |        92.0% |       230.0 km | Rs.    8500.00 |
| VAN-204  | Rivian EDV 700         | LastMile Van |        78.0% |       236.8 km | Rs.    3200.00 |
| DRN-309  | DJI FlyCart Express    | Express Drone |        88.0% |        70.4 km | Rs.    1450.00 |
| TRK-102  | Volvo FH Electric Semi | Semi-Truck |        22.0% |        55.0 km | Rs.    8500.00 |
+----------+------------------------+------------+---------------+----------------+----------------+
Total Projected Fleet Maintenance Budget: Rs. 21,650.00

--- 3. Autonomous Navigation Contracts & Sensor Telemetry ---
Asset [VAN-204] conforms to AutonomousNavigable Contract:
    [Autonomous Nav] LiDAR point-cloud recalibrated: 360-degree SLAM field active.
  [Van VAN-204 Navigation] GPS Route locked: [Electronic City Tech Park (12.8399, 77.6770)] via Urban Highway-44
    Sensor Rig -> Solid-State LiDAR: ACTIVE | 8x HDR Optical Cameras: 60FPS | Radar: 120m Sweep

Asset [DRN-309] conforms to AutonomousNavigable Contract:
    [Autonomous Nav] LiDAR point-cloud recalibrated: 360-degree SLAM field active.
  [Drone DRN-309 Navigation] Direct 3D Airway vector locked to [Electronic City Tech Park (12.8399, 77.6770)]
    Sensor Rig -> Dual Optical Flow + Ultrasonic Altitude Sonar + RTK-GPS (Centimeter Precision)

--- 4. Mission Dispatch Simulation & Security Audit ---
  [Truck TRK-101] MISSION DISPATCHED to Whitefield Distribution Hub with 18.5 tons cargo. Battery now: 47.0%
    [SEC-AUDIT] VID=TRK-101    | VIN=1HTMK48D930291847 | ACTION=TRUCK_DISPATCH     | BATTERY=47.0%
  [Truck TRK-102] REJECTED: Range 55.0 km is insufficient for mission (needs 180.0 km).
  [Drone DRN-309] REJECTED: Payload 35.0 kg exceeds FAA drone limit (15.0 kg).
  [Van VAN-204] AUTONOMOUS ROUTE engaged to Koramangala Commercial Hub. Cargo parcels loaded: 150. Battery: 53.0%
    [SEC-AUDIT] VID=VAN-204    | VIN=1GBHG31J482910384 | ACTION=VAN_AUTONOMOUS_RUN | BATTERY=53.0%

--- 5. Energy Grid Replenishment (Rechargeable Interface) ---
Asset [TRK-101] low battery (47.0%). Requesting Grid DC Fast Charging:
    [Rechargeable] Verified standard compatibility: CCS-2 Combo (CCS Type 2)
    Estimated Time to 90%: 64 minutes.
  [Truck TRK-101] Charged 120.0 kWh. Battery level restored to 81.3%

Asset [VAN-204] low battery (53.0%). Requesting Grid DC Fast Charging:
    [Rechargeable] Verified standard compatibility: CCS-2 Combo (CCS Type 2)
    Estimated Time to 90%: 29 minutes.
  [Van VAN-204] AC Fast Charging: Restored to 100.0%

Asset [TRK-102] low battery (22.0%). Requesting Grid DC Fast Charging:
    [Rechargeable] Verified standard compatibility: CCS-2 Combo (CCS Type 2)
    Estimated Time to 90%: 102 minutes.
  [Truck TRK-102] Charged 120.0 kWh. Battery level restored to 56.3%

--- 6. Placement Edge Case: Deep vs. Shallow Object Cloning ---
Original Van Next Waypoint: [Electronic City Tech Park (12.8399, 77.6770)]
Cloned Van Next Waypoint  : [Electronic City Tech Park (12.8399, 77.6770)]

Mutating cloned van's waypoint location name to 'Airport Cargo Terminal'...
Result Check:
  Original Van Waypoint: Electronic City Tech Park (UNTOUCHED! Deep copy verified)
  Cloned Van Waypoint  : Airport Cargo Terminal
  Waypoint Memory Reference Match? (original == clone): false (Different Heap addresses)

====================================================================================
  CAPSTONE FLEET SIMULATION CONCLUDED: ZERO CONTRACT OR LOGIC VIOLATIONS
===================================================================================="""
    add_terminal_output(out_cap, title="java -cp bin week2.EnterpriseFleetManagementApp")

    # =========================================================================
    # 12. VERIFICATION & TESTING SUMMARY
    # =========================================================================
    add_custom_heading("11. Comprehensive Verification & Testing Summary", level=1)

    doc.add_paragraph(
        "All seven Week 2 Java source files were compiled using the Oracle JDK 22 compiler and executed under the 64-Bit HotSpot VM. "
        "Zero compilation warnings, syntax errors, or runtime anomalies occurred. The test matrix below records the verified execution status:"
    )

    test_table = doc.add_table(rows=8, cols=5)
    test_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    test_table.columns[0].width = Inches(2.0)
    test_table.columns[1].width = Inches(1.8)
    test_table.columns[2].width = Inches(1.0)
    test_table.columns[3].width = Inches(0.6)
    test_table.columns[4].width = Inches(1.1)
    set_table_borders(test_table)

    t_headers = ["Program Class Name", "Source File Path", "Compiler", "Exit", "Status"]
    for i, h in enumerate(t_headers):
        cell = test_table.rows[0].cells[i]
        set_cell_background(cell, HEX_HEADER_BG)
        set_cell_margins(cell, top=80, bottom=80, left=100, right=100)
        p = cell.paragraphs[0]
        r = p.add_run(h)
        r.bold = True
        r.font.size = Pt(9.5)
        r.font.color.rgb = RGBColor(255, 255, 255)

    test_rows = [
        ("ClassesAndObjectsDemo", "src/week2/ClassesAndObjectsDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("EncapsulationDemo", "src/week2/EncapsulationDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("InheritanceDemo", "src/week2/InheritanceDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("PolymorphismDemo", "src/week2/PolymorphismDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("AbstractionDemo", "src/week2/AbstractionDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("ImportanceOfOOPDemo", "src/week2/ImportanceOfOOPDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
        ("EnterpriseFleetManagementApp", "src/week2/EnterpriseFleetManagementApp.java", "javac 22.0.1", "0", "PASSED (100%)")
    ]

    for row_idx, data in enumerate(test_rows, start=1):
        row = test_table.rows[row_idx]
        bg = HEX_LIGHT_ROW if row_idx % 2 == 1 else "FFFFFF"
        for col_idx, text in enumerate(data):
            cell = row.cells[col_idx]
            set_cell_background(cell, bg)
            set_cell_margins(cell, top=70, bottom=70, left=100, right=100)
            p = cell.paragraphs[0]
            p.paragraph_format.space_after = Pt(1)
            r = p.add_run(text)
            r.font.size = Pt(9)
            if col_idx == 0:
                r.bold = True
                r.font.color.rgb = COLOR_PRIMARY_NAVY
            elif col_idx == 4:
                r.bold = True
                r.font.color.rgb = RGBColor(22, 101, 52)

    doc.add_paragraph()

    # =========================================================================
    # 13. PLACEMENT INTERVIEW CHEATSHEET
    # =========================================================================
    add_custom_heading("12. Placement Interview Cheatsheet: Top 8 Java OOP Traps", level=1)

    qa_list = [
        ("Q1: Can we override a private or static method in Java?",
         "No. Private methods are not visible outside their declaring class, so a subclass cannot override them. Static methods belong to the class metadata (Metaspace) rather than object instances on the Heap; declaring a static method in a subclass with the same signature merely 'hides' the parent method. Static binding occurs at compile time based on the reference type."),
        
        ("Q2: What is the exact difference between this() and super() in constructors?",
         "'this()' invokes another overloaded constructor within the SAME class (constructor chaining). 'super()' invokes a constructor in the IMMEDIATE PARENT class. Both must strictly appear as the FIRST statement in a constructor body, which means a single constructor cannot contain both this() and super()."),
        
        ("Q3: Why doesn't Java support Multiple Class Inheritance, but allows Multiple Interface Inheritance?",
         "Multiple class inheritance introduces the 'Diamond Problem' (ambiguity when two parent classes implement the same method or maintain conflicting instance state). With interfaces, prior to Java 8, interfaces contained no method bodies or state, so there was zero implementation ambiguity. In Java 8+, if two interfaces define conflicting default methods, the compiler forces the implementing class to explicitly override and disambiguate using InterfaceName.super.method()."),
        
        ("Q4: What is a Covariant Return Type and when is it useful?",
         "Introduced in Java 5, a covariant return type allows an overriding subclass method to declare a return type that is a SUBTYPE of the superclass method's return type. For example, if superclass method returns 'Vehicle', subclass can return 'ElectricTruck'. This eliminates tedious, error-prone manual type-casting on the caller side."),
        
        ("Q5: What is the difference between Shallow Copy and Deep Copy?",
         "A shallow copy duplicates the primary object, but any internal reference fields still point to the EXACT SAME nested objects in Heap memory as the original (aliasing). A deep copy recursively duplicates both the primary object AND all nested reference objects, ensuring complete memory isolation so mutations on the clone never affect the original."),
        
        ("Q6: Can an abstract class have a constructor? Why or why not?",
         "Yes! An abstract class can—and often does—have constructors. Even though an abstract class cannot be instantiated directly via 'new AbstractClass()', its constructors are invoked by subclasses during constructor chaining (super(...)) to properly initialize inherited fields."),
        
        ("Q7: How does Java 16+ Pattern Matching for instanceof improve code quality?",
         "Prior to Java 16, testing and casting required two redundant steps: 'if (obj instanceof Vehicle) { Vehicle v = (Vehicle) obj; ... }'. Pattern matching combines the type check and variable binding into a single atomic operation: 'if (obj instanceof Vehicle v) { ... }', eliminating boilerplate casting and ClassCastException risks."),
        
        ("Q8: Why is composition frequently favored over inheritance ('Favor composition over inheritance')?",
         "Inheritance breaks encapsulation because subclasses depend intimately on superclass internal implementation details (the 'fragile base class' problem). Composition constructs complex types by containing references to other objects (HAS-A instead of IS-A), offering greater flexibility, looser coupling, and runtime swappability.")
    ]

    for q, a in qa_list:
        p_q = doc.add_paragraph()
        p_q.paragraph_format.space_before = Pt(6)
        p_q.paragraph_format.space_after = Pt(2)
        r_q = p_q.add_run(q)
        r_q.bold = True
        r_q.font.size = Pt(10.5)
        r_q.font.color.rgb = COLOR_PRIMARY_NAVY

        p_a = doc.add_paragraph()
        p_a.paragraph_format.space_after = Pt(6)
        r_a = p_a.add_run(a)
        r_a.font.size = Pt(10)
        r_a.font.color.rgb = COLOR_CHARCOAL

    # =========================================================================
    # 14. CONCLUSION AND REFERENCES
    # =========================================================================
    add_custom_heading("13. Conclusion and Professional References", level=1)

    doc.add_paragraph(
        "Week 2 of the YuvaIntern Engineer – Java Development Internship has established comprehensive, verified competency "
        "in the Object-Oriented Programming paradigm. By moving beyond superficial definitions into JVM memory models, dynamic dispatch vtables, "
        "defensive copying, and modern enterprise design patterns, the candidate is fully prepared for placement technical interviews "
        "and production backend development."
    )

    doc.add_paragraph(
        "References & Citations:\n"
        "1. Bloch, J. (2018). Effective Java (3rd Edition). Addison-Wesley Professional. [Items 15, 17, 18, 19, 20].\n"
        "2. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). Design Patterns: Elements of Reusable Object-Oriented Software. Addison-Wesley.\n"
        "3. Oracle Corporation. (2024). The Java Language Specification (Java SE 22 Edition) – Chapter 8 (Classes) & Chapter 9 (Interfaces).\n"
        "4. Horstmann, C. S. (2023). Core Java Volume I – Fundamentals (12th Edition). Prentice Hall.\n"
        "5. YuvaIntern Virtual Internship Program Curriculum – Week 2: Object-Oriented Programming in Java."
    )

    # Save to docs folder and root
    doc_path1 = os.path.abspath("docs/Task2_Object_Oriented_Programming_Java.docx")
    doc_path2 = os.path.abspath("Task2_Object_Oriented_Programming_Java.docx")
    doc.save(doc_path1)
    doc.save(doc_path2)
    print(f"Week 2 Report generated successfully:\n1. {doc_path1}\n2. {doc_path2}")

if __name__ == "__main__":
    create_document()
