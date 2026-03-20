}/**
 * UseCase7AddOnServiceSelection
 *
 * This class demonstrates how add-on services can be attached
 * to existing reservations without modifying core booking logic.
 *
 * It uses a Map<String, List<Service>> to maintain a one-to-many
 * relationship between reservation IDs and selected services.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// Add-On Service class
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Mapping: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service '" + service.getServiceName() +
                "' to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " ($" + s.getCost() + ")");
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;

        List<Service> services = serviceMap.get(reservationId);

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// Main class
public class BookMyStayapp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("====================================");
        System.out.println("Version: 7.0\n");

        // Assume reservation IDs from previous use case
        String reservation1 = "SI1";
        String reservation2 = "SU2";

        // Initialize service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 20.0);
        Service wifi = new Service("WiFi", 10.0);
        Service spa = new Service("Spa Access", 50.0);

        // Add services to reservations
        serviceManager.addService(reservation1, breakfast);
        serviceManager.addService(reservation1, wifi);

        serviceManager.addService(reservation2, spa);

        // Display services
        serviceManager.displayServices(reservation1);
        System.out.println("Total Add-On Cost: $" +
                serviceManager.calculateTotalCost(reservation1));

        serviceManager.displayServices(reservation2);
        System.out.println("Total Add-On Cost: $" +
                serviceManager.calculateTotalCost(reservation2));

        System.out.println("\nAdd-on services processed successfully!");
    }
}