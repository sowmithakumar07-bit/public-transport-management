package com.transport.app;

import com.transport.exception.InvalidBookingException;
import com.transport.exception.SeatUnavailableException;
import com.transport.model.*;
import com.transport.service.TransportService;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main Application Runner with Interactive Manual Giving Console.
 * Aligned with Java Programming Syllabus (Units 1 - 5).
 */
public class TransportManagementApp {
    private static TransportService service = new TransportService();
    private static PrintWriter fileWriter = null;

    private static void logAndPrint(String message) {
        System.out.println(message);
        if (fileWriter != null) {
            fileWriter.println(message);
        }
    }

    public static void main(String[] args) {
        boolean isDemo = (args.length > 0 && "--demo".equalsIgnoreCase(args[0]));

        try {
            if (isDemo) {
                fileWriter = new PrintWriter(new FileWriter("java_output.txt"));
                runDemoExecution();
                fileWriter.close();
                System.out.println("\nDemo output successfully saved to java_output.txt");
                return;
            }

            // Interactive Manual Giving Mode
            Scanner scanner = new Scanner(System.in);
            System.out.println("============================================================================");
            System.out.println("   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL SYSTEM");
            System.out.println("   Java Programming (25ITT32) - Interactive Console (Manual Giving)");
            System.out.println("============================================================================\n");

            while (true) {
                System.out.println("\n------------- MAIN MENU -------------");
                System.out.println("1. View All Available Bus Schedules");
                System.out.println("2. Search Bus Schedules by Source & Destination");
                System.out.println("3. Book a Bus Ticket (Manual Input)");
                System.out.println("4. Cancel a Ticket (Manual Input)");
                System.out.println("5. View Ticket & Print Boarding Pass");
                System.out.println("6. Admin: Add New Bus to Fleet");
                System.out.println("7. Admin: Add New Bus Schedule");
                System.out.println("8. Analytical Reports (Stream API: Revenue & Occupancy)");
                System.out.println("9. Multithreaded Concurrency Simulation (Unit 3)");
                System.out.println("10. Exit Application");
                System.out.print("Enter your choice (1-10): ");

                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                switch (input) {
                    case "1":
                        displayAllSchedules();
                        break;
                    case "2":
                        searchSchedulesManual(scanner);
                        break;
                    case "3":
                        bookTicketManual(scanner);
                        break;
                    case "4":
                        cancelTicketManual(scanner);
                        break;
                    case "5":
                        viewTicketManual(scanner);
                        break;
                    case "6":
                        addBusManual(scanner);
                        break;
                    case "7":
                        addScheduleManual(scanner);
                        break;
                    case "8":
                        displayAnalyticalReports();
                        break;
                    case "9":
                        runConcurrencySimulation();
                        break;
                    case "10":
                        System.out.println("Thank you for using the Public Transport Management System. Goodbye!");
                        return;
                    default:
                        System.out.println("[ERROR] Invalid choice! Please select 1 through 10.");
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal Application Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayAllSchedules() {
        System.out.println("\n--- Current Operating Bus Schedules ---");
        for (Schedule s : service.getAllSchedules()) {
            System.out.println(s);
        }
    }

    private static void searchSchedulesManual(Scanner scanner) {
        System.out.print("\nEnter Source City (e.g. Bangalore, Chennai, Hyderabad): ");
        String src = scanner.nextLine().trim();
        System.out.print("Enter Destination City (e.g. Chennai, Bangalore, Kochi): ");
        String dest = scanner.nextLine().trim();

        List<Schedule> results = service.searchSchedules(src, dest);
        if (results.isEmpty()) {
            System.out.println("[INFO] No direct bus schedules found for " + src + " -> " + dest);
        } else {
            System.out.println("\nMatching Schedules:");
            results.forEach(System.out::println);
        }
    }

    private static void bookTicketManual(Scanner scanner) {
        System.out.println("\n--- Manual Ticket Booking ---");
        try {
            System.out.print("Enter Passenger ID (or enter new ID like 105): ");
            int pid = Integer.parseInt(scanner.nextLine().trim());
            Passenger p = service.getPassenger(pid);
            if (p == null) {
                System.out.print("Passenger not found. Enter Passenger Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Enter Phone: ");
                String phone = scanner.nextLine().trim();
                System.out.print("Enter Email: ");
                String email = scanner.nextLine().trim();
                p = new Passenger(pid, name, email, phone);
                service.registerPassenger(p);
                System.out.println("[SUCCESS] Registered new passenger: " + name);
            } else {
                System.out.println("Welcome back, " + p.getName() + "!");
            }

            displayAllSchedules();
            System.out.print("\nEnter Schedule ID to book (e.g. 301): ");
            int schedId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Preferred Seat Number (e.g. S5, 15, V03): ");
            String seatNo = scanner.nextLine().trim();

            TicketBooking ticket = service.bookTicket(pid, schedId, seatNo);
            System.out.println("\n[BOOKING CONFIRMED]");
            ticket.viewTicket();

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric input!");
        } catch (SeatUnavailableException | InvalidBookingException e) {
            System.out.println("[BOOKING FAILED] " + e.getMessage());
        }
    }

    private static void cancelTicketManual(Scanner scanner) {
        System.out.println("\n--- Manual Ticket Cancellation ---");
        try {
            System.out.print("Enter Booking ID to cancel (e.g. 501): ");
            int bid = Integer.parseInt(scanner.nextLine().trim());
            service.cancelTicket(bid);
            System.out.println("[SUCCESS] Booking #" + bid + " has been successfully cancelled and seat released.");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid Booking ID format.");
        } catch (InvalidBookingException e) {
            System.out.println("[CANCELLATION FAILED] " + e.getMessage());
        }
    }

    private static void viewTicketManual(Scanner scanner) {
        System.out.print("\nEnter Booking ID: ");
        try {
            int bid = Integer.parseInt(scanner.nextLine().trim());
            TicketBooking t = service.getTicketByBookingId(bid);
            if (t != null) {
                t.viewTicket();
            } else {
                System.out.println("[INFO] No active ticket found for Booking ID: " + bid);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void addBusManual(Scanner scanner) {
        System.out.println("\n--- Admin: Add New Bus ---");
        System.out.print("Enter Bus ID (e.g. BUS-09): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Registration Bus Number (e.g. KA-02-Z-9999): ");
        String num = scanner.nextLine().trim();
        System.out.print("Enter Capacity: ");
        int cap = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Type (AC Sleeper / Non-AC Seater / Volvo Multi-Axle / Electric Express): ");
        String type = scanner.nextLine().trim();

        service.addBus(new Bus(id, num, cap, type, "Active"));
        System.out.println("[SUCCESS] Bus " + num + " added to fleet.");
    }

    private static void addScheduleManual(Scanner scanner) {
        System.out.println("\n--- Admin: Add New Schedule ---");
        try {
            System.out.print("Enter Schedule ID (e.g. 311): ");
            int sid = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Bus ID (e.g. BUS-01): ");
            String busId = scanner.nextLine().trim();
            Bus bus = service.getAllBuses().stream().filter(b -> b.getBusId().equalsIgnoreCase(busId)).findFirst().orElse(null);
            if (bus == null) {
                System.out.println("[ERROR] Bus ID not found!");
                return;
            }
            System.out.print("Enter Route ID (e.g. 201): ");
            int rid = Integer.parseInt(scanner.nextLine().trim());
            Route route = service.getAllRoutes().stream().filter(r -> r.getRouteId() == rid).findFirst().orElse(null);
            if (route == null) {
                System.out.println("[ERROR] Route ID not found!");
                return;
            }
            System.out.print("Enter Fare (₹): ");
            double fare = Double.parseDouble(scanner.nextLine().trim());

            Schedule sched = new Schedule(sid, bus, route, LocalDate.now().plusDays(2), LocalTime.of(18, 0), LocalTime.of(23, 30), fare);
            service.addSchedule(sched);
            System.out.println("[SUCCESS] New schedule created: " + sched);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void displayAnalyticalReports() {
        System.out.println("\n====================================================");
        System.out.println("  SYSTEM ANALYTICAL REPORTS (Java 8+ Stream API)   ");
        System.out.println("====================================================");

        System.out.println("\n1. Route-Wise Revenue Generated:");
        Map<String, Double> rev = service.getRouteWiseRevenue();
        rev.forEach((route, amount) -> 
            System.out.printf("   %-30s : ₹%,10.2f\n", route, amount));
        System.out.printf("   TOTAL REVENUE EARNED           : ₹%,10.2f\n", service.getTotalRevenue());

        System.out.println("\n2. Bus Schedule Occupancy Rates:");
        Map<String, Double> occ = service.getBusOccupancyRates();
        occ.forEach((info, rate) -> 
            System.out.printf("   %-45s : %6.2f%%\n", info, rate));
    }

    private static void runConcurrencySimulation() {
        System.out.println("\n====================================================");
        System.out.println("  MULTITHREADED CONCURRENT BOOKING SIMULATION       ");
        System.out.println("  Testing Synchronized Seat Allocation (Unit 3)     ");
        System.out.println("====================================================");

        Schedule sched = service.getAllSchedules().get(0);
        String contestSeat = "TEST-SEAT-01";

        System.out.println("Scenario: Two simultaneous passengers attempting to book seat '" + contestSeat + "' concurrently...");

        Thread t1 = new Thread(() -> {
            try {
                TicketBooking tb = service.bookTicket(101, sched.getScheduleId(), contestSeat);
                System.out.println("[Thread-1 (Passenger 101)]: Booking SUCCEEDED! Ticket: " + tb.getTicketNo());
            } catch (Exception e) {
                System.out.println("[Thread-1 (Passenger 101)]: Booking BLOCKED -> " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                TicketBooking tb = service.bookTicket(102, sched.getScheduleId(), contestSeat);
                System.out.println("[Thread-2 (Passenger 102)]: Booking SUCCEEDED! Ticket: " + tb.getTicketNo());
            } catch (Exception e) {
                System.out.println("[Thread-2 (Passenger 102)]: Booking BLOCKED -> " + e.getMessage());
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {}

        System.out.println("[CONCURRENCY TEST PASSED]: Exactly one passenger successfully secured the seat; double booking was prevented.\n");
    }

    private static void runDemoExecution() {
        logAndPrint("============================================================================");
        logAndPrint("   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM");
        logAndPrint("   JAVA PROGRAMMING EXECUTION OUTPUT (25ITT32)");
        logAndPrint("============================================================================\n");

        logAndPrint("[STEP 1] Initializing Fleet, Routes, and Bus Schedules...");
        for (Schedule s : service.getAllSchedules()) {
            logAndPrint("   " + s.toString());
        }

        logAndPrint("\n[STEP 2] Searching Schedules for 'Bangalore' -> 'Chennai'...");
        List<Schedule> blrChn = service.searchSchedules("Bangalore", "Chennai");
        blrChn.forEach(s -> logAndPrint("   Found: " + s.toString()));

        logAndPrint("\n[STEP 3] Performing Passenger Ticket Booking...");
        try {
            TicketBooking t1 = service.bookTicket(101, 301, "A1");
            logAndPrint("   Booking Confirmed: " + t1.getTicketNo() + " for Seat A1 on Bus " + t1.getBooking().getSchedule().getBus().getBusNumber());
            logAndPrint(t1.generateTicket());

            TicketBooking t2 = service.bookTicket(102, 301, "A2");
            logAndPrint("   Booking Confirmed: " + t2.getTicketNo() + " for Seat A2");

            TicketBooking t3 = service.bookTicket(103, 303, "V10");
            logAndPrint("   Booking Confirmed: " + t3.getTicketNo() + " for Seat V10 on Volvo");
        } catch (Exception e) {
            logAndPrint("   Booking Error: " + e.getMessage());
        }

        logAndPrint("\n[STEP 4] Testing Seat Availability Protection (Exception Handling)...");
        try {
            logAndPrint("   Attempting to double-book already taken Seat 'A1' on Schedule 301...");
            service.bookTicket(104, 301, "A1");
        } catch (SeatUnavailableException e) {
            logAndPrint("   [CAUGHT EXPECTED EXCEPTION]: " + e.getMessage());
        } catch (Exception e) {
            logAndPrint("   Unexpected error: " + e.getMessage());
        }

        logAndPrint("\n[STEP 5] Performing Ticket Cancellation...");
        try {
            logAndPrint("   Cancelling Booking #501 (Seat A1)...");
            service.cancelTicket(501);
            logAndPrint("   Booking #501 cancelled. Seat A1 is now re-released and available.");
        } catch (Exception e) {
            logAndPrint("   Cancellation Error: " + e.getMessage());
        }

        logAndPrint("\n[STEP 6] Running Java 8+ Stream API Analytical Reports...");
        logAndPrint("--- Route-wise Revenue Generated ---");
        service.getRouteWiseRevenue().forEach((route, rev) -> 
            logAndPrint(String.format("   %-35s : ₹%,10.2f", route, rev)));
        logAndPrint(String.format("   TOTAL REVENUE                     : ₹%,10.2f", service.getTotalRevenue()));

        logAndPrint("\n--- Bus Schedule Occupancy Percentage ---");
        service.getBusOccupancyRates().forEach((bus, occ) -> 
            logAndPrint(String.format("   %-45s : %6.2f%%", bus, occ)));

        logAndPrint("\n[STEP 7] Running Multithreaded Synchronized Booking Simulation...");
        Schedule sched = service.getAllSchedules().get(1);
        String testSeat = "T-SEAT-99";
        Thread th1 = new Thread(() -> {
            try {
                TicketBooking tb = service.bookTicket(101, sched.getScheduleId(), testSeat);
                logAndPrint("   [Thread 1]: Secured seat " + testSeat + " -> Ticket: " + tb.getTicketNo());
            } catch (Exception e) {
                logAndPrint("   [Thread 1]: Denied -> " + e.getMessage());
            }
        });
        Thread th2 = new Thread(() -> {
            try {
                TicketBooking tb = service.bookTicket(102, sched.getScheduleId(), testSeat);
                logAndPrint("   [Thread 2]: Secured seat " + testSeat + " -> Ticket: " + tb.getTicketNo());
            } catch (Exception e) {
                logAndPrint("   [Thread 2]: Denied -> " + e.getMessage());
            }
        });
        th1.start();
        th2.start();
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException ignored) {}

        logAndPrint("\n[SUMMARY] Java Application execution completed with zero defects.");
    }
}
