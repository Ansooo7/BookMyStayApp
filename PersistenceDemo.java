import java.io.*;
import java.util.*;

// Reservation class (Serializable for persistence)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                '}';
    }
}

// BookingHistory class (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "system_state.dat";

    // Save state (BookingHistory + Inventory)
    public void saveState(BookingHistory history, Map<String, Integer> inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(history);
            oos.writeObject(inventory);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    @SuppressWarnings("unchecked")
    public Object[] loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            BookingHistory history = (BookingHistory) ois.readObject();
            Map<String, Integer> inventory = (Map<String, Integer>) ois.readObject();
            System.out.println("System state loaded successfully.");
            return new Object[]{history, inventory};
        } catch (FileNotFoundException e) {
            System.out.println("No persistence file found. Starting fresh.");
            return new Object[]{new BookingHistory(), new HashMap<String, Integer>()};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return new Object[]{new BookingHistory(), new HashMap<String, Integer>()};
        }
    }
}

// Demo
public class PersistenceDemo {
    public static void main(String[] args) {
        PersistenceService persistenceService = new PersistenceService();

        // Simulate system startup (load state)
        Object[] state = persistenceService.loadState();
        BookingHistory history = (BookingHistory) state[0];
        Map<String, Integer> inventory = (Map<String, Integer>) state[1];

        // Initialize inventory if empty
        if (inventory.isEmpty()) {
            inventory.put("Single", 2);
            inventory.put("Double", 1);
        }

        // Add new booking
        Reservation r1 = new Reservation("R001", "Alice", "Single");
        history.addBooking(r1);
        inventory.put("Single", inventory.get("Single") - 1);

        // Show current state
        System.out.println("Current Bookings: " + history.getAllBookings());
        System.out.println("Current Inventory: " + inventory);

        // Simulate system shutdown (save state)
        persistenceService.saveState(history, inventory);
    }
}
