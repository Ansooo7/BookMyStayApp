import java.util.*;

// Core reservation class (unchanged from earlier use cases)
class Reservation {
    private String reservationId;
    private String guestName;
    private double totalCost;

    public Reservation(String reservationId, String guestName, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.totalCost = totalCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", totalCost=" + totalCost +
                '}';
    }
}

// Booking History: stores confirmed reservations in insertion order
class BookingHistory {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    // Add confirmed reservation
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

// Reporting Service: generates summaries and reports
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Generate summary report
    public void generateSummaryReport() {
        List<Reservation> bookings = bookingHistory.getAllBookings();
        System.out.println("=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + bookings.size());

        double totalRevenue = bookings.stream()
                .mapToDouble(Reservation::getTotalCost)
                .sum();

        System.out.println("Total Revenue: ₹" + totalRevenue);
        System.out.println("Bookings Detail:");
        for (Reservation r : bookings) {
            System.out.println(" - " + r);
        }
    }
}

// Demo
public class BookingHistoryDemo {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R001", "Alice", 6200);
        Reservation r2 = new Reservation("R002", "Bob", 5000);
        Reservation r3 = new Reservation("R003", "Charlie", 7500);

        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);

        // Admin requests report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();
    }
}
