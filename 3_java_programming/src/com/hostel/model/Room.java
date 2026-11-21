package com.hostel.model;

/**
 * Room domain entity: Represents hostel room details, bed capacity, and active occupancy.
 * Demonstrates Unit 1 (Classes, Encapsulation) and Unit 2 (Inheritance).
 */
public class Room extends BaseEntity {
    private String roomNumber;
    private String roomType; // 'Single AC', 'Double Non-AC', 'Triple Non-AC'
    private int capacity;
    private int occupancy;

    public Room(int roomId, String roomNumber, String roomType, int capacity, int occupancy) {
        super(roomId);
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.occupancy = occupancy;
    }

    public Room(int roomId, String roomNumber, String roomType, int capacity) {
        this(roomId, roomNumber, roomType, capacity, 0);
    }

    public int getRoomId() {
        return getId();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    /**
     * Computes available vacant beds dynamically: capacity - occupancy.
     */
    public int availableBeds() {
        return Math.max(0, capacity - occupancy);
    }

    public boolean isFull() {
        return occupancy >= capacity;
    }

    public boolean isVacant() {
        return occupancy == 0;
    }

    /**
     * Atomic change to occupancy count.
     */
    public synchronized void updateOccupancy(int change) {
        this.occupancy = Math.max(0, Math.min(capacity, this.occupancy + change));
    }

    public double getOccupancyPercentage() {
        if (capacity == 0) return 0.0;
        return (double) occupancy / capacity * 100.0;
    }

    @Override
    public String getSummary() {
        return String.format("Room[ID=%d, No='%s', Type='%s', Cap=%d, Occ=%d, Available=%d]",
                id, roomNumber, roomType, capacity, occupancy, availableBeds());
    }

    @Override
    public String toString() {
        return String.format("%-6d %-10s %-15s Cap:%-2d Occ:%-2d Available:%-2d (%5.1f%%)",
                id, roomNumber, roomType, capacity, occupancy, availableBeds(), getOccupancyPercentage());
    }
}
