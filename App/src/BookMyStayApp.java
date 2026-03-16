import java.util.HashMap;
import java.util.Map;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void checkAvailability(String roomType) throws InvalidBookingException {
        if (rooms.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        int available = rooms.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("Inventory cannot go negative.");
        }

        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " Rooms Available: " + rooms.get(type));
        }
    }
}

class BookingValidator {

    public static void validateGuestName(String name) throws InvalidBookingException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        String guestName = "Alice";
        String roomType = "Single";

        try {

            BookingValidator.validateGuestName(guestName);

            inventory.validateRoomType(roomType);

            inventory.checkAvailability(roomType);

            inventory.allocateRoom(roomType);

            System.out.println("Booking successful for " + guestName + " (Room: " + roomType + ")");

        } catch (InvalidBookingException e) {

            System.out.println("Booking Failed: " + e.getMessage());

        }

        inventory.displayInventory();
    }
}