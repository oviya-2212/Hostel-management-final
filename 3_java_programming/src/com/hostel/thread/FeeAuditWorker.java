package com.hostel.thread;

import com.hostel.service.HostelService;

/**
 * FeeAuditWorker: Extends Thread for concurrent financial audit and defaulter detection.
 * Demonstrates Unit 3 Multithreading (Creating a Thread via Class Extension, Thread Priorities).
 */
public class FeeAuditWorker extends Thread {
    private final HostelService hostelService;

    public FeeAuditWorker(HostelService hostelService) {
        super("FeeAuditor-Thread");
        this.hostelService = hostelService;
    }

    @Override
    public void run() {
        System.out.printf("[%s] Financial Background Audit Thread Launched (Priority: %d)...\n",
                getName(), getPriority());
        try {
            Thread.sleep(400); // Simulate audit calculation
            double totalPaid = hostelService.getTotalRevenueCollected();
            double totalArrears = hostelService.getTotalArrearsPending();
            int defaultersCount = hostelService.getDefaulters().size();

            System.out.printf("[%s] Audit Summary:\n", getName());
            System.out.printf("   * Total Revenue Inflow  : ₹%,.2f\n", totalPaid);
            System.out.printf("   * Total Uncollected Dues: ₹%,.2f\n", totalArrears);
            System.out.printf("   * Total Defaulter Count : %d students\n", defaultersCount);
            System.out.printf("[%s] Financial Background Audit Thread Finished Successfully.\n", getName());
        } catch (InterruptedException e) {
            System.err.printf("[%s] Audit thread interrupted: %s\n", getName(), e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
