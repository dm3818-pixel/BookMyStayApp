/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates object-oriented modeling using abstraction,
 * inheritance, and polymorphism in a Hotel Booking System.
 *
 * It initializes different room types and displays their details
 * along with static availability.
 *
 * @author YourName
 * @version 2.1
 */

// Abstract class representing a generic Room
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    // Constructor
    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}

// Single Room class
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

// Double Room class
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// Main Application Class
public class BookMyStayapp {

    /**
     * Entry point of the application
     */
    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 2.1\n");

        // Create room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display room details and availability
        System.out.println("---- Room Details & Availability ----\n");

        singleRoom.displayDetails();
        System.out.println("Available : " + singleAvailability);
        System.out.println("------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available : " + doubleAvailability);
        System.out.println("------------------------------------");

        suiteRoom.displayDetails();
        System.out.println("Available : " + suiteAvailability);
        System.out.println("------------------------------------");

        System.out.println("Thank you for exploring room options!");
    }
}