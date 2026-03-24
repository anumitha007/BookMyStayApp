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
    Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
    }

    public boolean isAvailable(String type) {
        return rooms.getOrDefault(type, 0) > 0;
    }

    public void allocate(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public void display() {
        System.out.println("Inventory: " + rooms);
    }
}

class AllocationService {

    public void allocateRoom(Reservation r, RoomInventory inventory) {

        if (inventory.isAvailable(r.roomType)) {
            inventory.allocate(r.roomType);

            System.out.println(Thread.currentThread().getName() +
                    " booked " + r.roomType +
                    " for " + r.guestName);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED for " + r.guestName +
                    " (No " + r.roomType + " rooms)");
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;
    private AllocationService allocationService;

    public ConcurrentBookingProcessor(Queue<Reservation> bookingQueue,
                                      RoomInventory inventory,
                                      AllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {

            Reservation reservation;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.poll();
            }

            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single"));
        queue.add(new Reservation("Bob", "Single"));
        queue.add(new Reservation("Charlie", "Single"));
        queue.add(new Reservation("David", "Double"));
        queue.add(new Reservation("Eve", "Double"));

        RoomInventory inventory = new RoomInventory();
        AllocationService allocationService = new AllocationService();

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, allocationService), "Thread-1");
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, allocationService), "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Inventory State:");
        inventory.display();
    }
}