import java.util.*;

class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

class BookingReportService {

    public void displayAllBookings(List<Reservation> history) {

        System.out.println("\n--- Booking History ---");

        for (Reservation r : history) {
            r.display();
        }
    }

    public void generateSummary(List<Reservation> history) {

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : history) {
            roomTypeCount.put(r.roomType,
                    roomTypeCount.getOrDefault(r.roomType, 0) + 1);
        }

        System.out.println("\n--- Booking Summary Report ---");

        for (String room : roomTypeCount.keySet()) {
            System.out.println(room + " Rooms Booked: " + roomTypeCount.get(room));
        }

        System.out.println("Total Reservations: " + history.size());
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();

        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Double");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite");

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        BookingReportService reportService = new BookingReportService();

        reportService.displayAllBookings(bookingHistory.getHistory());
        reportService.generateSummary(bookingHistory.getHistory());
    }
}