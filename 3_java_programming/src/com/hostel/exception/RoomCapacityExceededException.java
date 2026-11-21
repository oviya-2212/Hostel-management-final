package com.hostel.exception;

/**
 * Thrown when an allocation attempt is made on a room that has reached its maximum bed capacity.
 * Enforces Functional Requirement 3: Prevent allocations beyond room capacity.
 */
public class RoomCapacityExceededException extends HostelException {
    private final int roomId;
    private final String roomNumber;
    private final int capacity;

    public RoomCapacityExceededException(int roomId, String roomNumber, int capacity) {
        super(String.format("Allocation Rejected: Room '%s' (ID: %d) has reached maximum bed capacity of %d.",
                roomNumber, roomId, capacity));
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }
}
