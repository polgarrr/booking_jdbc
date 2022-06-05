package src.repository;

import src.entities.Booking;
import src.services.exeptions.BookingNotFoundException;

import java.sql.Date;
import java.util.List;

public interface BookingRepository {
    Booking create(Booking booking);
    Booking update(Booking booking);
    void deleteBy(String id) throws BookingNotFoundException;
    Booking getBy(String id) throws BookingNotFoundException;
    List<Booking> getBookingsBy(Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo);
}
