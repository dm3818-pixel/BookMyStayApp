/**
 * UseCase9ErrorHandlingValidation
 *
 * This class demonstrates validation and error handling in booking operations.
 * It ensures invalid inputs are detected early and handled gracefully.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // No availability
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {
        int current = inventory.get(roomType);

        if (current <= 0) {
            throw new InvalidBookingException("No availability for " + roomType);
        }

        inventory.put(roomType, current - 1);
    }
}

// Validator class
class BookingValidator {

    public static void validate(RoomInventory inventory, Reservation reservation)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(inventory, reservation);

            // Step 2: Perform allocation
            inventory.decrementRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking Confirmed for " + reservation.getGuestName()
                    + " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 9.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Test cases

        // Valid booking
        bookingService.confirmBooking(new Reservation("Alice", "Single Room"));

        // Invalid room type
        bookingService.confirmBooking(new Reservation("Bob", "Luxury Room"));

        // No availability
        bookingService.confirmBooking(new Reservation("Charlie", "Suite Room"));

        // Empty guest name
        bookingService.confirmBooking(new Reservation("", "Double Room"));

        System.out.println("\nSystem remains stable after handling errors!");
    }
}