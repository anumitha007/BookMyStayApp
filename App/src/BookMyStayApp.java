import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {
    private HashMap<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 2);
        availability.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return availability.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " Rooms Available: " + availability.get(type));
        }
    }
}

class BookingService {

    private Queue<Reservation> requestQueue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    private int idCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, RoomInventory inventory) {
        this.requestQueue = requestQueue;
        this.inventory = inventory;
    }

    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            Reservation r = requestQueue.poll();
            String roomType = r.roomType;

            if (inventory.isAvailable(roomType)) {

                String roomId = roomType.substring(0,1).toUpperCase() + idCounter++;
                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(roomType, new HashSet<>());
                roomAllocations.get(roomType).add(roomId);

                inventory.decrement(roomType);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + r.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("No available rooms for " + roomType + " for guest " + r.guestName);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Alice", "Single"));
        bookingQueue.add(new Reservation("Bob", "Double"));
        bookingQueue.add(new Reservation("Charlie", "Suite"));
        bookingQueue.add(new Reservation("David", "Suite"));

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService(bookingQueue, inventory);

        bookingService.processBookings();

        inventory.displayInventory();
    }
}