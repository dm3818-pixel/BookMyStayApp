/**
 * UseCase4RoomSearch
 *
 * This class demonstrates room search functionality using
 * read-only access to centralized inventory.
 *
 * It ensures that only available rooms are displayed without
 * modifying system state.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    private String roomType;
    private double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Price     : $" + price);
    }
}

// Concrete Room types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 300.0);
    }
}

// Inventory class (read-only usage in this use case)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // Example: unavailable
        inventory.put("Suite Room", 2);
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// Search Service (read-only operations)
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room domain objects
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    // Method to search available rooms
    public void searchAvailableRooms() {
        System.out.println("---- Available Rooms ----\n");

        for (String roomType : roomCatalog.keySet()) {
            int available = inventory.getAvailability(roomType);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(roomType);
                room.displayDetails();
                System.out.println("Available : " + available);
                System.out.println("--------------------------");
            }
        }
    }
}

// Main class
public class BookMyStayapp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 4.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("Search completed successfully!");
    }
}