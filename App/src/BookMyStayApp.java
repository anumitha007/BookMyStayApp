import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

class RoomInventory implements Serializable {
    Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public void display() {
        System.out.println("Inventory: " + rooms);
    }
}

class SystemState implements Serializable {
    List<Reservation> bookingHistory;
    RoomInventory inventory;

    public SystemState(List<Reservation> bookingHistory, RoomInventory inventory) {
        this.bookingHistory = bookingHistory;
        this.inventory = inventory;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state.");
        }
    }

    public SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (Exception e) {
            System.out.println("No saved data found. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        SystemState state = persistence.load();

        List<Reservation> history;
        RoomInventory inventory;

        if (state == null) {
            history = new ArrayList<>();
            inventory = new RoomInventory();
        } else {
            history = state.bookingHistory;
            inventory = state.inventory;
        }

        history.add(new Reservation("R101", "Alice", "Single"));
        history.add(new Reservation("R102", "Bob", "Double"));

        System.out.println("\nCurrent Bookings:");
        for (Reservation r : history) {
            r.display();
        }

        System.out.println();
        inventory.display();

        persistence.save(new SystemState(history, inventory));
    }
}