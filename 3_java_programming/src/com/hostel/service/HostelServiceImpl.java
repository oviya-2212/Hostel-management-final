package com.hostel.service;

import com.hostel.exception.EntityNotFoundException;
import com.hostel.exception.HostelException;
import com.hostel.exception.RoomCapacityExceededException;
import com.hostel.exception.StudentAlreadyAllocatedException;
import com.hostel.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * HostelServiceImpl: Enterprise implementation of HostelService.
 * Demonstrates:
 * - Unit 1: Classes, Methods, Encapsulation, static counters
 * - Unit 2: Interface implementation, polymorphism
 * - Unit 3: Exception handling, synchronized methods
 * - Unit 4: String manipulation, StringBuilder
 * - Unit 5: Generics, Collections Framework, Lambdas, and Stream API
 */
public class HostelServiceImpl implements HostelService {

    // Thread-safe in-memory collections (Unit 5 Collections)
    private final Map<Integer, Student> students = new ConcurrentHashMap<>();
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    private final Map<Integer, Allocation> allocations = new ConcurrentHashMap<>();
    private final Map<Integer, CheckInOut> checkInOutRecords = new ConcurrentHashMap<>();
    private final Map<Integer, FeePayment> feePayments = new ConcurrentHashMap<>();

    private static int nextAllocationId = 1001;
    private static int nextCheckInOutId = 501;
    private static int nextPaymentId = 901;

    public HostelServiceImpl() {
        seedInitialData();
    }

    private void seedInitialData() {
        // Seed Students
        addStudent(new Student(101, "Aarav Kumar", "CSE", 2, "9876543210", "aarav.kumar@univ.edu"));
        addStudent(new Student(102, "Diya Sharma", "IT", 3, "9876543211", "diya.sharma@univ.edu"));
        addStudent(new Student(103, "Rohan Verma", "ECE", 1, "9876543212", "rohan.verma@univ.edu"));
        addStudent(new Student(104, "Ananya Iyer", "CSE", 4, "9876543213", "ananya.iyer@univ.edu"));
        addStudent(new Student(105, "Kavya Nair", "AIDS", 2, "9876543214", "kavya.nair@univ.edu"));
        addStudent(new Student(106, "Siddharth Patel", "MECH", 3, "9876543215", "siddharth.patel@univ.edu"));
        addStudent(new Student(107, "Pooja Reddy", "IT", 2, "9876543216", "pooja.reddy@univ.edu"));
        addStudent(new Student(108, "Vikram Singh", "CSE", 1, "9876543217", "vikram.singh@univ.edu"));
        addStudent(new Student(109, "Meera Joshi", "ECE", 4, "9876543218", "meera.joshi@univ.edu"));
        addStudent(new Student(110, "Aditya Menon", "MECH", 2, "9876543219", "aditya.menon@univ.edu"));
        addStudent(new Student(111, "Sneha Pillai", "AIDS", 3, "9876543220", "sneha.pillai@univ.edu"));
        addStudent(new Student(112, "Harish Balan", "IT", 1, "9876543221", "harish.balan@univ.edu"));
        addStudent(new Student(113, "Varun Rao", "CSE", 3, "9876543222", "varun.rao@univ.edu"));
        addStudent(new Student(114, "Tanvi Deshmukh", "ECE", 2, "9876543223", "tanvi.deshmukh@univ.edu"));
        addStudent(new Student(115, "Pranav Sundaram", "MECH", 4, "9876543224", "pranav.sundaram@univ.edu"));

        // Seed Rooms
        addRoom(new Room(201, "A-101", "Single AC", 1, 0));
        addRoom(new Room(202, "A-102", "Single AC", 1, 0));
        addRoom(new Room(203, "A-103", "Single AC", 1, 0));
        addRoom(new Room(204, "B-201", "Double Non-AC", 2, 0));
        addRoom(new Room(205, "B-202", "Double Non-AC", 2, 0));
        addRoom(new Room(206, "B-203", "Double Non-AC", 2, 0));
        addRoom(new Room(207, "B-204", "Double Non-AC", 2, 0));
        addRoom(new Room(208, "C-301", "Triple Non-AC", 3, 0));
        addRoom(new Room(209, "C-302", "Triple Non-AC", 3, 0));
        addRoom(new Room(210, "C-303", "Triple Non-AC", 3, 0));

        // Initial Allocations using Service logic
        try {
            allocateRoom(101, 201);
            allocateRoom(102, 202);
            allocateRoom(103, 204);
            allocateRoom(104, 204);
            allocateRoom(105, 205);
            allocateRoom(106, 205);
            allocateRoom(107, 206);
            allocateRoom(108, 208);
            allocateRoom(109, 208);
            allocateRoom(110, 208);
            allocateRoom(111, 209);
            allocateRoom(112, 209);
        } catch (HostelException ignored) {}

        // Seed Fee Payments
        recordFee(new FeePayment(nextPaymentId++, 101, 65000, 65000, LocalDate.of(2026, 7, 5)));
        recordFee(new FeePayment(nextPaymentId++, 102, 65000, 45000, LocalDate.of(2026, 7, 6)));
        recordFee(new FeePayment(nextPaymentId++, 103, 45000, 45000, LocalDate.of(2026, 7, 5)));
        recordFee(new FeePayment(nextPaymentId++, 104, 45000, 25000, LocalDate.of(2026, 7, 7)));
        recordFee(new FeePayment(nextPaymentId++, 105, 45000, 45000, LocalDate.of(2026, 7, 4)));
        recordFee(new FeePayment(nextPaymentId++, 106, 45000, 15000, LocalDate.of(2026, 7, 8)));
        recordFee(new FeePayment(nextPaymentId++, 107, 45000, 45000, LocalDate.of(2026, 7, 5)));
        recordFee(new FeePayment(nextPaymentId++, 108, 35000, 35000, LocalDate.of(2026, 7, 6)));
        recordFee(new FeePayment(nextPaymentId++, 109, 35000, 20000, LocalDate.of(2026, 7, 9)));
        recordFee(new FeePayment(nextPaymentId++, 110, 35000, 0, LocalDate.of(2026, 7, 10)));
        recordFee(new FeePayment(nextPaymentId++, 111, 35000, 35000, LocalDate.of(2026, 7, 7)));
        recordFee(new FeePayment(nextPaymentId++, 112, 35000, 15000, LocalDate.of(2026, 7, 10)));
        recordFee(new FeePayment(nextPaymentId++, 115, 35000, 0, LocalDate.of(2026, 7, 12)));
    }

