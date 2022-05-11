package src.repository;

import src.entities.Booking;
import src.services.exeptions.BookingNotFoundException;

import java.sql.Date;
import java.util.List;

public interface BookingRepository {
    Booking createBooking(Booking booking);
    Booking updateBooking(Booking booking);
    void deleteBooking(String id) throws BookingNotFoundException;
    Booking getByBooking(String id) throws BookingNotFoundException;
    List<Booking> getBookings(Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo);
}
