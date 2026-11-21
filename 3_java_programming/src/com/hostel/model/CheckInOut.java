package com.hostel.model;

import java.time.LocalDate;

/**
 * CheckInOut domain entity: Tracks arrival and departure dates.
 */
public class CheckInOut extends BaseEntity {
    private int allocationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public CheckInOut(int recordId, int allocationId, LocalDate checkInDate, LocalDate checkOutDate) {
        super(recordId);
        this.allocationId = allocationId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public CheckInOut(int recordId, int allocationId, LocalDate checkInDate) {
        this(recordId, allocationId, checkInDate, null);
    }

    public int getRecordId() {
        return getId();
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isCurrentlyCheckedIn() {
        return checkInDate != null && checkOutDate == null;
    }

    @Override
    public String getSummary() {
        return String.format("CheckInOut[ID=%d, AllocID=%d, CheckIn=%s, CheckOut=%s]",
                id, allocationId, checkInDate, checkOutDate != null ? checkOutDate : "ACTIVE");
    }

    @Override
    public String toString() {
        return String.format("%-8d Alloc:%-8d In:%-12s Out:%-12s Status:%-10s",
                id, allocationId, checkInDate,
                checkOutDate != null ? checkOutDate.toString() : "-",
                isCurrentlyCheckedIn() ? "CHECKED_IN" : "CHECKED_OUT");
    }
}
