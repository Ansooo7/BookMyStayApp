import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation
 * Represents a guest’s intent to book a room.
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

/**
 * BookingRequestQueue
 * Manages and orders incoming booking requests using FIFO principle.
 */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Accept a new booking request
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // Display queued requests (read-only, no allocation yet)
    public void displayQueue() {
        System.out.println("\n=== Current Booking Request Queue ===");
        for (Reservation reservation : requestQueue) {
            reservation.displayRequest();
        }
    }
}

/**
 * Application Entry Point
 */
public class UseCase5BookingQueue {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Application v5.0 ===\n");

        // Initialize booking request queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Suite Room"));
        queue.addRequest(new Reservation("Charlie", "Double Room"));

        // Display queue state (FIFO order preserved)
        queue.displayQueue();

        // Application terminates (no inventory mutation yet)
    }
}
