/**
 * UseCase10BookingCancellation
 *
 * This class demonstrates safe cancellation of bookings using
 * rollback logic with Stack (LIFO) and inventory restoration.
 *
 * It ensures consistent system state after cancellations.
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// Reservation class
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

    public String getRoomType() {
        return roomType;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 0);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History (for validation)
class BookingHistory {
    private Map<String, Reservation> reservations;

    public BookingHistory() {
        reservations = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void removeReservation(String id) {
        reservations.remove(id);
    }
}

// Cancellation Service
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for ID: " + reservationId);

        // Validate reservation existence
        Reservation reservation = history.getReservation(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        String roomType = reservation.getRoomType();

        // Push to rollback stack (LIFO)
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.incrementRoom(roomType);

        // Remove from booking history
        history.removeReservation(reservationId);

        // Confirm cancellation
        System.out.println("Cancellation Successful!");
        System.out.println("Guest   : " + reservation.getGuestName());
        System.out.println("Room    : " + roomType);
        System.out.println("Released Room ID: " + reservationId);
    }

    // Display rollback stack
    public void displayRollbackStack() {
        System.out.println("\n---- Rollback Stack (LIFO) ----");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

// Main class
public class BookMyStayapp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 10.0\n");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SI1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SU2", "Bob", "Suite Room"));

        // Initialize cancellation service
        CancellationService cancellationService =
                new CancellationService(inventory, history);

        // Perform cancellations
        cancellationService.cancelBooking("SI1"); // valid
        cancellationService.cancelBooking("XX9"); // invalid
        cancellationService.cancelBooking("SU2"); // valid

        // Display rollback stack
        cancellationService.displayRollbackStack();

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nSystem state restored successfully after cancellations!");
    }
}