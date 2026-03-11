import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Room class
 */
abstract class Room {
    protected String type;
    protected int beds;
    protected double size;
    protected double price;

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per night: $" + price);
    }

    public String getType() {
        return type;
    }
}

/**
 * Concrete Room classes
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 20.0, 50.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 35.0, 90.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 60.0, 200.0);
    }
}

/**
 * Centralized Inventory
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // Example: unavailable
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/**
 * Search Service - Read-only access
 */
class RoomSearchService {
    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {
        System.out.println("=== Available Rooms ===");
        for (Room room : rooms) {
            int availability = inventory.getAvailability(room.getType());
            if (availability > 0) { // Defensive check
                room.displayDetails();
                System.out.println("Available: " + availability + "\n");
            }
        }
    }
}

/**
 * Application Entry Point
 */
public class UseCase4RoomSearch {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Application v4.0 ===\n");

        // Initialize inventory and rooms
        RoomInventory inventory = new RoomInventory();
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };

        // Search service (read-only)
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest initiates search
        searchService.searchAvailableRooms(rooms);

        // Application terminates
    }
}
