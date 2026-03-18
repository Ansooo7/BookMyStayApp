import java.util.*;

// Custom exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class (simplified for validation demo)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
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

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", nights=" + nights +
                '}';
    }
}

// Validator class to check booking input and system state
class InvalidBookingValidator {
    private static final Set<String> VALID_ROOM_TYPES = Set.of("Single", "Double", "Suite");
    private Map<String, Integer> inventory;

    public InvalidBookingValidator(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void validate(Reservation reservation) throws InvalidBookingException {
        // Validate room type
        if (!VALID_ROOM_TYPES.contains(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate nights
        if (reservation.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero.");
        }

        // Validate inventory availability
        int available = inventory.getOrDefault(reservation.getRoomType(), 0);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + reservation.getRoomType());
        }
    }

    // Update inventory safely
    public void allocateRoom(Reservation reservation) throws InvalidBookingException {
        validate(reservation); // fail-fast validation
        int available = inventory.get(reservation.getRoomType());
        if (available - 1 < 0) {
            throw new InvalidBookingException("Cannot allocate room. Inventory would go negative.");
        }
        inventory.put(reservation.getRoomType(), available - 1);
    }
}

// Demo
public class BookingValidationDemo {
    public static void main(String[] args) {
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 0); // no suites available

        InvalidBookingValidator validator = new InvalidBookingValidator(inventory);

        // Valid booking
        Reservation r1 = new Reservation("R001", "Alice", "Single", 3);

        // Invalid booking (wrong room type)
        Reservation r2 = new Reservation("R002", "Bob", "Penthouse", 2);

        // Invalid booking (no availability)
        Reservation r3 = new Reservation("R003", "Charlie", "Suite", 1);

        try {
            validator.allocateRoom(r1);
            System.out.println("Booking confirmed: " + r1);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            validator.allocateRoom(r2);
            System.out.println("Booking confirmed: " + r2);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            validator.allocateRoom(r3);
            System.out.println("Booking confirmed: " + r3);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}
