import src.entities.Booking;
import src.entities.Room;
import src.entities.User;
import src.repository.BookingRepository;
import src.repository.RoomRepository;
import src.repository.UserRepository;
import src.repository.impl.BookingRepositoryImpl;
import src.repository.impl.RoomRepositoryImpl;
import src.repository.impl.UserRepositoryImpl;
import src.services.exeptions.BookingNotFoundException;
import src.services.exeptions.RoomNotFoundException;
import src.services.exeptions.UserNotFoundException;


import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {
        User user = new User(UUID.randomUUID().toString(), "79991696969", "love.ru", "Margo", "Rita", "YYY", new ArrayList<>());
        User user1 = new User(UUID.randomUUID().toString(), "79999999999", "ove.ru", "Dje", "Ya", "Ny", new ArrayList<>());
        Room room = new Room(UUID.randomUUID().toString(), "200", 22, "super", "super room", 9999, new ArrayList<>());
        Room room1 = new Room(UUID.randomUUID().toString(), "400", 44, "bad", "bad room", 100, new ArrayList<>());
        Booking booking = new Booking(UUID.randomUUID().toString(), new Date(Instant.now().toEpochMilli()), new Date(Instant.now().toEpochMilli()), user, room);
        Booking booking1 = new Booking(UUID.randomUUID().toString(), new Date(Instant.now().toEpochMilli()), new Date(Instant.now().toEpochMilli()), user1, room1);

        UserRepository userRepository = new UserRepositoryImpl();
        try {
            userRepository.delete("098101ce-3d0d-4a63-9983-28d3564d5110");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        userRepository.create(user1);
        user1.setEmail("nooo.god.please.noooo");
        userRepository.update(user1);

        try {
            userRepository.getUser("098101ce-3d0d-4a63-9983-28d3564d5110");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }


        RoomRepository roomRepository = new RoomRepositoryImpl();
        roomRepository.createRoom(room);

        try {
            roomRepository.deleteRoom("098101ce-3d0d-4a63-9983-28d3564d5110");
        } catch (RoomNotFoundException e) {
            e.printStackTrace();
        }


        roomRepository.createRoom(room1);
        room1.setRoomType("good enough");
        roomRepository.updateRoom(room1);

        try {
            roomRepository.getByRoom("001");
        } catch (RoomNotFoundException e) {
            e.printStackTrace();
        }

        BookingRepository bookingRepository = new BookingRepositoryImpl();
        userRepository.create(user);
        bookingRepository.createBooking(booking);

        bookingRepository.createBooking(booking1);

        try {
            bookingRepository.deleteBooking("098101ce-3d0d-4a63-9983-28d3564d5110");
        } catch (BookingNotFoundException e) {
            e.printStackTrace();
        }


        try {
            bookingRepository.getByBooking("001");
        } catch (BookingNotFoundException e) {
            e.printStackTrace();
        }

    }
}
