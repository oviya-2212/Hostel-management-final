package com.hostel.exception;

/**
 * Base custom checked exception for Hostel Management System (Unit 3 Syllabus).
 */
public class HostelException extends Exception {
    public HostelException(String message) {
        super(message);
    }

    public HostelException(String message, Throwable cause) {
        super(message, cause);
    }
}
