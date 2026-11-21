package com.hostel.exception;

/**
 * Thrown when attempting to allocate a room to a student who already holds an active allocation.
 */
public class StudentAlreadyAllocatedException extends HostelException {
    private final int studentId;

    public StudentAlreadyAllocatedException(int studentId) {
        super(String.format("Allocation Rejected: Student with ID %d already holds an active room allocation.", studentId));
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }
}
