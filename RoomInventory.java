import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory
 * 
 * Manages availability of different room types in a centralized structure.
 * Demonstrates how HashMap provides fast lookups and updates.
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    // Initialize inventory with predefined room types
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Retrieve availability for a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability in a controlled manner
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Display current inventory state
    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " - Available: " + entry.getValue());
        }
    }
}

/**
 * Application Entry Point
 */
public class UseCase3CentralizedInventory {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Application v3.0 ===\n");

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial state
        inventory.displayInventory();

        // Controlled update example
        System.out.println("\nUpdating availability...");
        inventory.updateAvailability("Double Room", 4);

        // Display updated state
        inventory.displayInventory();

        // Application terminates
    }
}
