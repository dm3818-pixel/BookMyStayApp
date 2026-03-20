/**
 * UseCase12DataPersistenceRecovery
 *
 * This class demonstrates persistence of booking history and inventory
 * using Java serialization. The system state is saved to a file and restored
 * during startup, enabling continuity across application restarts.
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Serializable Inventory
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) { reservations.add(r); }

    public void removeReservation(String reservationId) {
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
    }

    public void displayHistory() {
        System.out.println("\n---- Booking History ----");
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }
    }

    public List<Reservation> getReservations() { return reservations; }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_PATH = "book_my_stay_data.ser";

    public static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("\nNo saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\nSystem state restored successfully!");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// Main Application
public class BookMyStayapp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 12.0\n");

        // Attempt to load previous state
        Object[] restoredState = PersistenceService.loadState();
        RoomInventory inventory;
        BookingHistory history;

        if (restoredState != null) {
            inventory = (RoomInventory) restoredState[0];
            history = (BookingHistory) restoredState[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // Simulate new bookings
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Suite Room");

        if (inventory.allocateRoom(r1.getRoomType())) history.addReservation(r1);
        if (inventory.allocateRoom(r2.getRoomType())) history.addReservation(r2);

        // Display current state
        inventory.displayInventory();
        history.displayHistory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, history);

        System.out.println("\nSystem shutdown completed. Data persisted for next startup!");
    }
}