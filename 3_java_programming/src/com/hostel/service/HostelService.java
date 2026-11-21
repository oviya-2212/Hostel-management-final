package com.hostel.service;

import com.hostel.exception.HostelException;
import com.hostel.model.*;

import java.util.List;
import java.util.Map;

/**
 * HostelService Interface: Contract for hostel business logic operations.
 * Demonstrates Unit 2 (Interfaces and Dynamic Method Dispatch).
 */
public interface HostelService {
    // Student Management
    void addStudent(Student student);
    Student getStudent(int studentId) throws HostelException;
    List<Student> getAllStudents();
    List<Student> getStudentsByDepartment(String dept);

    // Room Management
    void addRoom(Room room);
    Room getRoom(int roomId) throws HostelException;
    List<Room> getAllRooms();
    List<Room> getAvailableRooms();

    // Allocation Logic with Capacity Enforcement
    Allocation allocateRoom(int studentId, int roomId) throws HostelException;
    void releaseRoom(int allocationId) throws HostelException;
    List<Allocation> getActiveAllocations();

    // Check-in and Check-out
    CheckInOut checkIn(int allocationId) throws HostelException;
    CheckInOut checkOut(int checkInOutId) throws HostelException;
    List<CheckInOut> getAllCheckInOutRecords();

    // Fee Payments & Arrears
    FeePayment recordFee(FeePayment payment);
    FeePayment payInstallment(int studentId, double amount) throws HostelException;
    List<FeePayment> getAllFeePayments();
    List<FeePayment> getDefaulters();

    // Stream-based Analytical Aggregations (Unit 5 Streams & Lambdas)
    Map<String, Long> getStudentCountByDepartment();
    Map<String, Double> getAverageOccupancyByRoomType();
    double getTotalRevenueCollected();
    double getTotalArrearsPending();

    // Reporting
    void printOccupancyReport();
    void printFeeDefaultersReport();
}
