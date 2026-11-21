package com.hostel.thread;

import com.hostel.service.HostelService;

/**
 * OccupancyReportWorker: Implements Runnable for asynchronous room occupancy analytics.
 * Demonstrates Unit 3 Multithreading (Java Thread Model, Implementing Runnable).
 */
public class OccupancyReportWorker implements Runnable {
    private final HostelService hostelService;

    public OccupancyReportWorker(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] Asynchronous Occupancy Worker Started...\n", threadName);
        try {
            Thread.sleep(300); // Simulate background processing
            System.out.printf("[%s] Generating Room Inventory Analytics via Java Stream API:\n", threadName);
            hostelService.getAverageOccupancyByRoomType().forEach((type, avgPct) ->
                    System.out.printf("   * Room Type: %-15s -> Average Utilization: %5.1f%%\n", type, avgPct));
            System.out.printf("[%s] Asynchronous Occupancy Worker Completed.\n", threadName);
        } catch (InterruptedException e) {
            System.err.printf("[%s] Worker thread was interrupted: %s\n", threadName, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
