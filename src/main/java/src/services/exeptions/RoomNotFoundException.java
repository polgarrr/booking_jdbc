package src.services.exeptions;

public class RoomNotFoundException extends Exception {
        public RoomNotFoundException(String message) {
            super(message);
        }
        public RoomNotFoundException() {
            super("Room with this ID is not found.");
        }
}
