import java.util.*;

class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public void decrement(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public void increment(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (String r : rooms.keySet()) {
            System.out.println(r + " Rooms Available: " + rooms.get(r));
        }
    }
}

class BookingHistory {

    Map<String, Reservation> confirmedBookings = new HashMap<>();

    public void addBooking(Reservation r) {
        confirmedBookings.put(r.reservationId, r);
    }

    public Reservation getBooking(String id) {
        return confirmedBookings.get(id);
    }

    public void removeBooking(String id) {
        confirmedBookings.remove(id);
    }
}

class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelReservation(String reservationId,
                                  BookingHistory history,
                                  RoomInventory inventory) {

        Reservation r = history.getBooking(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        rollbackStack.push(r.roomId);

        inventory.increment(r.roomType);

        history.removeBooking(reservationId);

        System.out.println("Reservation Cancelled Successfully");
        System.out.println("Released Room ID: " + r.roomId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Released Room IDs): " + rollbackStack);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        Reservation r1 = new Reservation("R101", "Alice", "Single", "S1");

        history.addBooking(r1);

        inventory.decrement("Single");

        System.out.println("Booking Confirmed: R101 for Alice (Room S1)");

        inventory.display();

        System.out.println("\nGuest requests cancellation...\n");

        cancelService.cancelReservation("R101", history, inventory);

        inventory.display();

        cancelService.showRollbackStack();
    }
}