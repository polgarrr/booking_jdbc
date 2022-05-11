//package src.services.impl;
//
//import entities.Booking;
//import entities.Room;
//import repository.BookingRepository;
//import services.BookingService;
//import services.RoomService;
//import services.exeptions.BookingNotFoundException;
//import services.exeptions.RequiredFieldMissedException;
//import services.exeptions.RoomNotFoundException;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class BookingServiceImpl implements BookingService {
//    private final BookingRepository bookingRepository;
//    private final RoomService roomService;
//
//    // конструктор
//    public BookingServiceImpl(BookingRepository bookingRepository, RoomService roomService) {
//        this.bookingRepository = bookingRepository;
//        this.roomService = roomService;
//    }
//
//    @Override
//    public Booking getBy(String id) throws BookingNotFoundException {
//        // получить бронирование по идентификатору, если не найдено -- выкинуть
//        // BookingNotFoundException (его нужно создать)
//        Booking booking = bookingRepository.getBy(id);
//        if (booking == null) {
//            throw new BookingNotFoundException("Booking with id: " + id + " not found");
//        }
//        return booking;
//    }
//
//    // здесь нужно с помощью bookingRepository создать бронирование
//    @Override
//    public Booking createBooking(Booking booking) throws RoomNotFoundException, RequiredFieldMissedException {
//        // проверить, что checkIn, checkOut, guest и room != null -- иначе выкинуть RequiredFieldMissedException
//
//        if (booking.getCheckIn() == null || booking.getCheckOut() == null || booking.getGuest() == null || booking.getRoom() == null) {
//            throw new RequiredFieldMissedException("Booking is not completed with all required fields: checkIn, checkOut, guest, room");
//        }
//        // проверить, что переданная комната существует -- иначе выкинуть RoomNotFoundException
//        Room bookingRoom = roomService.getBy(booking.getRoom().getId());
//        // обновить комнату, переданную в запросе на создание бронирования, добавив это самое
//        // бронирование в поле bookings комнаты
//        Set<Booking> bookings = new HashSet<>();
//        bookings.add(booking);
//        bookingRoom.setBookings(bookings);
//        roomService.updateRoom(bookingRoom.getId(), bookingRoom);
//        return booking;
//    }
//
//    @Override
//    public Booking updateBooking(
//            String id, Booking booking
//    ) throws RoomNotFoundException, RequiredFieldMissedException, BookingNotFoundException {
//        // проверить, что заполнено поле id -- иначе выкинуть RequiredFieldMissedException
//        // проверить, что такое бронирование существует -- иначе выкинуть BookingNotFoundException
//        // обновить с помощью bookingRepository данные бронирования
//        if (id == null) {
//            throw new RequiredFieldMissedException("Id is required to update the booking");
//        }
//        getBy(booking.getId());
//        Booking savedBooking = bookingRepository.save(booking);
//        // если переданное поле room не равно null, проверить, что комната с таким id существует --
//        // иначе выкинуть RoomNotFoundException
//        // обновить данные комнат, удалив бронирование из прежней комнаты и поместив его в новую
//        if (savedBooking.getRoom() != null) {
//            try {
//                Room room = roomService.getBy(savedBooking.getRoom().getId());
//                Set<Booking> bookings = new HashSet<>();
//                bookings.add(savedBooking);
//                room.setBookings(bookings);
//                roomService.updateRoom(room.getId(), room);
//            } catch (RoomNotFoundException e) {
//                e.printStackTrace();
//                throw e;
//            }
//        }
//        return booking;
//    }
//
//    @Override
//    public void deleteBooking(Booking booking) {
//        // удалить бронирование с помощью bookingRepository
//        bookingRepository.delete(booking);
//
//        // удалить бронирование из комнаты в которой оно было
//        try {
//            Room bookedRoom = roomService.getBy(booking.getRoom().getId());
//            bookedRoom.setBookings(new HashSet<>());
//        } catch (RoomNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
