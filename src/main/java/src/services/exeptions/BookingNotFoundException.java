package src.services.exeptions;

public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(String message) {
        super(message);
    }
}
