package com.hostel.exception;

/**
 * Thrown when a requested student, room, or allocation is not found in memory/database.
 */
public class EntityNotFoundException extends HostelException {
    public EntityNotFoundException(String entityType, Object id) {
        super(String.format("%s with identifier '%s' was not found in the hostel records.", entityType, id));
    }
}
