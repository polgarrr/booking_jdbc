package src.services;

import src.entities.Booking;
import src.services.exeptions.BookingNotFoundException;
import src.services.exeptions.RequiredFieldMissedException;
import src.services.exeptions.RoomNotFoundException;

import java.sql.Date;
import java.util.List;

public interface BookingService {
    /**
     * Создать бронирование.
     *
     * @param booking бронирование
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return созданное бронирование с присвоенным идентификатором
     */
    Booking createBooking(Booking booking) throws RequiredFieldMissedException;

    /**
     * Обновить бронирование.
     *
     * @param booking обновленное бронирование
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return обновленное бронирование
     */
    Booking updateBooking(Booking booking) throws RequiredFieldMissedException;

    /**
     * Удалить бронирование.
     *
     * @param id бронирование
     * @throws BookingNotFoundException если бронирование с таким id не найдено
     */
    void deleteBooking(String id);

    /**
     * Получить бронирование по идентификатору.
     *
     * @param id идентификатор
     * @throws BookingNotFoundException если бронирование с таким id не найдено
     * @return бронирование
     */
    Booking getByBooking(String id) throws BookingNotFoundException;

    /**
     * Получить бронирование с фильтрацией по дате заезда/выезда.
     *
     * @param checkInFrom заезд от
     * @param checkInTo заезд до
     * @param checkOutFrom отъезд от
     * @param checkOutTo отъезд до
     * @throws BookingNotFoundException если бронирование с такими параметрами не найдено
     * @return бронирование
     */
    List<Booking> getBookings(Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo);
}
