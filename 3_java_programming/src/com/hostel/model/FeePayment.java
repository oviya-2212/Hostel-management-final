package com.hostel.model;

import java.time.LocalDate;

/**
 * FeePayment domain entity: Financial records, installment auditing, and pending calculations.
 */
public class FeePayment extends BaseEntity {
    private int studentId;
    private double totalFee;
    private double paidAmount;
    private double pendingAmount;
    private LocalDate paymentDate;
    private String status; // 'PAID', 'PARTIAL', 'PENDING'

    public FeePayment(int paymentId, int studentId, double totalFee, double paidAmount, LocalDate paymentDate) {
        super(paymentId);
        this.studentId = studentId;
        this.totalFee = totalFee;
        this.paidAmount = paidAmount;
        this.paymentDate = paymentDate;
        calculatePending();
    }

    public FeePayment(int paymentId, int studentId, double totalFee) {
        this(paymentId, studentId, totalFee, 0.0, LocalDate.now());
    }

    public int getPaymentId() {
        return getId();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
        calculatePending();
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
        calculatePending();
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public synchronized void recordPayment(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative.");
        }
        this.paidAmount = Math.min(this.totalFee, this.paidAmount + amount);
        this.paymentDate = LocalDate.now();
        calculatePending();
    }

    public final void calculatePending() {
        this.pendingAmount = Math.max(0.0, this.totalFee - this.paidAmount);
        if (this.pendingAmount == 0.0) {
            this.status = "PAID";
        } else if (this.paidAmount > 0.0) {
            this.status = "PARTIAL";
        } else {
            this.status = "PENDING";
        }
    }

    @Override
    public String getSummary() {
        return String.format("FeePayment[ID=%d, StudentID=%d, Total=%.2f, Paid=%.2f, Pending=%.2f, Status='%s']",
                id, studentId, totalFee, paidAmount, pendingAmount, status);
    }

    @Override
    public String toString() {
        return String.format("%-8d Student:%-6d Total:₹%-10.2f Paid:₹%-10.2f Pending:₹%-10.2f Status:%-8s (%s)",
                id, studentId, totalFee, paidAmount, pendingAmount, status, paymentDate);
    }
}
