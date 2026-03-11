/**
 * Abstract Room class
 * Represents the generalized concept of a hotel room.
 * Common attributes: type, beds, size, price.
 */
abstract class Room {
    protected String type;
    protected int beds;
    protected double size; // in square meters
    protected double price; // per night

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per night: $" + price);
    }
}

/**
 * Concrete room classes extending Room
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
 * Application Entry Point
 */
public class UseCase2RoomInitialization {
    public static void main(String[] args) {
        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability representation
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display room details and availability
        System.out.println("=== Hotel Booking Application v2.0 ===\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");

        // Application terminates
    }
}
