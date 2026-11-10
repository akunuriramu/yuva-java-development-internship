import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls


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
    HEX_CONSOLE_BG = "0F172A"
    HEX_BORDER = "CBD5E1"

    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Calibri'
    normal_style.font.size = Pt(11)
    normal_style.font.color.rgb = COLOR_CHARCOAL
    normal_style.paragraph_format.line_spacing = 1.15
    normal_style.paragraph_format.space_after = Pt(6)

    def set_cell_background(cell, hex_color):
        shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
        cell._tc.get_or_add_tcPr().append(shading)

    def set_cell_margins(cell, top=100, bottom=100, left=140, right=140):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(
            f'<w:tcMar {nsdecls("w")}>'
            f'<w:top w:w="{top}" w:type="dxa"/>'
            f'<w:bottom w:w="{bottom}" w:type="dxa"/>'
            f'<w:left w:w="{left}" w:type="dxa"/>'
            f'<w:right w:w="{right}" w:type="dxa"/>'
            f'</w:tcMar>'
        )
        tcPr.append(tcMar)

    def set_table_borders(table, color=HEX_BORDER, sz="4"):
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
        borders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:left w:val="single" w:sz="24" w:space="0" w:color="2563EB"/>'
            f'<w:top w:val="none"/><w:right w:val="none"/><w:bottom w:val="none"/>'
            f'</w:tcBorders>'
        )
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
        set_cell_margins(cell, top=120, bottom=120, left=160, right=160)

        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:left w:val="single" w:sz="16" w:space="0" w:color="0E7490"/>'
            f'<w:top w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/>'
            f'<w:right w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/>'
            f'<w:bottom w:val="single" w:sz="4" w:space="0" w:color="{HEX_BORDER}"/>'
            f'</w:tcBorders>'
        )
        tcPr.append(borders)

        p = cell.paragraphs[0]
        p.paragraph_format.line_spacing = 1.05
        p.paragraph_format.space_after = Pt(2)

        if filename:
            r_fn = p.add_run(f"// File: {filename}\n")
            r_fn.font.name = 'Consolas'
            r_fn.font.size = Pt(8.5)
            r_fn.bold = True
            r_fn.font.color.rgb = COLOR_ACCENT_TEAL

        r_code = p.add_run(code_text.strip())
        r_code.font.name = 'Consolas'
        r_code.font.size = Pt(8.5)
        r_code.font.color.rgb = RGBColor(30, 41, 59)
        doc.add_paragraph()

    def add_terminal_output(output_text, title="Verified Execution Output (Terminal)"):
        table = doc.add_table(rows=1, cols=1)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        table.columns[0].width = Inches(6.5)
        cell = table.cell(0, 0)
        set_cell_background(cell, HEX_CONSOLE_BG)
        set_cell_margins(cell, top=120, bottom=120, left=160, right=160)

        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:left w:val="single" w:sz="16" w:space="0" w:color="22C55E"/>'
            f'<w:top w:val="single" w:sz="4" w:space="0" w:color="334155"/>'
            f'<w:right w:val="single" w:sz="4" w:space="0" w:color="334155"/>'
            f'<w:bottom w:val="single" w:sz="4" w:space="0" w:color="334155"/>'
            f'</w:tcBorders>'
        )
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
        r_out.font.size = Pt(8.0)
        r_out.font.color.rgb = RGBColor(226, 232, 240)
        doc.add_paragraph()

    def add_styled_table(headers, rows, col_widths, font_size=9.5, highlight_last_col=False):
        table = doc.add_table(rows=len(rows) + 1, cols=len(headers))
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        table.autofit = False
        for i, w in enumerate(col_widths):
            table.columns[i].width = Inches(w)
        set_table_borders(table)

        for i, h in enumerate(headers):
            cell = table.rows[0].cells[i]
            cell.width = Inches(col_widths[i])
            set_cell_background(cell, HEX_HEADER_BG)
            set_cell_margins(cell, top=90, bottom=90, left=100, right=100)
            p = cell.paragraphs[0]
            p.paragraph_format.space_after = Pt(1)
            r = p.add_run(h)
            r.bold = True
            r.font.size = Pt(font_size)
            r.font.color.rgb = RGBColor(255, 255, 255)

        for r_idx, row_data in enumerate(rows, start=1):
            bg = HEX_LIGHT_ROW if r_idx % 2 == 1 else "FFFFFF"
            for c_idx, val in enumerate(row_data):
                cell = table.rows[r_idx].cells[c_idx]
                cell.width = Inches(col_widths[c_idx])
                set_cell_background(cell, bg)
                set_cell_margins(cell, top=80, bottom=80, left=100, right=100)
                p = cell.paragraphs[0]
                p.paragraph_format.space_after = Pt(1)
                r = p.add_run(val)
                r.font.size = Pt(font_size - 0.5)
                if c_idx == 0:
                    r.bold = True
                    r.font.color.rgb = COLOR_PRIMARY_NAVY
                elif highlight_last_col and c_idx == len(headers) - 1:
                    r.bold = True
                    r.font.color.rgb = RGBColor(22, 101, 52)
        doc.add_paragraph()

    def add_bullet_lines(items):
        for title, desc in items:
            p = doc.add_paragraph()
            p.paragraph_format.left_indent = Inches(0.2)
            p.paragraph_format.space_after = Pt(4)
            r1 = p.add_run(f"• {title}: ")
            r1.bold = True
            r1.font.color.rgb = COLOR_PRIMARY_NAVY
            r2 = p.add_run(desc)
            r2.font.color.rgb = COLOR_CHARCOAL

    def load_source(rel_path):
        with open(rel_path, "r", encoding="utf-8") as f:
            return f.read()

    # =========================================================================
    # TITLE PAGE
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
    r_track = p_track.add_run("TRACK: JAVA SOFTWARE DEVELOPMENT | WEEK 3 TECHNICAL MILESTONE")
    r_track.font.size = Pt(11)
    r_track.bold = True
    r_track.font.color.rgb = COLOR_ACCENT_TEAL

    p_title = doc.add_paragraph()
    p_title.paragraph_format.space_after = Pt(10)
    r_title = p_title.add_run("Task 3: Data Structures in Java")
    r_title.font.size = Pt(26)
    r_title.bold = True
    r_title.font.color.rgb = COLOR_PRIMARY_NAVY

    p_sub = doc.add_paragraph()
    p_sub.paragraph_format.space_after = Pt(32)
    r_sub = p_sub.add_run(
        "From-Scratch Algorithmic Implementations, JVM Heap & Pointer Memory Dynamics, "
        "Big-O Complexity Analysis, E-Commerce Fulfillment Capstone Engine, and Placement Engineering Guide"
    )
    r_sub.font.size = Pt(13)
    r_sub.italic = True
    r_sub.font.color.rgb = COLOR_MUTED

    meta_table = doc.add_table(rows=8, cols=2)
    meta_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    meta_table.columns[0].width = Inches(2.2)
    meta_table.columns[1].width = Inches(4.3)
    set_table_borders(meta_table, color="94A3B8", sz="6")

    meta_data = [
        ("Intern Name", "Akunuri Ramu"),
        ("Intern Role / Designation", "YuvaIntern Student Engineer - Java Development"),
        ("Project Milestone", "Week 3: Data Structures in Java (From-Scratch Implementations)"),
        ("GitHub Repository", "https://github.com/akunuriramu/yuva-java-development-internship"),
        ("Java Platform / JDK", "Java SE Development Kit (JDK 22.0.1+8-16, 64-Bit HotSpot VM)"),
        ("Development Environment", "Windows 11 x64, PowerShell, javac -Xlint:all (0 Warnings)"),
        ("Verification Methodology", "100% Genuine Local Compilation & Terminal Execution Output"),
        ("Core Data Structures", "Dynamic Arrays, Singly & Doubly Linked Lists, Stacks, Circular Queues, Binary Search Trees (BST), HashMaps (Separate Chaining) + E-Commerce Capstone")
    ]

    for row_idx, (lbl, val) in enumerate(meta_data):
        row = meta_table.rows[row_idx]
        c0, c1 = row.cells[0], row.cells[1]
        set_cell_background(c0, "F8FAFC")
        set_cell_background(c1, "FFFFFF")
        set_cell_margins(c0, top=95, bottom=95, left=140, right=140)
        set_cell_margins(c1, top=95, bottom=95, left=140, right=140)

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
    # SECTION 1: EXECUTIVE SCOPE & DATA STRUCTURE SELECTION FRAMEWORK
    # =========================================================================
    add_custom_heading("1. Executive Scope & Data Structure Selection Framework", level=1)

    doc.add_paragraph(
        "Week 3 of the YuvaIntern Java Development Virtual Internship focuses on foundational computer science "
        "mastery: designing, implementing, and benchmarking all six core linear and non-linear data structures "
        "from first principles in Java—without delegating to java.util collection shortcuts. Understanding the "
        "underlying pointer wiring, contiguous vs. non-contiguous JVM Heap allocation, resizing amortization, "
        "and worst-case complexity degradation is essential for writing low-latency backend services."
    )

    add_custom_heading("1.1 Architectural Selection Framework", level=2)
    doc.add_paragraph(
        "Selecting the wrong data structure turns an O(1) microservice endpoint into an O(n) latency bottleneck "
        "under load. The engineering decision matrix below maps enterprise workload requirements to the optimal "
        "data structure and underlying JVM memory topology:"
    )

    add_styled_table(
        headers=["Workload Access Pattern", "Optimal Structure", "Heap Memory Topology", "Enterprise Production Example"],
        rows=[
            ("Fast random access by integer index; cache-friendly sequential scans", "Dynamic Array", "Contiguous Heap block (base + i * stride)", "Financial time-series tick buffers, batch warehouse audits"),
            ("Frequent O(1) head/tail splicing or mid-list insertion with known node", "Singly / Doubly Linked List", "Non-contiguous Heap nodes linked via references", "Shipment transit hops, LRU cache eviction chains"),
            ("Strict LIFO rollback, syntax nesting, or recursive state tracking", "Stack (Array / Linked)", "Contiguous array w/ top index or linked nodes", "Transactional undo/rollback history, JSON delimiter validation"),
            ("Strict FIFO buffering without head-shift penalties", "Circular Queue / Linked Queue", "Fixed ring buffer w/ modulo wrap or head/tail nodes", "Order fulfillment pipelines, support ticket dispatch engines"),
            ("Sorted traversal, dynamic predecessor/successor, and range queries", "Binary Search Tree (BST)", "Hierarchical TreeNode objects (left/right pointers)", "Database price-range indexing, interval scheduling"),
            ("Instant key-value lookup, deduplication, and session caching", "HashMap (Separate Chaining)", "Bucket array of linked-list chains w/ dynamic rehash", "O(1) SKU catalog lookup, distributed API session cache")
        ],
        col_widths=[1.8, 1.3, 1.6, 1.8],
        font_size=9.0
    )

    add_callout(
        "Modern CPUs rely heavily on L1/L2 cache lines (typically 64 bytes). Contiguous arrays achieve high spatial "
        "locality because adjacent elements are prefetched together, whereas linked nodes scattered across the JVM "
        "Heap incur pointer-chasing cache misses and an extra 12-16 bytes of JVM Object Header overhead per node.",
        label="JVM & CPU CACHE LOCALITY"
    )

    # =========================================================================
    # SECTION 2: ARRAYS & DYNAMIC ARRAY MECHANICS
    # =========================================================================
    add_custom_heading("2. Arrays & Dynamic Array Mechanics (ArraysDSDemo)", level=1)

    doc.add_paragraph(
        "An array is a fixed-size, contiguous block of Heap memory. Because elements occupy uniform byte strides, "
        "random access arr[i] computes (baseAddress + i * elementBytes) in strictly O(1) hardware time. Dynamic arrays "
        "(such as java.util.ArrayList) wrap a fixed backing array and grow geometrically (2x) whenever capacity is exhausted. "
        "Although a single resize copies n elements in O(n), doubling capacity guarantees that n appends perform at most "
        "2n total writes, yielding O(1) amortized append complexity."
    )

    add_custom_heading("2.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/ArraysDSDemo.java"), filename="src/week3/ArraysDSDemo.java")

    add_bullet_lines([
        ("Lines 29-34 (append & 2x Geometric Resize)", "Checks size == backingStore.length; when full, resize(length * 2) allocates a fresh Heap block and executes System.arraycopy() (JVM intrinsic memcpy), making the old array eligible for GC."),
        ("Lines 37-49 (insertAt Right-Shift)", "Iterates backward from index size down to index + 1 so elements shift right without overwriting adjacent slots, costing O(n) worst-case time."),
        ("Lines 52-61 (deleteAt Left-Shift)", "Captures removed value, shifts elements [index+1..size-1] left by one slot, and zeroes the trailing slot."),
        ("Lines 73-82 (Two-Pointer Reversal)", "Initializes left=0 and right=size-1 converging inward via in-place swaps in O(n) time and O(1) auxiliary space."),
        ("Lines 104-125 (Kadane's Algorithm)", "Maintains running currentMax = max(arr[i], currentMax + arr[i]), resetting candidateStart whenever starting fresh beats extending the negative prefix."),
        ("Lines 130-150 (Sliding Window Telemetry)", "Maintains a running sum over windowSize=3 by adding latencyMs[i] and subtracting latencyMs[i - windowSize], reducing O(n*k) brute force to O(n).")
    ])

    add_custom_heading("2.2 Verified Terminal Output", level=2)
    out_arrays = """==========================================================================
  WEEK 3 - MODULE 1: ARRAYS, DYNAMIC RESIZING & SUBARRAY ALGORITHMS
==========================================================================

--- 1. Dynamic Array Contiguous Memory & Geometric Resizing (2x) ---
  [Heap Resize Event] Capacity expanded: 2 -> 4 (Copied 2 elements, O(n))
  [Heap Resize Event] Capacity expanded: 4 -> 8 (Copied 4 elements, O(n))
  Current Elements : [105, 210, 315, 420, 525] | Size: 5 | Capacity: 8 | Resizes: 2

--- 2. O(1) Random Access, O(n) Insert & Delete Operations ---
  O(1) Lookup at index 3: 420
  After insertAt(2, 999): [105, 210, 999, 315, 420, 525] (Elements 2..4 shifted right)
  After deleteAt(1) [removed 210]: [105, 999, 315, 420, 525] (Elements 2..5 shifted left)

--- 3. Two-Pointer In-Place Reversal (O(n) Time, O(1) Space) ---
  Reversed Array   : [525, 420, 315, 999, 105]

--- 4. Enterprise Use Case: Financial P&L & Telemetry Subarray Analytics ---
  Input Cash Flows : [-25, 40, -10, 65, 30, -90, 55, 20]
  Peak Profit Sum  : 125 (Window Indices [1..4])
  Latency Stream (ms): [18, 22, 45, 80, 95, 30, 15, 25]
  Peak 3-Req Window  : Sum = 220 ms | Avg = 73.33 ms (Start Index: 2)

--- 5. Big-O Time & Space Complexity Breakdown (Dynamic Array) ---
  +------------------------+--------------+--------------+--------------+------------+
  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |
  +------------------------+--------------+--------------+--------------+------------+
  | Indexed Access get(i)  | O(1)         | O(1)         | O(1)         | O(1)       |
  | Append (End)           | O(1)         | O(1) amort.  | O(n) resize  | O(n)       |
  | Insert at Index k      | O(1) (tail)  | O(n)         | O(n)         | O(1)       |
  | Delete at Index k      | O(1) (tail)  | O(n)         | O(n)         | O(1)       |
  | Two-Pointer Reversal   | O(n)         | O(n)         | O(n)         | O(1)       |
  | Kadane / Sliding Win.  | O(n)         | O(n)         | O(n)         | O(1)       |
  +------------------------+--------------+--------------+--------------+------------+

==========================================================================
  MODULE 1 COMPLETE: ALL ARRAY OPERATIONS VERIFIED
=========================================================================="""
    add_terminal_output(out_arrays, title="java -cp bin week3.ArraysDSDemo")

    add_custom_heading("2.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time", "Aux Space"],
        rows=[
            ("Random Access get(i)", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("Append (Dynamic 2x)", "O(1)", "O(1) amortized", "O(n) on resize", "O(n)"),
            ("Insert / Delete at k", "O(1) at tail", "O(n)", "O(n)", "O(1)"),
            ("Two-Pointer / Kadane / Window", "O(n)", "O(n)", "O(n)", "O(1)")
        ],
        col_widths=[2.1, 1.0, 1.2, 1.2, 1.0]
    )

    # =========================================================================
    # SECTION 3: SINGLY & DOUBLY LINKED LISTS
    # =========================================================================
    add_custom_heading("3. Singly & Doubly Linked Lists (LinkedListDSDemo)", level=1)

    doc.add_paragraph(
        "Linked lists decouple logical sequence order from physical Heap contiguity. Each Node is an independent "
        "Heap allocation holding a data reference and next (plus prev in Doubly Linked Lists) pointer references. "
        "This eliminates resize copies and enables O(1) head/tail splicing, while sacrificing O(1) random indexing "
        "because locating index k requires traversing k pointer hops."
    )

    add_custom_heading("3.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/LinkedListDSDemo.java"), filename="src/week3/LinkedListDSDemo.java")

    add_bullet_lines([
        ("Lines 35-56 (insertAtHead & insertAtTail)", "Maintains both head and tail pointers so both prepending and appending execute in strictly O(1) time without full traversal."),
        ("Lines 58-77 (insertAtPosition)", "Walks to predecessor index - 1, binds newNode.next = prev.next BEFORE updating prev.next = newNode so the remaining chain is never orphaned."),
        ("Lines 79-103 (deleteAtPosition)", "Rewires predecessor pointer around the target node and updates tail when deleting the final element, allowing the unlinked node to be collected by the JVM GC."),
        ("Lines 154-169 (DoublyLinkedList.removeLast)", "Uses the tail.prev pointer to unlink the tail node in O(1) time—an operation that costs O(n) in a Singly Linked List—and clears tail.next = null to prevent memory leaks."),
        ("Lines 199-225 (Floyd's Tortoise & Hare)", "Advances slow by 1 step and fast by 2 steps; if they collide, resets slow to head and advances both at speed 1. By modular loop invariants, they reconverge at the exact cycle entry node in O(n) time and O(1) space.")
    ])

    add_custom_heading("3.2 Verified Terminal Output", level=2)
    out_ll = """==========================================================================
  WEEK 3 - MODULE 2: SINGLY & DOUBLY LINKED LISTS + CYCLE DETECTION
==========================================================================

--- 1. Singly Linked List: Head, Tail & K-th Position Splicing ---
  Pipeline Chain : [INGRESS-GW] -> [AUTH-CHECK] -> [RATE-LIMIT] -> [FRAUD-SCAN] -> null
  Deleted idx 2  : RATE-LIMIT
  Updated Chain  : [INGRESS-GW] -> [AUTH-CHECK] -> [FRAUD-SCAN] -> null (Size: 3)

--- 2. Doubly Linked List: Enterprise Navigation History ---
  Forward Pass   : null <-> [Login] <-> [Dashboard] <-> [Orders] <-> [Invoice-#402] <-> null
  Backward Pass  : TAIL -> [Invoice-#402] <- [Orders] <- [Dashboard] <- [Login] -> HEAD
  O(1) Tail Pop  : Removed [Invoice-#402]
  After Pop      : null <-> [Login] <-> [Dashboard] <-> [Orders] <-> null

--- 3. Floyd's Tortoise & Hare Cycle Detection (O(n) Time, O(1) Space) ---
  Acyclic Check  : Cycle Entry = null
  Corrupted Loop : n4.next -> n2 injected.
  Cycle Detected : true | Loop Entry Node = [Hop-2: CoreRouter]

--- 4. Big-O Complexity Breakdown (Singly vs Doubly Linked List) ---
  +--------------------------+------------+------------+--------------+------------+
  | Operation                | SLL Time   | DLL Time   | Worst Case   | Aux Space  |
  +--------------------------+------------+------------+--------------+------------+
  | Access / Search by Index | O(n)       | O(n)       | O(n)         | O(1)       |
  | Insert / Delete at Head  | O(1)       | O(1)       | O(1)         | O(1)       |
  | Insert at Tail (w/ tail) | O(1)       | O(1)       | O(1)         | O(1)       |
  | Delete at Tail           | O(n)       | O(1)       | O(n) / O(1)  | O(1)       |
  | Insert/Delete at k-th    | O(n)       | O(n)       | O(n)         | O(1)       |
  | Floyd's Cycle Detection  | O(n)       | O(n)       | O(n)         | O(1)       |
  +--------------------------+------------+------------+--------------+------------+

==========================================================================
  MODULE 2 COMPLETE: ALL LINKED LIST MECHANICS VERIFIED
=========================================================================="""
    add_terminal_output(out_ll, title="java -cp bin week3.LinkedListDSDemo")

    add_custom_heading("3.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time", "Aux Space"],
        rows=[
            ("Access / Search by Index", "O(1) head", "O(n)", "O(n)", "O(1)"),
            ("Insert / Delete at Head", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("Delete at Tail (SLL vs DLL)", "O(1) DLL", "O(n) SLL / O(1) DLL", "O(n) SLL / O(1) DLL", "O(1)"),
            ("Floyd's Cycle Detection", "O(1) no loop", "O(n)", "O(n)", "O(1)")
        ],
        col_widths=[2.1, 1.0, 1.2, 1.2, 1.0]
    )

    # =========================================================================
    # SECTION 4: STACKS (LIFO)
    # =========================================================================
    add_custom_heading("4. Stack (LIFO) Architecture & Delimiter Validator (StackDSDemo)", level=1)

    doc.add_paragraph(
        "A Stack enforces Last-In, First-Out (LIFO) discipline: insertions (push) and removals (pop) occur strictly "
        "at one end called the top. Both array-backed and linked-node stacks expose identical O(1) abstract contracts, "
        "but differ in memory behavior: ArrayStack leverages CPU cache locality with occasional O(n) resize spikes, "
        "whereas LinkedStack guarantees strict O(1) worst-case push/pop with zero resize pauses."
    )

    add_custom_heading("4.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/StackDSDemo.java"), filename="src/week3/StackDSDemo.java")

    add_bullet_lines([
        ("Lines 32-47 (ArrayStack.push & pop)", "Expands backing Object[] when top+1 == length; on pop(), nulls out elements[top--] to eliminate reference loitering so popped objects can be reclaimed by GC."),
        ("Lines 71-84 (LinkedStack.push & pop)", "Prepends a new Node directly onto top via top = new Node<>(item, top), guaranteeing O(1) worst-case push without capacity resizing."),
        ("Lines 114-156 (validateDelimiters)", "Tracks string-literal state (ignoring escaped quotes \\\") so brackets inside JSON strings do not trigger false positives; pushes openers '(', '{', '[' and verifies exact LIFO matching on closers.")
    ])

    add_custom_heading("4.2 Verified Terminal Output", level=2)
    out_stack = """==========================================================================
  WEEK 3 - MODULE 3: STACK (LIFO) - ARRAY, LINKED & EXPRESSION VALIDATOR
==========================================================================

--- 1. Dynamic ArrayStack Mechanics (Contiguous LIFO Buffer) ---
  Top Frame (peek) : queryDatabase() | Size: 3 | Backing Capacity: 4
  Popped Frame     : queryDatabase()
  New Top (peek)   : authenticateRequest() | Size: 2

--- 2. LinkedStack Mechanics (Pointer-Based O(1) Worst-Case Push) ---
  Top Transaction  : TXN-1003 | Size: 3
  Rolled Back      : TXN-1003
  Current Top      : TXN-1002

--- 3. Enterprise Use Case: API Payload & Expression Delimiter Validator ---
  +-------------------------------------------------------------+---------+----------------------------------------------------+
  | Input Payload / Expression                                  | Status  | Diagnostic Message                                 |
  +-------------------------------------------------------------+---------+----------------------------------------------------+
  | {"orderId": 501, "items": [{"sku": "A1", "qty": 2}]}        | VALID   | All delimiters properly nested and balanced.       |
  | ((a + b) * [c - d] / {e + f})                               | VALID   | All delimiters properly nested and balanced.       |
  | {"items": [{"sku": "A1"}                                    | INVALID | Unclosed opening delimiter '[' remaining on stack. |
  | {[}]                                                        | INVALID | Mismatched '}' (expected closer for '[') at idx 2  |
  | compute((x + y))                                            | VALID   | All delimiters properly nested and balanced.       |
  +-------------------------------------------------------------+---------+----------------------------------------------------+

--- 4. Big-O Time & Space Complexity Breakdown (Stack) ---
  +------------------------+--------------+--------------+--------------+------------+
  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |
  +------------------------+--------------+--------------+--------------+------------+
  | push(item) [Array]     | O(1)         | O(1) amort.  | O(n) resize  | O(1) amort |
  | push(item) [Linked]    | O(1)         | O(1)         | O(1)         | O(1)       |
  | pop()                  | O(1)         | O(1)         | O(1)         | O(1)       |
  | peek()                 | O(1)         | O(1)         | O(1)         | O(1)       |
  | Delimiter Validator    | O(n)         | O(n)         | O(n)         | O(n)       |
  +------------------------+--------------+--------------+--------------+------------+

==========================================================================
  MODULE 3 COMPLETE: ALL STACK OPERATIONS & VALIDATOR VERIFIED
=========================================================================="""
    add_terminal_output(out_stack, title="java -cp bin week3.StackDSDemo")

    add_custom_heading("4.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time", "Aux Space"],
        rows=[
            ("push(item) [ArrayStack]", "O(1)", "O(1) amortized", "O(n) resize", "O(n) total"),
            ("push(item) [LinkedStack]", "O(1)", "O(1)", "O(1)", "O(1) per push"),
            ("pop() / peek()", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("Delimiter Validator", "O(n)", "O(n)", "O(n)", "O(n) stack depth")
        ],
        col_widths=[2.1, 1.0, 1.2, 1.2, 1.0]
    )

    # =========================================================================
    # SECTION 5: QUEUES (FIFO & CIRCULAR RING BUFFER)
    # =========================================================================
    add_custom_heading("5. Queue (FIFO) & Circular Ring Buffers (QueueDSDemo)", level=1)

    doc.add_paragraph(
        "A Queue enforces First-In, First-Out (FIFO) ordering. Implementing a queue on a linear array naively causes "
        "either O(n) left-shifting on every dequeue or unbounded front-slot waste. A Circular Array Queue solves this "
        "using modular arithmetic ((idx + 1) % capacity) so front and rear pointers wrap seamlessly around the fixed "
        "ring buffer in strictly O(1) time—a pattern used in high-throughput event rings like LMAX Disruptor."
    )

    add_custom_heading("5.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/QueueDSDemo.java"), filename="src/week3/QueueDSDemo.java")

    add_bullet_lines([
        ("Lines 36-53 (CircularArrayQueue.enqueue & dequeue)", "Advances rear = (rear + 1) % capacity on enqueue and front = (front + 1) % capacity on dequeue, clearing buffer[front] = null to avoid retaining stale GC references."),
        ("Lines 79-103 (LinkedQueue.enqueue & dequeue)", "Maintains head and tail pointers; crucially resets tail = null when head becomes null on the final dequeue so no dangling tail reference persists."),
        ("Lines 125-149 (simulateDispatchEngine)", "Models a customer support dispatcher computing exact service start time max(currentClockMin, arrivalMinute) and cumulative FIFO wait times.")
    ])

    add_custom_heading("5.2 Verified Terminal Output", level=2)
    out_queue = """==========================================================================
  WEEK 3 - MODULE 4: QUEUE (FIFO) - CIRCULAR RING BUFFER & LINKED QUEUE
==========================================================================

--- 1. Circular Array Queue Wrap-Around & Pointer Mechanics ---
  Enqueued 3 items -> front=0, rear=2, size=3
  Dequeued: REQ-01 | Dequeued: REQ-02
  After 2 dequeues -> front=2, rear=2, size=1
  Enqueued REQ-04, REQ-05 (Wrap-Around!) -> front=2, rear=0, size=3, peek=REQ-03

--- 2. Enterprise Use Case: Customer Support Ticket Dispatch Engine ---
  +-----------+---------------+---------------------------+---------+------------+-----------+
  | Ticket ID | Customer      | Issue Summary             | Arrived | Dispatched | Wait Time |
  +-----------+---------------+---------------------------+---------+------------+-----------+
  | TKT-901   | Ananya R.     | Payment Gateway Timeout   |    0 m  |       5 m  |      5 m  |
  | TKT-902   | Vikram S.     | API Key Rotation Failure  |    2 m  |       9 m  |      7 m  |
  | TKT-903   | Meera K.      | Invoice PDF Export Error  |    5 m  |      13 m  |      8 m  |
  | TKT-904   | Rohan P.      | SSO SAML Callback 403     |    7 m  |      17 m  |     10 m  |
  +-----------+---------------+---------------------------+---------+------------+-----------+

--- 3. Big-O Time & Space Complexity Breakdown (Queue) ---
  +------------------------+--------------+--------------+--------------+------------+
  | Operation              | Best Case    | Average Case | Worst Case   | Aux Space  |
  +------------------------+--------------+--------------+--------------+------------+
  | enqueue(item) [Circ.]  | O(1)         | O(1)         | O(1)         | O(1)       |
  | enqueue(item) [Linked] | O(1)         | O(1)         | O(1)         | O(1)       |
  | dequeue()              | O(1)         | O(1)         | O(1)         | O(1)       |
  | peek()                 | O(1)         | O(1)         | O(1)         | O(1)       |
  | search(target)         | O(1)         | O(n)         | O(n)         | O(1)       |
  +------------------------+--------------+--------------+--------------+------------+

==========================================================================
  MODULE 4 COMPLETE: CIRCULAR & LINKED QUEUE OPERATIONS VERIFIED
=========================================================================="""
    add_terminal_output(out_queue, title="java -cp bin week3.QueueDSDemo")

    add_custom_heading("5.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time", "Aux Space"],
        rows=[
            ("enqueue(item) [Circular / Linked]", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("dequeue()", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("peek()", "O(1)", "O(1)", "O(1)", "O(1)"),
            ("search(target)", "O(1)", "O(n)", "O(n)", "O(1)")
        ],
        col_widths=[2.1, 1.0, 1.2, 1.2, 1.0]
    )

    # =========================================================================
    # SECTION 6: BINARY SEARCH TREES (BST)
    # =========================================================================
    add_custom_heading("6. Binary Search Trees (TreeDSDemo)", level=1)

    doc.add_paragraph(
        "A Binary Search Tree (BST) organizes comparable keys hierarchically on the JVM Heap so that for every node N, "
        "all keys in N.left are strictly less than N.key and all keys in N.right are strictly greater than N.key. "
        "Balanced BSTs achieve O(log n) search and range queries, whereas sorted insertion degrades an unbalanced BST "
        "into a skewed chain of height n (O(n) worst case), motivating self-balancing trees like Red-Black Trees (java.util.TreeMap)."
    )

    add_custom_heading("6.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/TreeDSDemo.java"), filename="src/week3/TreeDSDemo.java")

    add_bullet_lines([
        ("Lines 32-47 (insertRecursive)", "Returns the updated subtree root at each recursion unwind, cleanly attaching new leaves without explicit parent pointers."),
        ("Lines 62-97 (In-Order, Pre-Order, Post-Order)", "In-Order visits Left-Root-Right to yield sorted ascending keys; Pre-Order captures structural topology; Post-Order processes children before parents."),
        ("Lines 100-124 (calculateHeight & rangeQuery)", "Height computes 1 + max(left, right); rangeQuery prunes entire subtrees whenever node.key <= minKey or node.key >= maxKey."),
        ("Lines 134-144 (isValidBST Global Bound Validator)", "Passes narrowing (minBound, maxBound) intervals using Long.MIN_VALUE/MAX_VALUE, catching deep-subtree violations (e.g., node 55 placed in left subtree of root 50).")
    ])

    add_custom_heading("6.2 Verified Terminal Output", level=2)
    out_bst = """==========================================================================
  WEEK 3 - MODULE 5: BINARY SEARCH TREE (BST), TRAVERSALS & VALIDATION
==========================================================================

--- 1. Recursive BST Insertion & Hierarchical Structure ---
  Inserted 8 catalog price nodes into BST (Root = 500: Monitor).
  Tree Height      : 4 levels
  BST Valid Check  : true

--- 2. Depth-First Traversals (In-Order, Pre-Order, Post-Order) ---
  In-Order (Sorted): [120, 250, 300, 350, 500, 620, 750, 900]
  Pre-Order (Root) : [500, 250, 120, 350, 300, 750, 620, 900]
  Post-Order (Leaf): [120, 300, 350, 250, 620, 900, 750, 500]

--- 3. Enterprise Use Case: Indexed Search & Price Range Query ---
  Search key=620   : FOUND -> Tablet
  Search key=410   : NOT FOUND
  Range [250..650] : [250 (Keyboard), 300 (Webcam), 350 (Headset), 500 (Monitor), 620 (Tablet)]

--- 4. Placement Trap: Deep Subtree BST Violation Check ---
  Injected node(55) as right child of node(30) under root(50).
  Naive local check would pass (30 < 55), but Global Bound Validator returns: false

--- 5. Big-O Time & Space Complexity Breakdown (BST) ---
  +------------------------+--------------+--------------+--------------+------------+
  | Operation              | Best Case    | Average Case | Worst (Skew) | Aux Space  |
  +------------------------+--------------+--------------+--------------+------------+
  | Search / Lookup        | O(1) root    | O(log n)     | O(n)         | O(h) stack |
  | Recursive Insert       | O(1) root    | O(log n)     | O(n)         | O(h) stack |
  | In/Pre/Post Traversal  | O(n)         | O(n)         | O(n)         | O(h) stack |
  | Calculate Height       | O(n)         | O(n)         | O(n)         | O(h) stack |
  | Validate BST Property  | O(n)         | O(n)         | O(n)         | O(h) stack |
  +------------------------+--------------+--------------+--------------+------------+

==========================================================================
  MODULE 5 COMPLETE: ALL BST TRAVERSALS & INVARIANTS VERIFIED
=========================================================================="""
    add_terminal_output(out_bst, title="java -cp bin week3.TreeDSDemo")

    add_custom_heading("6.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time (Skewed)", "Aux Space"],
        rows=[
            ("Search / Lookup", "O(1) root", "O(log n)", "O(n)", "O(h) call stack"),
            ("Insert", "O(1) root", "O(log n)", "O(n)", "O(h) call stack"),
            ("DFS Traversals / Height", "O(n)", "O(n)", "O(n)", "O(h) call stack"),
            ("Range Query [min..max]", "O(log n + k)", "O(log n + k)", "O(n)", "O(h + k)")
        ],
        col_widths=[2.1, 1.0, 1.1, 1.3, 1.0]
    )

    # =========================================================================
    # SECTION 7: HASHMAPS & SEPARATE CHAINING
    # =========================================================================
    add_custom_heading("7. HashMap Mechanics, Collisions & Rehashing (HashMapDSDemo)", level=1)

    doc.add_paragraph(
        "A HashMap delivers O(1) average-case key-value lookup by compressing arbitrary keys into array indices via a "
        "hash function. Because distinct keys can map to the same bucket (pigeonhole principle), collisions are resolved "
        "via Separate Chaining—each bucket stores a linked list of EntryNode objects. As load factor (size / capacity) "
        "exceeds 0.75, rehashing doubles the bucket array and redistributes entries to keep chains short."
    )

    add_custom_heading("7.1 Key Implementation & Line-by-Line Analysis", level=2)
    add_code_block(load_source("src/week3/HashMapDSDemo.java"), filename="src/week3/HashMapDSDemo.java")

    add_bullet_lines([
        ("Lines 53-62 (computeHash & bucketIndex)", "Applies bit-mixing h ^ (h >>> 16) to propagate high-bit entropy downward, then masks with 0x7FFFFFFF to guarantee a non-negative bucket index even for Integer.MIN_VALUE hashes."),
        ("Lines 64-85 (put & Separate Chaining)", "Scans the bucket chain for an existing matching key (updating in place if found); otherwise prepends a new EntryNode in O(1) and triggers rehash() when load factor > 0.75."),
        ("Lines 98-119 (remove)", "Tracks prev and curr pointers during chain traversal to unlink the matched EntryNode cleanly without breaking downstream collision nodes."),
        ("Lines 122-141 (rehash)", "Doubles capacity (4 -> 8 -> 16) and re-threads existing EntryNode objects directly into the new bucket array without allocating duplicate node objects."),
        ("Lines 185-187 (Deterministic Collision Verification)", "Inserts 'FB' and 'Ea' (which share identical Java String hashCode() == 2236) to prove separate chaining links multiple collided entries inside Bucket[12].")
    ])

    add_custom_heading("7.2 Verified Terminal Output", level=2)
    out_hm = """==========================================================================
  WEEK 3 - MODULE 6: HASHMAP - BUCKETS, CHAINING, LOAD FACTOR & REHASHING
==========================================================================

--- 1. Enterprise Session Cache: Insertion, Collisions & Rehashing ---
  [Rehash Triggered] Load Factor 1.00 > 0.75 | Capacity 4 -> 8
  [Rehash Triggered] Load Factor 0.88 > 0.75 | Capacity 8 -> 16

  Map Telemetry -> Size: 9 | Capacity: 16 | Load Factor: 0.56 | Collisions: 2 | Rehashes: 2

--- 2. Heap Bucket Array & Separate Chaining Topology ---
    Bucket[00]: <empty>
    Bucket[01]: <empty>
    Bucket[02]: <empty>
    Bucket[03]: <empty>
    Bucket[04]: <empty>
    Bucket[05]: <empty>
    Bucket[06]: <empty>
    Bucket[07]: <empty>
    Bucket[08]: [usr-102=ACTIVE:Analyst]
    Bucket[09]: [usr-103=ACTIVE:Operator]
    Bucket[10]: <empty>
    Bucket[11]: [usr-101=ACTIVE:Admin]
    Bucket[12]: [Ea=ACTIVE:CollisionNodeB] -> [FB=ACTIVE:CollisionNodeA] -> [usr-106=ACTIVE:Manager]
    Bucket[13]: [usr-107=ACTIVE:Guest]
    Bucket[14]: [usr-104=ACTIVE:Auditor]
    Bucket[15]: [usr-105=ACTIVE:Engineer]

--- 3. O(1) Lookup, Key Update & Node Unlinking (remove) ---
  get('usr-104')          : ACTIVE:Auditor
  get('usr-104') [updated]: LOCKED:Auditor
  remove('usr-102')       : Removed [ACTIVE:Analyst] | New Size = 8
  get('usr-102')          : null

--- 4. Big-O Time & Space Complexity Breakdown (HashMap) ---
  +------------------------+--------------+--------------+--------------+------------+
  | Operation              | Best Case    | Average Case | Worst (Coll) | Aux Space  |
  +------------------------+--------------+--------------+--------------+------------+
  | put(key, val)          | O(1)         | O(1) amort.  | O(n)         | O(1)       |
  | get(key)               | O(1)         | O(1)         | O(n)         | O(1)       |
  | remove(key)            | O(1)         | O(1)         | O(n)         | O(1)       |
  | rehash()               | O(n)         | O(n)         | O(n)         | O(n)       |
  +------------------------+--------------+--------------+--------------+------------+

==========================================================================
  MODULE 6 COMPLETE: ALL HASHMAP & REHASHING MECHANICS VERIFIED
=========================================================================="""
    add_terminal_output(out_hm, title="java -cp bin week3.HashMapDSDemo")

    add_custom_heading("7.3 Big-O Complexity Summary", level=2)
    add_styled_table(
        headers=["Operation", "Best Time", "Average Time", "Worst Time (All Collide)", "Aux Space"],
        rows=[
            ("put(key, value)", "O(1)", "O(1) amortized", "O(n) [O(log n) in JDK8+ tree]", "O(1)"),
            ("get(key)", "O(1)", "O(1)", "O(n)", "O(1)"),
            ("remove(key)", "O(1)", "O(1)", "O(n)", "O(1)"),
            ("rehash()", "O(n)", "O(n)", "O(n)", "O(n)")
        ],
        col_widths=[2.1, 0.9, 1.2, 1.5, 0.8]
    )

    # =========================================================================
    # SECTION 8: INTEGRATED CAPSTONE PROGRAM
    # =========================================================================
    add_custom_heading("8. Integrated Capstone: E-Commerce Inventory & Order Engine", level=1)

    doc.add_paragraph(
        "To synthesize all six data structures in an enterprise architecture, ECommerceInventoryOrderEngine.java "
        "models a high-throughput fulfillment pipeline where every subsystem is powered by its optimal custom-built "
        "data structure:"
    )

    add_bullet_lines([
        ("Fixed Array (Batch Inventory Audit)", "Stores contiguous warehouse bin records for O(n) valuation and unit totals."),
        ("Custom HashMap (SkuHashMap)", "Indexes every SKU for O(1) instant lookup during order validation and rollbacks."),
        ("Binary Search Tree (PriceIndexBST)", "Organizes products by unit price to execute pruned range queries ($100-$260)."),
        ("Circular Queue (OrderPipelineQueue)", "Processes incoming customer orders in strict FIFO arrival sequence."),
        ("Linked Stack (RollbackStack)", "Captures pre-mutation snapshots so promotional cuts or orders revert cleanly in LIFO order."),
        ("Singly Linked List (ShipmentRouteList)", "Tracks transit checkpoints and splices mid-route customs reroutes dynamically.")
    ])

    add_custom_heading("8.1 Capstone Source Code (ECommerceInventoryOrderEngine.java)", level=2)
    add_code_block(load_source("src/week3/ECommerceInventoryOrderEngine.java"),
                   filename="src/week3/ECommerceInventoryOrderEngine.java")

    add_custom_heading("8.2 Capstone Verified Terminal Execution Output", level=2)
    out_capstone = """====================================================================================
     YUVAINTERN E-COMMERCE FULFILLMENT & INVENTORY ENGINE (WEEK 3 CAPSTONE)
  Integrated Architecture: HashMap + BST + Queue + Stack + LinkedList + Array
====================================================================================

--- 1. Fixed Array: Warehouse Batch Inventory Ingestion & Audit ---
  +----------+--------------------------+------------+----------+------------------+
  | SKU      | Product Name             | Unit Price | In Stock | Batch Valuation  |
  +----------+--------------------------+------------+----------+------------------+
  | SKU-101  | Mechanical Keyboard Pro  | $   129.99 |       45 | $        5849.55 |
  | SKU-102  | USB-C Docking Hub        | $    79.50 |       80 | $        6360.00 |
  | SKU-103  | 4K IPS Studio Monitor    | $   449.00 |       18 | $        8082.00 |
  | SKU-104  | Ergonomic Vertical Mouse | $    54.00 |       60 | $        3240.00 |
  | SKU-105  | NVMe Gen4 SSD 2TB        | $   189.00 |       32 | $        6048.00 |
  | SKU-106  | Noise-Cancel Headset     | $   249.50 |       25 | $        6237.50 |
  +----------+--------------------------+------------+----------+------------------+
  Array Audit Summary: 6 SKUs | 260 Total Units | Total Valuation: $35,817.05

--- 2. Custom HashMap: O(1) Instant SKU Catalog Lookup ---
  HashMap Telemetry -> Entries: 6 | Buckets: 8 | Load Factor: 0.75
  Lookup [SKU-103] -> HIT: 4K IPS Studio Monitor    | Price: $449.00 | Stock: 18
  Lookup [SKU-105] -> HIT: NVMe Gen4 SSD 2TB        | Price: $189.00 | Stock: 32
  Lookup [SKU-999] -> MISS: SKU not registered in catalog.

--- 3. Binary Search Tree: Price-Sorted Catalog & Range Query [$100 - $260] ---
  +----------+--------------------------+------------+
  | SKU      | Product Name             | Price      |
  +----------+--------------------------+------------+
  | SKU-101  | Mechanical Keyboard Pro  | $   129.99 |
  | SKU-105  | NVMe Gen4 SSD 2TB        | $   189.00 |
  | SKU-106  | Noise-Cancel Headset     | $   249.50 |
  +----------+--------------------------+------------+

--- 4. Circular Queue: FIFO Order Fulfillment Pipeline ---
  +----------+--------------+----------+-----+------------+--------------+
  | Order ID | Customer     | SKU      | Qty | Status     | Post-Stock   |
  +----------+--------------+----------+-----+------------+--------------+
  | ORD-5001 | Aarav Mehta  | SKU-101  |   3 | FULFILLED  |           42 |
  | ORD-5002 | Priya Nair   | SKU-103  |   2 | FULFILLED  |           16 |
  | ORD-5003 | Karthik Rao  | SKU-105  |  40 | BACKORDER  |           32 |
  | ORD-5004 | Sneha Iyer   | SKU-102  |   5 | FULFILLED  |           75 |
  +----------+--------------+----------+-----+------------+--------------+

--- 5. Linked Stack: LIFO Transactional Undo / Rollback History ---
  Applied Promo Price Cut on SKU-103 -> New Price: $399.00 (Undo Stack Depth: 4)
  [ROLLBACK] Reverted PROMO_PRICE_CUT  on SKU-103 -> Restored Stock: 16 | Price: $449.00
  [ROLLBACK] Reverted FULFILL_ORD-5004 on SKU-102 -> Restored Stock: 80 | Price: $79.50

--- 6. Singly Linked List: Dynamic Shipment Transit Checkpoints ---
  Transit Route for [SHP-ORD-5001] (4 hops):
    Hop 1: Bengaluru Central Hub          @ 2026-11-10 08:15  -->
    Hop 2: Hyderabad Regional Sort        @ 2026-11-10 14:40  -->
    Hop 3: Pune Customs Clearance Hub     @ 2026-11-10 21:05  -->
    Hop 4: Mumbai Final Mile Depot        @ 2026-11-11 06:30  [DELIVERED]

====================================================================================
  WEEK 3 CAPSTONE EXECUTION COMPLETE: ALL 6 DATA STRUCTURES OPERATIONAL
===================================================================================="""
    add_terminal_output(out_capstone, title="java -cp bin week3.ECommerceInventoryOrderEngine")

    # =========================================================================
    # SECTION 9: COMPREHENSIVE COMPLEXITY MATRIX & VERIFICATION
    # =========================================================================
    add_custom_heading("9. Comprehensive Data Structures Complexity Matrix", level=1)

    doc.add_paragraph(
        "The master matrix below compares all implemented data structures across Average and Worst-Case Time "
        "Complexity for Access, Search, Insertion, and Deletion, alongside auxiliary space overhead:"
    )

    add_styled_table(
        headers=["Data Structure", "Access (Avg/Worst)", "Search (Avg/Worst)", "Insert (Avg/Worst)", "Delete (Avg/Worst)", "Space"],
        rows=[
            ("Static / Dynamic Array", "O(1) / O(1)", "O(n) / O(n)", "O(1)* / O(n)", "O(n) / O(n)", "O(n)"),
            ("Singly Linked List", "O(n) / O(n)", "O(n) / O(n)", "O(1) ends / O(n)", "O(1) head / O(n)", "O(n)"),
            ("Doubly Linked List", "O(n) / O(n)", "O(n) / O(n)", "O(1) ends / O(n)", "O(1) ends / O(n)", "O(n)"),
            ("Stack (Array / Linked)", "O(1) top / O(n)", "O(n) / O(n)", "O(1)* / O(1) linked", "O(1) / O(1)", "O(n)"),
            ("Queue (Circular / Linked)", "O(1) front / O(n)", "O(n) / O(n)", "O(1) / O(1)", "O(1) / O(1)", "O(n)"),
            ("Binary Search Tree (BST)", "O(log n) / O(n)", "O(log n) / O(n)", "O(log n) / O(n)", "O(log n) / O(n)", "O(n)"),
            ("HashMap (Chaining)", "N/A (Keyed)", "O(1) / O(n)", "O(1)* / O(n)", "O(1) / O(n)", "O(n)")
        ],
        col_widths=[1.5, 1.05, 1.05, 1.1, 1.1, 0.7],
        font_size=8.5
    )

    doc.add_paragraph(
        "*Note: Dynamic Array append, ArrayStack push, and HashMap put achieve O(1) amortized time due to geometric "
        "2x capacity resizing, while worst-case single-operation spikes reach O(n) during array copy or rehash."
    )

    add_custom_heading("9.1 Verification Summary (Oracle JDK 22.0.1)", level=2)
    add_styled_table(
        headers=["Class Name", "Source Path", "Compiler", "Exit", "Status"],
        rows=[
            ("ArraysDSDemo", "src/week3/ArraysDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("LinkedListDSDemo", "src/week3/LinkedListDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("StackDSDemo", "src/week3/StackDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("QueueDSDemo", "src/week3/QueueDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("TreeDSDemo", "src/week3/TreeDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("HashMapDSDemo", "src/week3/HashMapDSDemo.java", "javac 22.0.1", "0", "PASSED (100%)"),
            ("ECommerceInventoryOrderEngine", "src/week3/ECommerceInventoryOrderEngine.java", "javac 22.0.1", "0", "PASSED (100%)")
        ],
        col_widths=[2.0, 2.0, 0.9, 0.5, 1.1],
        font_size=9.0,
        highlight_last_col=True
    )

    # =========================================================================
    # SECTION 10: TOP 8 PLACEMENT INTERVIEW QUESTIONS & TECHNICAL EDGE CASES
    # =========================================================================
    add_custom_heading("10. Top 8 Placement Interview DS Questions & Edge Cases", level=1)

    qa_list = [
        ("Q1: Why is Dynamic Array append O(1) amortized even though resizing copies O(n) elements?",
         "Using the potential/accounting method: when capacity doubles from C to 2C, C cheap O(1) appends occur before "
         "the next resize copy of 2C elements. Total copy work across n insertions forms a geometric series (n + n/2 + n/4 + ... < 2n), "
         "averaging to <= 3 operations per append, hence O(1) amortized."),

        ("Q2: Why does Floyd's Tortoise and Hare algorithm guarantee finding the exact cycle entry point?",
         "Let L be the distance from head to cycle start, k the offset from cycle start to meeting point, and C the cycle length. "
         "Since fast travels 2x distance of slow: 2(L + k) = L + k + mC, which simplifies to L = mC - k. "
         "Resetting slow to head and advancing both 1 step at a time makes them collide at the cycle entry after L steps."),

        ("Q3: What is 'reference loitering' in ArrayStack/CircularArrayQueue and why must popped slots be nulled?",
         "Even after decrementing top or incrementing front, the underlying Object[] still holds a reference to the popped object. "
         "Because the array itself is reachable from a live GC root, the JVM Garbage Collector cannot reclaim the popped object until "
         "that slot is explicitly set to null (memory leak via obsolete object references, Effective Java Item 7)."),

        ("Q4: Why does checking only (node.left.val < node.val < node.right.val) fail to validate a BST?",
         "It only enforces local parent-child ordering and misses ancestor bounds. For example, placing 55 as the right child of 30 "
         "inside the left subtree of root 50 satisfies 30 < 55 locally, yet violates the BST invariant because 55 > 50. "
         "Correct validation narrows recursive (min, max) bounds down the tree."),

        ("Q5: What happens in a HashMap if you mutate a key object's fields after inserting it?",
         "Mutating fields used in hashCode() changes the computed bucket index while the EntryNode remains stranded in the old bucket. "
         "Subsequent get(key) or remove(key) calls compute the new bucket index and return null, effectively losing the entry and leaking memory. "
         "Therefore, HashMap keys should always be immutable (e.g., String, Integer, final record)."),

        ("Q6: How did Java 8 upgrade HashMap worst-case performance against hash-flooding DoS attacks?",
         "In Java 7, colliding keys formed a pure linked list, allowing attackers to force O(n) lookup degradation. "
         "In Java 8+, when a single bucket's chain length reaches TREEIFY_THRESHOLD (8) and table capacity >= 64, "
         "the bucket converts from a LinkedList into a balanced Red-Black Tree, bounding worst-case lookup to O(log n)."),

        ("Q7: When should you choose a Circular Array Queue over a LinkedList Queue in low-latency Java systems?",
         "When the maximum queue bound is known and predictability matters: a circular array pre-allocates a single contiguous block "
         "with zero per-enqueue Heap allocations (avoiding GC pressure) and provides superior CPU L1/L2 cache spatial locality."),

        ("Q8: Why does In-Order traversal of a BST produce sorted output, and what is its space complexity?",
         "Because the BST invariant guarantees all left-subtree keys < root < all right-subtree keys, visiting Left -> Root -> Right "
         "emits keys in strictly non-decreasing order. Recursive In-Order traversal takes O(n) time and O(h) auxiliary stack space "
         "(O(log n) balanced, O(n) worst-case skewed).")
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

    add_custom_heading("10.1 References & Technical Standards", level=2)
    doc.add_paragraph(
        "1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). Introduction to Algorithms (4th Edition). MIT Press.\n"
        "2. Bloch, J. (2018). Effective Java (3rd Edition). Addison-Wesley Professional. [Items 7, 11, 17].\n"
        "3. Sedgewick, R., & Wayne, K. (2011). Algorithms (4th Edition). Addison-Wesley.\n"
        "4. Oracle Corporation. (2024). Java SE 22 Platform API & OpenJDK HashMap Implementation Notes.\n"
        "5. YuvaIntern Virtual Internship Curriculum - Week 3: Data Structures in Java."
    )

    os.makedirs("docs", exist_ok=True)
    doc_path1 = os.path.abspath("docs/Task3_Data_Structures_in_Java.docx")
    doc_path2 = os.path.abspath("Task3_Data_Structures_in_Java.docx")
    doc.save(doc_path1)
    doc.save(doc_path2)
    print(f"Week 3 Report generated successfully:\n1. {doc_path1}\n2. {doc_path2}")


if __name__ == "__main__":
    create_document()
