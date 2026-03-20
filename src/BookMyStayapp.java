/**
 * UseCase11ConcurrentBookingSimulation
 *
 * This class simulates multiple guests booking rooms concurrently.
 * Synchronization ensures thread-safe access to shared booking queue
 * and inventory to prevent double-booking and inconsistent state.
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;
import java.util.concurrent.*;

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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Thread-safe Inventory
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    // Synchronized to ensure thread-safe update
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    // Synchronized to restore inventory safely
    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addReservation(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation pollReservation() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Processor (Runnable for threads)
class BookingProcessor implements Runnable {
    private BookingQueue bookingQueue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                r = bookingQueue.pollReservation();
            }

            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " -> Booking Confirmed: " + r.getGuestName() +
                        " (" + r.getRoomType() + ")");
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " -> Booking Failed: No availability for " + r.getRoomType() +
                        " (" + r.getGuestName() + ")");
            }

            try {
                Thread.sleep(50); // Simulate processing delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
        System.out.println("Version: 11.0\n");

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue bookingQueue = new BookingQueue();

        // Add multiple reservations
        bookingQueue.addReservation(new Reservation("R1", "Alice", "Single Room"));
        bookingQueue.addReservation(new Reservation("R2", "Bob", "Single Room"));
        bookingQueue.addReservation(new Reservation("R3", "Charlie", "Double Room"));
        bookingQueue.addReservation(new Reservation("R4", "David", "Suite Room"));
        bookingQueue.addReservation(new Reservation("R5", "Eve", "Suite Room")); // likely to fail

        // Simulate multiple threads processing bookings
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Display final inventory
        inventory.displayInventory();

        System.out.println("\nConcurrent booking simulation completed successfully!");
    }
}