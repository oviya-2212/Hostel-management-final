package com.hostel.model;

import java.time.LocalDate;

/**
 * Allocation domain entity: Represents assigning a student to a room.
 */
public class Allocation extends BaseEntity {
    private int studentId;
    private int roomId;
    private LocalDate allocationDate;
    private String status; // 'ALLOCATED', 'RELEASED', 'CANCELLED'

    public Allocation(int allocationId, int studentId, int roomId, LocalDate allocationDate, String status) {
        super(allocationId);
        this.studentId = studentId;
        this.roomId = roomId;
        this.allocationDate = allocationDate;
        this.status = status;
    }

    public Allocation(int allocationId, int studentId, int roomId) {
        this(allocationId, studentId, roomId, LocalDate.now(), "ALLOCATED");
    }

    public int getAllocationId() {
        return getId();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "ALLOCATED".equalsIgnoreCase(status);
    }

    @Override
    public String getSummary() {
        return String.format("Allocation[ID=%d, StudentID=%d, RoomID=%d, Date=%s, Status='%s']",
                id, studentId, roomId, allocationDate, status);
    }

    @Override
    public String toString() {
        return String.format("%-8d Student:%-6d Room:%-6d Date:%-12s Status:%-10s",
                id, studentId, roomId, allocationDate, status);
    }
}
