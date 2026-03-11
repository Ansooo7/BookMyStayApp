import java.util.*;

/**
 * Reservation
 * Represents a guest’s confirmed booking.
 */
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public void displayConfirmation() {
        System.out.println("Reservation Confirmed!");
        System.out.println("Guest: " + guestName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Assigned Room ID: " + roomId);
        System.out.println();
    }
}

/**
 * Inventory Service
 * Maintains and updates room availability state.
 */
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 2);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            availability.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("=== Current Inventory ===");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " - Available: " + entry.getValue());
        }
        System.out.println();
    }
}

/**
 * Booking Service
 * Processes queued requests and performs room allocation.
 */
class BookingService {
    private Queue<String[]> requestQueue;
    private Map<String, Set<String>> allocatedRooms;
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.requestQueue = new LinkedList<>();
        this.allocatedRooms = new HashMap<>();
    }

    // Add booking request to queue
    public void addRequest(String guestName, String roomType) {
        requestQueue.add(new String[]{guestName, roomType});
        System.out.println("Request queued for " + guestName + " (" + roomType + ")");
    }

    // Process requests in FIFO order
    public void processRequests() {
        while (!requestQueue.isEmpty()) {
            String[] request = requestQueue.poll();
            String guestName = request[0];
            String roomType = request[1];

            if (inventory.decrementAvailability(roomType)) {
                // Generate unique room ID
                String roomId = UUID.randomUUID().toString();

                // Ensure uniqueness with Set
                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                // Confirm reservation
                Reservation reservation = new Reservation(guestName, roomType, roomId);
                reservation.displayConfirmation();
            } else {
                System.out.println("Sorry, " + guestName + ". No " + roomType + " available.\n");
            }
        }
    }
}

/**
 * Application Entry Point
 */
public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Application v6.0 ===\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Initial inventory state
        inventory.displayInventory();

        // Guests submit requests
        bookingService.addRequest("Alice", "Single Room");
        bookingService.addRequest("Bob", "Suite Room");
        bookingService.addRequest("Charlie", "Double Room");
        bookingService.addRequest("Diana", "Single Room");
        bookingService.addRequest("Eve", "Single Room"); // Will fail if inventory exhausted

        // Process requests (FIFO)
        System.out.println("\nProcessing requests...\n");
        bookingService.processRequests();

        // Final inventory state
        inventory.displayInventory();
    }
}
