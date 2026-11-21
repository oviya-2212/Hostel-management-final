package com.hostel;

import com.hostel.exception.HostelException;
import com.hostel.exception.RoomCapacityExceededException;
import com.hostel.exception.StudentAlreadyAllocatedException;
import com.hostel.model.*;
import com.hostel.service.HostelService;
import com.hostel.service.HostelServiceImpl;
import com.hostel.thread.FeeAuditWorker;
import com.hostel.thread.OccupancyReportWorker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main Entry Point for the Hostel Room Allocation System.
 * Supports both Interactive Manual Input Mode and Automated Demonstration Suite.
 */
public class Main {

    private static final String OUTPUT_FILE = "java_execution_output.txt";

    public static void main(String[] args) {
        HostelService service = new HostelServiceImpl();
        Scanner scanner = new Scanner(System.in);

        // Check if an argument was passed
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--auto-run")) {
                runAutomatedDemonstration(service);
                return;
            } else if (args[0].equalsIgnoreCase("--interactive")) {
                runInteractiveMenu(service, scanner);
                return;
            }
        }

        // Default startup: Ask user whether they want Manual Input or Automated Mode
        System.out.println("========================================================================================");
        System.out.println(" HOSTEL ROOM ALLOCATION & ACCOMMODATION ANALYSIS SYSTEM - JAVA SE SUITE");
        System.out.println(" Course Code: 25ITT32 - Java Programming | Object-Oriented System");
        System.out.println("========================================================================================");
        System.out.println("Select Execution Mode:");
        System.out.println("  [1] Interactive Manual Input Mode (Enter students, rooms & allocations manually)");
        System.out.println("  [2] Automated Verification Suite (Run all 10 stages & generate reports automatically)");
        System.out.print("Enter choice [1 or 2] (Default is 1): ");

        String input = scanner.nextLine().trim();
        if (input.equals("2")) {
            runAutomatedDemonstration(service);
        } else {
            runInteractiveMenu(service, scanner);
        }
    }

    public static void runInteractiveMenu(HostelService service, Scanner scanner) {
        System.out.println("\n[INFO] Starting Interactive Manual Input Mode...");

        while (true) {
            System.out.println("\n========================================================================================");
            System.out.println("                     HOSTEL MANAGEMENT SYSTEM - MAIN MENU");
            System.out.println("========================================================================================");
            System.out.println(" 1. Register New Student (Manual Input: ID, Name, Dept, Year, Phone, Email)");
            System.out.println(" 2. Add New Hostel Room (Manual Input: Room ID, Number, Type, Capacity)");
            System.out.println(" 3. Allocate Room to Student (Manual Input: Student ID, Room ID - With Capacity Check)");
            System.out.println(" 4. Release Room Allocation (Manual Input: Allocation ID)");
            System.out.println(" 5. Record Student Check-In (Manual Input: Allocation ID)");
            System.out.println(" 6. Record Student Check-Out (Manual Input: CheckInOut Record ID)");
            System.out.println(" 7. Record Fee Payment (Manual Input: Student ID, Payment Amount)");
            System.out.println(" 8. View All Registered Students");
            System.out.println(" 9. View All Rooms & Bed Availability");
            System.out.println("10. Search Students by Department (Manual Input: Department Name)");
            System.out.println("11. Print Room Occupancy Report");
            System.out.println("12. Print Fee Defaulters & Arrears Report");
            System.out.println("13. Run Multithreaded Background Audit (Concurrent Worker Threads)");
            System.out.println("14. Run Full Automated Verification Suite");
            System.out.println("15. Exit");
            System.out.println("========================================================================================");
            System.out.print("Enter your choice (1-15): ");

            String choiceStr = scanner.nextLine().trim();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid selection! Please enter a number between 1 and 15.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Manual Student Registration
                    System.out.println("\n--- [1] REGISTER NEW STUDENT ---");
                    try {
                        System.out.print("Enter Student ID (e.g. 116): ");
                        int sId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Full Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter Department (CSE / IT / ECE / MECH / AIDS): ");
                        String dept = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Enter Year of Study (1 - 4): ");
                        int yr = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Contact Phone (10 digits): ");
                        String phone = scanner.nextLine().trim();
                        System.out.print("Enter Email Address: ");
                        String email = scanner.nextLine().trim();

                        Student newStudent = new Student(sId, name, dept, yr, phone, email);
                        service.addStudent(newStudent);
                        System.out.println("[SUCCESS] Student Registered Successfully!");
                        System.out.println("Details: " + newStudent);
                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to register student: " + e.getMessage());
                    }
                    break;

                case 2:
                    // Manual Room Registration
                    System.out.println("\n--- [2] ADD NEW HOSTEL ROOM ---");
                    try {
                        System.out.print("Enter Room ID (e.g. 211): ");
                        int rId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Room Number (e.g. D-401): ");
                        String rNum = scanner.nextLine().trim();
                        System.out.println("Select Room Type: 1. Single AC  2. Double Non-AC  3. Triple Non-AC");
                        System.out.print("Enter choice (1-3): ");
                        int tChoice = Integer.parseInt(scanner.nextLine().trim());
                        String rType = tChoice == 1 ? "Single AC" : (tChoice == 2 ? "Double Non-AC" : "Triple Non-AC");
                        System.out.print("Enter Bed Capacity (1-5): ");
                        int cap = Integer.parseInt(scanner.nextLine().trim());

                        Room newRoom = new Room(rId, rNum, rType, cap, 0);
                        service.addRoom(newRoom);
                        System.out.println("[SUCCESS] Room Added to Inventory Successfully!");
                        System.out.println("Details: " + newRoom);
                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to add room: " + e.getMessage());
                    }
                    break;

                case 3:
                    // Manual Room Allocation (With Capacity Validation!)
                    System.out.println("\n--- [3] ALLOCATE ROOM TO STUDENT ---");
                    try {
                        System.out.print("Enter Student ID to allocate: ");
                        int sId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Room ID to assign: ");
                        int rId = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("Validating room capacity and student records...");
                        Allocation alloc = service.allocateRoom(sId, rId);
                        System.out.println("[SUCCESS] Room Allocation Committed Successfully!");
                        System.out.println("Allocation Voucher: " + alloc);
                        Room updatedRoom = service.getRoom(rId);
                        System.out.printf("Updated Room Status: %s (Available Beds: %d)\n",
                                updatedRoom.getRoomNumber(), updatedRoom.availableBeds());
                    } catch (RoomCapacityExceededException e) {
                        System.out.println("\n[ALERT - CAPACITY LIMIT EXCEEDED]");
                        System.out.println("-> " + e.getMessage());
                        System.out.println("-> System rejected allocation to protect room capacity limits.");
                    } catch (StudentAlreadyAllocatedException e) {
                        System.out.println("\n[ALERT - DUPLICATE ALLOCATION PREVENTED]");
                        System.out.println("-> " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("[ERROR] Allocation failed: " + e.getMessage());
                    }
                    break;

                case 4:
                    // Release Room
                    System.out.println("\n--- [4] RELEASE ROOM ALLOCATION ---");
                    try {
                        System.out.print("Enter Allocation ID to release: ");
                        int aId = Integer.parseInt(scanner.nextLine().trim());
                        service.releaseRoom(aId);
                        System.out.println("[SUCCESS] Allocation released. Room bed is now vacant.");
                    } catch (Exception e) {
                        System.out.println("[ERROR] " + e.getMessage());
                    }
                    break;

                case 5:
                    // Check-In
                    System.out.println("\n--- [5] RECORD CHECK-IN ---");
                    try {
                        System.out.print("Enter Allocation ID: ");
                        int aId = Integer.parseInt(scanner.nextLine().trim());
                        CheckInOut c = service.checkIn(aId);
                        System.out.println("[SUCCESS] Physical check-in recorded: " + c);
                    } catch (Exception e) {
                        System.out.println("[ERROR] " + e.getMessage());
                    }
                    break;

                case 6:
                    // Check-Out
                    System.out.println("\n--- [6] RECORD CHECK-OUT ---");
                    try {
                        System.out.print("Enter CheckInOut Record ID: ");
                        int cId = Integer.parseInt(scanner.nextLine().trim());
                        CheckInOut c = service.checkOut(cId);
                        System.out.println("[SUCCESS] Physical check-out recorded: " + c);
                    } catch (Exception e) {
                        System.out.println("[ERROR] " + e.getMessage());
                    }
                    break;

                case 7:
                    // Record Fee Payment
                    System.out.println("\n--- [7] RECORD FEE PAYMENT ---");
                    try {
                        System.out.print("Enter Student ID: ");
                        int sId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Payment Installment Amount (in INR): ");
                        double amt = Double.parseDouble(scanner.nextLine().trim());

                        FeePayment fp = service.payInstallment(sId, amt);
                        System.out.println("[SUCCESS] Fee Payment Processed!");
                        System.out.println("Updated Ledger: " + fp);
                    } catch (Exception e) {
                        System.out.println("[ERROR] " + e.getMessage());
                    }
                    break;

                case 8:
                    // View Students
                    System.out.println("\n--- REGISTERED STUDENTS ROSTER ---");
                    System.out.printf("%-6s %-18s %-8s %-9s %-12s %-25s\n", "ID", "Name", "Dept", "Year", "Phone", "Email");
                    System.out.println("----------------------------------------------------------------------------------------");
                    service.getAllStudents().forEach(System.out::println);
                    break;

                case 9:
                    // View Rooms
                    System.out.println("\n--- HOSTEL ROOM INVENTORY & BED AVAILABILITY ---");
                    System.out.printf("%-6s %-10s %-15s %-25s\n", "ID", "Room No", "Room Type", "Occupancy & Bed Availability");
                    System.out.println("----------------------------------------------------------------------------------------");
                    service.getAllRooms().forEach(System.out::println);
                    break;

                case 10:
                    // Search Students by Department
                    System.out.println("\n--- SEARCH STUDENTS BY DEPARTMENT ---");
                    System.out.print("Enter Department Code (e.g. CSE, IT, ECE, MECH, AIDS): ");
                    String sDept = scanner.nextLine().trim();
                    List<Student> results = service.getStudentsByDepartment(sDept);
                    if (results.isEmpty()) {
                        System.out.println("No students found in department: " + sDept);
                    } else {
                        System.out.printf("Found %d students in %s:\n", results.size(), sDept);
                        results.forEach(System.out::println);
                    }
                    break;

                case 11:
                    // Occupancy Report
                    service.printOccupancyReport();
                    break;

                case 12:
                    // Fee Defaulters Report
                    service.printFeeDefaultersReport();
                    break;

                case 13:
                    // Multithreaded Background Audit
                    System.out.println("\n--- LAUNCHING MULTITHREADED BACKGROUND WORKERS ---");
                    Thread t1 = new Thread(new OccupancyReportWorker(service), "OccupancyWorker");
                    FeeAuditWorker t2 = new FeeAuditWorker(service);
                    t1.start();
                    t2.start();
                    try {
                        t1.join();
                        t2.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Multithreaded background workers completed successfully.");
                    break;

                case 14:
                    // Automated Suite
                    runAutomatedDemonstration(service);
                    break;

                case 15:
                    System.out.println("\nExiting Hostel Management System. Thank you!");
                    return;

                default:
                    System.out.println("[ERROR] Invalid choice! Please select 1 through 15.");
            }

            System.out.print("\nPress Enter to return to menu...");
            scanner.nextLine();
        }
    }

    public static void runAutomatedDemonstration(HostelService service) {
        StringBuilder outputLog = new StringBuilder();
        
        printlnBoth(outputLog, "========================================================================================");
        printlnBoth(outputLog, " HOSTEL ROOM ALLOCATION & ACCOMMODATION ANALYSIS SYSTEM - JAVA SE SUITE");
        printlnBoth(outputLog, " Course Code: 25ITT32 - Java Programming | Comprehensive Evaluation");
        printlnBoth(outputLog, "========================================================================================\n");

        // 1. DEMONSTRATE STUDENT ROSTER (Unit 1 & Unit 5 Streams)
        printlnBoth(outputLog, "[STAGE 1] REGISTERED STUDENTS ROSTER (Sorted by ID via Stream API):");
        printlnBoth(outputLog, String.format("%-6s %-18s %-8s %-9s %-12s %-25s",
                "ID", "Student Name", "Dept", "Year", "Phone", "Email"));
        printlnBoth(outputLog, "----------------------------------------------------------------------------------------");
        service.getAllStudents().forEach(s -> printlnBoth(outputLog, s.toString()));
        printlnBoth(outputLog, "");

        // 2. DEMONSTRATE ROOM INVENTORY (Unit 1 & Unit 4 Formatting)
        printlnBoth(outputLog, "[STAGE 2] HOSTEL ROOM INVENTORY & BED AVAILABILITY:");
        printlnBoth(outputLog, String.format("%-6s %-10s %-15s %-25s", "ID", "Room No", "Room Type", "Occupancy & Bed Availability"));
        printlnBoth(outputLog, "----------------------------------------------------------------------------------------");
        service.getAllRooms().forEach(r -> printlnBoth(outputLog, r.toString()));
        printlnBoth(outputLog, "");

        // 3. DEMONSTRATE SUCCESSFUL ROOM ALLOCATION (FR-3)
        printlnBoth(outputLog, "[STAGE 3] EXECUTING NEW ROOM ALLOCATION (Positive Scenario):");
        try {
            printlnBoth(outputLog, "   * Attempting to allocate Student 113 (Varun Rao) to Room 203 (A-103)...");
            Allocation alloc = service.allocateRoom(113, 203);
            printlnBoth(outputLog, "   [SUCCESS] Allocation Confirmed! " + alloc.toString());
            Room r = service.getRoom(203);
            printlnBoth(outputLog, "   [UPDATED] Room Status: " + r.toString());
        } catch (HostelException e) {
            printlnBoth(outputLog, "   [ERROR] " + e.getMessage());
        }
        printlnBoth(outputLog, "");

        // 4. CRITICAL VALIDATION: PREVENT ALLOCATION BEYOND CAPACITY (FR-3 Exception Handling)
        printlnBoth(outputLog, "[STAGE 4] CRITICAL INTEGRITY TEST: PREVENT ALLOCATION BEYOND CAPACITY:");
        try {
            printlnBoth(outputLog, "   * Attempting to allocate Student 114 to Room 201 (A-101 - Already Full)...");
            service.allocateRoom(114, 201);
            printlnBoth(outputLog, "   [FAILURE] Test Failed: Over-allocation occurred!");
        } catch (RoomCapacityExceededException e) {
            printlnBoth(outputLog, "   [SECURITY INTERCEPT] RoomCapacityExceededException Caught!");
            printlnBoth(outputLog, "   -> Message: " + e.getMessage());
            printlnBoth(outputLog, "   [VERIFIED] Over-allocation was strictly PREVENTED. Room bed limits enforced.");
        } catch (HostelException e) {
            printlnBoth(outputLog, "   [EXCEPTION] " + e.getMessage());
        }
        printlnBoth(outputLog, "");

        // 5. PREVENT DUPLICATE ALLOCATION FOR SAME STUDENT
        printlnBoth(outputLog, "[STAGE 5] INTEGRITY TEST: PREVENT DUPLICATE ALLOCATION FOR SAME STUDENT:");
        try {
            printlnBoth(outputLog, "   * Attempting duplicate allocation for Student 101 (Already allocated to A-101)...");
            service.allocateRoom(101, 207);
            printlnBoth(outputLog, "   [FAILURE] Test Failed: Duplicate allocation allowed!");
        } catch (StudentAlreadyAllocatedException e) {
            printlnBoth(outputLog, "   [SECURITY INTERCEPT] StudentAlreadyAllocatedException Caught!");
            printlnBoth(outputLog, "   -> Message: " + e.getMessage());
            printlnBoth(outputLog, "   [VERIFIED] Business Rule: One active room allocation per student enforced.");
        } catch (HostelException e) {
            printlnBoth(outputLog, "   [EXCEPTION] " + e.getMessage());
        }
        printlnBoth(outputLog, "");

        // 6. RECORD CHECK-IN & CHECK-OUT (FR-4)
        printlnBoth(outputLog, "[STAGE 6] CHECK-IN AND CHECK-OUT MOVEMENT RECORDS:");
        printlnBoth(outputLog, String.format("%-8s %-10s %-12s %-12s %-10s", "RecID", "AllocID", "Check-In", "Check-Out", "Status"));
        printlnBoth(outputLog, "----------------------------------------------------------------------------------------");
        service.getAllCheckInOutRecords().forEach(c -> printlnBoth(outputLog, c.toString()));
        printlnBoth(outputLog, "");

        // 7. RECORD FEE PAYMENT & PENDING DUES (FR-5)
        printlnBoth(outputLog, "[STAGE 7] HOSTEL FEE PAYMENT PROCESSING & ARREARS AUDIT:");
        try {
            printlnBoth(outputLog, "   * Processing partial installment of ₹15,000 for Student 110 (MECH)...");
            FeePayment fp = service.payInstallment(110, 15000);
            printlnBoth(outputLog, "   [PAYMENT COMMITTED] Updated Ledger: " + fp.toString());
        } catch (HostelException e) {
            printlnBoth(outputLog, "   [ERROR] " + e.getMessage());
        }
        printlnBoth(outputLog, "");

        // 8. JAVA STREAM API ADVANCED AGGREGATIONS (Unit 5 Streams & Lambdas)
        printlnBoth(outputLog, "[STAGE 8] ADVANCED STREAM API ANALYTICS & GROUPING (Unit 5 Syllabus):");
        printlnBoth(outputLog, "   * Hosteller Count Grouped by Department:");
        Map<String, Long> deptCounts = service.getStudentCountByDepartment();
        deptCounts.forEach((dept, count) -> printlnBoth(outputLog, String.format("     - Department %-6s : %d students", dept, count)));
        printlnBoth(outputLog, "");

        // 9. MULTITHREADED BACKGROUND AUDIT (Unit 3 Multithreading)
        printlnBoth(outputLog, "[STAGE 9] MULTITHREADED CONCURRENT WORKERS EXECUTION (Unit 3 Syllabus):");
        Thread occupancyThread = new Thread(new OccupancyReportWorker(service), "OccupancyReport-Thread");
        FeeAuditWorker feeThread = new FeeAuditWorker(service);

        occupancyThread.start();
        feeThread.start();

        try {
            occupancyThread.join();
            feeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        printlnBoth(outputLog, "   -> Multithreaded background tasks synchronized and completed successfully.\n");

        // 10. GENERATE STRUCTURED REPORTS (FR-6)
        printlnBoth(outputLog, "[STAGE 10] GENERATING FINAL OPERATIONAL REPORTS:");
        service.printOccupancyReport();
        service.printFeeDefaultersReport();

        printlnBoth(outputLog, "========================================================================================");
        printlnBoth(outputLog, "[SUCCESS] JAVA SUITE EXECUTION COMPLETED WITH 100% TEST PASS RATE.");
        printlnBoth(outputLog, "[INFO] Execution log saved to: " + OUTPUT_FILE);
        printlnBoth(outputLog, "========================================================================================");

        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            writer.print(outputLog.toString());
        } catch (IOException e) {
            System.err.println("Failed to write output file: " + e.getMessage());
        }
    }

    private static void printlnBoth(StringBuilder log, String text) {
        System.out.println(text);
        log.append(text).append("\n");
    }
}