    // --- STUDENT MANAGEMENT ---
    @Override
    public void addStudent(Student student) {
        students.put(student.getStudentId(), student);
    }

    @Override
    public Student getStudent(int studentId) throws EntityNotFoundException {
        Student s = students.get(studentId);
        if (s == null) {
            throw new EntityNotFoundException("Student", studentId);
        }
        return s;
    }

    @Override
    public List<Student> getAllStudents() {
        return students.values().stream()
                .sorted(Comparator.comparingInt(Student::getStudentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getStudentsByDepartment(String dept) {
        return students.values().stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(dept))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    // --- ROOM MANAGEMENT ---
    @Override
    public void addRoom(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    @Override
    public Room getRoom(int roomId) throws EntityNotFoundException {
        Room r = rooms.get(roomId);
        if (r == null) {
            throw new EntityNotFoundException("Room", roomId);
        }
        return r;
    }

    @Override
    public List<Room> getAllRooms() {
        return rooms.values().stream()
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getAvailableRooms() {
        // Stream filtering for available beds > 0
        return rooms.values().stream()
                .filter(r -> r.availableBeds() > 0)
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .collect(Collectors.toList());
    }

    // --- ALLOCATION LOGIC WITH CAPACITY VALIDATION (FR-3) ---
    @Override
    public synchronized Allocation allocateRoom(int studentId, int roomId) throws HostelException {
        // 1. Verify Student exists
        Student student = getStudent(studentId);

        // 2. Verify Room exists
        Room room = getRoom(roomId);

        // 3. Rule: Student cannot hold more than one ACTIVE allocation
        boolean hasActive = allocations.values().stream()
                .anyMatch(a -> a.getStudentId() == studentId && a.isActive());
        if (hasActive) {
            throw new StudentAlreadyAllocatedException(studentId);
        }

        // 4. CRITICAL RULE: Check room capacity limit
        if (room.isFull() || room.availableBeds() <= 0) {
            throw new RoomCapacityExceededException(room.getRoomId(), room.getRoomNumber(), room.getCapacity());
        }

        // 5. Commit Allocation and update occupancy atomically
        Allocation alloc = new Allocation(nextAllocationId++, studentId, roomId, LocalDate.now(), "ALLOCATED");
        allocations.put(alloc.getAllocationId(), alloc);
        room.updateOccupancy(1);

        // Create CheckInOut record
        checkIn(alloc.getAllocationId());

        return alloc;
    }

    @Override
    public synchronized void releaseRoom(int allocationId) throws HostelException {
        Allocation alloc = allocations.get(allocationId);
        if (alloc == null) {
            throw new EntityNotFoundException("Allocation", allocationId);
        }
        if (!alloc.isActive()) {
            throw new HostelException("Allocation is already inactive (" + alloc.getStatus() + ").");
        }

        Room room = getRoom(alloc.getRoomId());
        alloc.setStatus("RELEASED");
        room.updateOccupancy(-1);

        // Update checkout
        checkInOutRecords.values().stream()
                .filter(c -> c.getAllocationId() == allocationId && c.isCurrentlyCheckedIn())
                .findFirst()
                .ifPresent(c -> c.setCheckOutDate(LocalDate.now()));
    }

    @Override
    public List<Allocation> getActiveAllocations() {
        return allocations.values().stream()
                .filter(Allocation::isActive)
                .sorted(Comparator.comparingInt(Allocation::getAllocationId))
                .collect(Collectors.toList());
    }

    // --- CHECK-IN / CHECK-OUT ---
    @Override
    public CheckInOut checkIn(int allocationId) throws HostelException {
        Allocation alloc = allocations.get(allocationId);
        if (alloc == null) {
            throw new EntityNotFoundException("Allocation", allocationId);
        }
        CheckInOut record = new CheckInOut(nextCheckInOutId++, allocationId, LocalDate.now());
        checkInOutRecords.put(record.getRecordId(), record);
        return record;
    }

    @Override
    public CheckInOut checkOut(int checkInOutId) throws HostelException {
        CheckInOut record = checkInOutRecords.get(checkInOutId);
        if (record == null) {
            throw new EntityNotFoundException("CheckInOut Record", checkInOutId);
        }
        record.setCheckOutDate(LocalDate.now());
        return record;
    }

    @Override
    public List<CheckInOut> getAllCheckInOutRecords() {
        return checkInOutRecords.values().stream()
                .sorted(Comparator.comparingInt(CheckInOut::getRecordId))
                .collect(Collectors.toList());
    }

    // --- FEE PAYMENTS ---
    @Override
    public FeePayment recordFee(FeePayment payment) {
        feePayments.put(payment.getPaymentId(), payment);
        return payment;
    }

    @Override
    public FeePayment payInstallment(int studentId, double amount) throws HostelException {
        FeePayment payment = feePayments.values().stream()
                .filter(f -> f.getStudentId() == studentId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("FeePayment for Student", studentId));
        payment.recordPayment(amount);
        return payment;
    }

    @Override
    public List<FeePayment> getAllFeePayments() {
        return feePayments.values().stream()
                .sorted(Comparator.comparingInt(FeePayment::getPaymentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeePayment> getDefaulters() {
        return feePayments.values().stream()
                .filter(f -> f.getPendingAmount() > 0)
                .sorted((a, b) -> Double.compare(b.getPendingAmount(), a.getPendingAmount()))
                .collect(Collectors.toList());
    }

    // --- STREAM AGGREGATIONS (Unit 5) ---
    @Override
    public Map<String, Long> getStudentCountByDepartment() {
        return students.values().stream()
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));
    }

    @Override
    public Map<String, Double> getAverageOccupancyByRoomType() {
        return rooms.values().stream()
                .collect(Collectors.groupingBy(
                        Room::getRoomType,
                        Collectors.averagingDouble(Room::getOccupancyPercentage)
                ));
    }

    @Override
    public double getTotalRevenueCollected() {
        return feePayments.values().stream()
                .mapToDouble(FeePayment::getPaidAmount)
                .sum();
    }

    @Override
    public double getTotalArrearsPending() {
        return feePayments.values().stream()
                .mapToDouble(FeePayment::getPendingAmount)
                .sum();
    }

    // --- REPORTING OUTPUTS (Unit 4 String handling / StringBuilder) ---
    @Override
    public void printOccupancyReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                          HOSTEL ROOM OCCUPANCY REPORT\n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-6s %-10s %-16s %-8s %-8s %-12s %-12s\n",
                "RoomID", "RoomNo", "RoomType", "Capacity", "Occupied", "Available", "Status"));
        sb.append("----------------------------------------------------------------------------------------\n");

        int totalCap = 0;
        int totalOcc = 0;

        for (Room r : getAllRooms()) {
            totalCap += r.getCapacity();
            totalOcc += r.getOccupancy();
            String status = r.isVacant() ? "Vacant" : (r.isFull() ? "Full" : "Partial");
            sb.append(String.format("%-6d %-10s %-16s %-8d %-8d %-12d %-12s\n",
                    r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                    r.getCapacity(), r.getOccupancy(), r.availableBeds(), status));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        double overallPct = totalCap > 0 ? ((double) totalOcc / totalCap * 100.0) : 0.0;
        sb.append(String.format("TOTAL ROOMS: %d | TOTAL CAPACITY: %d | TOTAL OCCUPIED: %d | VACANT: %d | RATE: %.2f%%\n",
                rooms.size(), totalCap, totalOcc, (totalCap - totalOcc), overallPct));
        sb.append("========================================================================================\n");
        System.out.println(sb);
    }

    @Override
    public void printFeeDefaultersReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                       HOSTEL FEE DEFAULTERS & ARREARS REPORT\n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-8s %-6s %-18s %-8s %-12s %-12s %-12s %-8s\n",
                "PayID", "StuID", "Student Name", "Dept", "Total Fee", "Paid Amount", "Pending Due", "Status"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (FeePayment f : getDefaulters()) {
            Student s = students.get(f.getStudentId());
            String name = s != null ? s.getName() : "Unknown";
            String dept = s != null ? s.getDepartment() : "-";
            sb.append(String.format("%-8d %-6d %-18s %-8s ₹%-11.2f ₹%-11.2f ₹%-11.2f %-8s\n",
                    f.getPaymentId(), f.getStudentId(), name, dept,
                    f.getTotalFee(), f.getPaidAmount(), f.getPendingAmount(), f.getStatus()));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("TOTAL REVENUE COLLECTED: ₹%,.2f | TOTAL OUTSTANDING ARREARS: ₹%,.2f\n",
                getTotalRevenueCollected(), getTotalArrearsPending()));
        sb.append("========================================================================================\n");
        System.out.println(sb);
    }
}
