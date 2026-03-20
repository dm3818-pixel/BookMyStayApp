/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates how booking requests are processed,
 * rooms are allocated uniquely, and inventory is updated safely.
 *
 * It prevents double-booking using Set and ensures consistency
 * through controlled allocation logic.
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

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

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Updated Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Core logic)
class BookingService {

    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocations = new HashMap<>();
    }

    // Process booking requests
    public void processBookings(BookingRequestQueue requestQueue) {

        System.out.println("\n---- Processing Booking Requests ----");

        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.getNextRequest();

            String roomType = reservation.getRoomType();
            String guestName = reservation.getGuestName();

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;

                // Ensure uniqueness (Set prevents duplicates)
                if (!allocatedRoomIds.contains(roomId)) {
                    allocatedRoomIds.add(roomId);

                    // Map room type to allocated IDs
                    roomAllocations.putIfAbsent(roomType, new HashSet<>());
                    roomAllocations.get(roomType).add(roomId);

                    // Update inventory immediately
                    inventory.decrementRoom(roomType);

                    // Confirm booking
                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest   : " + guestName);
                    System.out.println("Room    : " + roomType);
                    System.out.println("Room ID : " + roomId);
                    System.out.println("------------------------------");
                }

            } else {
                System.out.println("Booking Failed for " + guestName +
                        " (No availability for " + roomType + ")");
                System.out.println("------------------------------");
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
        System.out.println("Version: 6.0\n");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        BookingService bookingService = new BookingService(inventory);
        bookingService.processBookings(queue);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nAll bookings processed with consistency!");
    }
}