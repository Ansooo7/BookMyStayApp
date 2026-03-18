import java.util.*;
import java.util.concurrent.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                '}';
    }
}

// Concurrent Booking Processor
class ConcurrentBookingProcessor {
    private Map<String, Integer> inventory;
    private Queue<Reservation> bookingQueue = new LinkedList<>();

    public ConcurrentBookingProcessor(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    // Add booking request to queue
    public synchronized void addBookingRequest(Reservation reservation) {
        bookingQueue.offer(reservation);
    }

    // Process booking requests safely
    public void processBookings() {
        while (true) {
            Reservation reservation;
            synchronized (this) {
                reservation = bookingQueue.poll();
            }
            if (reservation == null) break; // no more requests

            synchronized (inventory) { // critical section
                String roomType = reservation.getRoomType();
                int available = inventory.getOrDefault(roomType, 0);

                if (available > 0) {
                    inventory.put(roomType, available - 1);
                    System.out.println("Booking confirmed: " + reservation);
                } else {
                    System.out.println("Booking failed (no availability): " + reservation);
                }
            }
        }
    }
}

// Demo
public class ConcurrentBookingDemo {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory);

        // Simulate multiple guests submitting requests concurrently
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> processor.addBookingRequest(new Reservation("R001", "Alice", "Single")));
        executor.submit(() -> processor.addBookingRequest(new Reservation("R002", "Bob", "Single")));
        executor.submit(() -> processor.addBookingRequest(new Reservation("R003", "Charlie", "Double")));
        executor.submit(() -> processor.addBookingRequest(new Reservation("R004", "Diana", "Double"))); // overbooking attempt

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        // Process all requests safely
        processor.processBookings();

        System.out.println("Final Inventory: " + inventory);
    }
}
