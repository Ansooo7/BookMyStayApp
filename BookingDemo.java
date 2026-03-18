import java.util.*;

// Core reservation class (unchanged)
class Reservation {
    private String reservationId;
    private String guestName;
    private double baseCost;

    public Reservation(String reservationId, String guestName, double baseCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.baseCost = baseCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getBaseCost() {
        return baseCost;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", baseCost=" + baseCost +
                '}';
    }
}

// Optional service class
class Service {
    private String serviceId;
    private String name;
    private double cost;

    public Service(String serviceId, String name, double cost) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Manager for add-on services
class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Attach a service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServices
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);
    }

    // Retrieve services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }

    // Calculate total additional cost
    public double calculateAdditionalCost(String reservationId) {
        return getServices(reservationId).stream()
                .mapToDouble(Service::getCost)
                .sum();
    }

    // Calculate full cost (base + services)
    public double calculateTotalCost(Reservation reservation) {
        return reservation.getBaseCost() + calculateAdditionalCost(reservation.getReservationId());
    }
}

// Demo
public class BookingDemo {
    public static void main(String[] args) {
        Reservation reservation = new Reservation("R001", "Alice", 5000);

        Service breakfast = new Service("S001", "Breakfast", 500);
        Service airportPickup = new Service("S002", "Airport Pickup", 1200);

        AddOnServiceManager manager = new AddOnServiceManager();
        manager.addService(reservation.getReservationId(), breakfast);
        manager.addService(reservation.getReservationId(), airportPickup);

        System.out.println(reservation);
        System.out.println("Selected Services: " + manager.getServices(reservation.getReservationId()));
        System.out.println("Additional Cost: ₹" + manager.calculateAdditionalCost(reservation.getReservationId()));
        System.out.println("Total Cost: ₹" + manager.calculateTotalCost(reservation));
    }
}
