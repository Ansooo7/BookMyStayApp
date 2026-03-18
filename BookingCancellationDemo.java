import java.util.*;

// Reservation class (simplified for cancellation demo)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean confirmed;
    private boolean cancelled;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.confirmed = true;   // assume confirmed when created
        this.cancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isConfirmed() {
        return confirmed && !cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", confirmed=" + confirmed +
                ", cancelled=" + cancelled +
                '}';
    }
}

// Booking History: stores confirmed reservations
class BookingHistory {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }

    public Reservation findReservation(String reservationId) {
        return confirmedBookings.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }
}

// Cancellation Service: validates and performs rollback
class CancellationService {
    private Map<String, Integer> inventory;
    private Stack<String> rollbackStack = new Stack<>();
    private BookingHistory bookingHistory;

    public CancellationService(Map<String, Integer> inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public void cancelBooking(String reservationId) {
        Reservation reservation = bookingHistory.findReservation(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        if (!reservation.isConfirmed()) {
            System.out.println("Cancellation failed: Reservation already cancelled or invalid.");
            return;
        }

        // Record rollback info
        rollbackStack.push(reservation.getReservationId());

        // Restore inventory
        String roomType = reservation.getRoomType();
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);

        // Update booking history
        reservation.cancel();

        System.out.println("Cancellation successful for reservation: " + reservationId);
        System.out.println("Inventory restored for room type: " + roomType);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (recent cancellations): " + rollbackStack);
    }
}

// Demo
public class BookingCancellationDemo {
    public static void main(String[] args) {
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("R001", "Alice", "Single");
        Reservation r2 = new Reservation("R002", "Bob", "Double");

        history.addBooking(r1);
        history.addBooking(r2);

        CancellationService cancellationService = new CancellationService(inventory, history);

        // Valid cancellation
        cancellationService.cancelBooking("R001");

        // Attempt duplicate cancellation
        cancellationService.cancelBooking("R001");

        // Attempt cancellation of non-existent booking
        cancellationService.cancelBooking("R999");

        cancellationService.showRollbackStack();
    }
}
