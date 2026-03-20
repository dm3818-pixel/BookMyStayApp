/**
 * UseCase8BookingHistoryReport
 *
 * This class demonstrates how confirmed bookings are stored
 * and used for reporting and administrative visibility.
 *
 * It introduces a booking history (List) and a reporting service
 * without modifying stored data.
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

// Reservation class (enhanced for history tracking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
    }
}

// Booking History (List-based storage)
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

// Reporting Service
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n---- Booking History ----");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
            System.out.println("--------------------------");
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n---- Booking Summary Report ----");

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String roomType = r.getRoomType();
            summary.put(roomType, summary.getOrDefault(roomType, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " Bookings : " + entry.getValue());
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 8.0\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SI1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI2", "Bob", "Single Room"));
        history.addReservation(new Reservation("SU3", "Charlie", "Suite Room"));

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummaryReport();

        System.out.println("\nReporting completed successfully!");
    }
}