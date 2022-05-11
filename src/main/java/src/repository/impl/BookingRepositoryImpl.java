package src.repository.impl;

import src.entities.Booking;
import src.entities.Room;
import src.entities.User;
import src.repository.BookingRepository;
import src.services.exeptions.BookingNotFoundException;
import src.services.exeptions.InternalServiceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {

    @Override
    public Booking createBooking(Booking booking) {
        try (Connection connection = getNewConnection()) {
            String queryCreateB = "insert into booking.bookings (id, check_in, check_out, user_id, room_id) values (?, ?, ?, ?, ?)";
            PreparedStatement statementCrtB = connection.prepareStatement(queryCreateB);
            statementCrtB.setString(1, booking.getId());
            statementCrtB.setDate(2, booking.getCheckIn());
            statementCrtB.setDate(3, booking.getCheckOut());
            statementCrtB.setString(4, booking.getUser().getId());
            statementCrtB.setString(5, booking.getRoom().getId());
            statementCrtB.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        try (Connection connection = getNewConnection()) {
            String queryUpdateB = "update booking.bookings set check_in = ?, check_out = ?, user_id = ?, room_id = ? where id = ?";
            PreparedStatement statementUpdB = connection.prepareStatement(queryUpdateB);
            statementUpdB.setDate(1, booking.getCheckIn());
            statementUpdB.setDate(2, booking.getCheckOut());
            statementUpdB.setString(3, booking.getUser().getId());
            statementUpdB.setString(4, booking.getRoom().getId());
            statementUpdB.setString(5, booking.getId());
            statementUpdB.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return booking;
    }

    @Override
    public void deleteBooking(String id) {
        try (Connection connection = getNewConnection()) {
            String queryDeleteB = "delete from booking.bookings where id = ?";
            PreparedStatement statementDltB = connection.prepareStatement(queryDeleteB);
            statementDltB.setString(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public Booking getByBooking(String id) throws BookingNotFoundException {
        try (Connection connection = getNewConnection()) {
            String queryGetByB = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where b.id = ?";
            PreparedStatement statementGetByB = connection.prepareStatement(queryGetByB);
            statementGetByB.setString(1, id);
            ResultSet resultSetB = statementGetByB.getResultSet();
            if (resultSetB == null || resultSetB.getRow() == 0) {
                throw new BookingNotFoundException(id);
            }
            while (resultSetB.next()) {
                String bookingId = resultSetB.getString("booking_id");
                Date checkIn = resultSetB.getDate("check_in");
                Date checkOut = resultSetB.getDate("check_out");
                String roomId = resultSetB.getString("room_id");
                String roomNumber = resultSetB.getString("room_number");
                Integer floor = resultSetB.getInt("floor");
                String roomType = resultSetB.getString("room_type");
                String description = resultSetB.getString("description");
                Integer price = resultSetB.getInt("price");
                String userId = resultSetB.getString("user_id");
                String phone = resultSetB.getString("phone");
                String email = resultSetB.getString("email");
                String firstName = resultSetB.getString("first_name");
                String lastName = resultSetB.getString("last_name");
                String middleName = resultSetB.getString("middle_name");
                User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                return booking;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Booking> getBookings(Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo) {
        try (Connection connection = getNewConnection()) {
            String queryGetBookings = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where check_in >= ? and check_in <= ? and check_out >= ? and check_out <= ?";
            PreparedStatement statementGetBookings = connection.prepareStatement(queryGetBookings);
            statementGetBookings.setDate(1, checkInFrom);
            statementGetBookings.setDate(2, checkInTo);
            statementGetBookings.setDate(3, checkOutFrom);
            statementGetBookings.setDate(4, checkOutTo);
            ResultSet resultSetGetBookings = statementGetBookings.getResultSet();
            List<Booking> bookings = new ArrayList<>();
            while (resultSetGetBookings.next()) {
                String bookingId = resultSetGetBookings.getString("booking_id");
                Date checkIn = resultSetGetBookings.getDate("check_in");
                Date checkOut = resultSetGetBookings.getDate("check_out");
                String roomId = resultSetGetBookings.getString("room_id");
                String roomNumber = resultSetGetBookings.getString("room_number");
                Integer floor = resultSetGetBookings.getInt("floor");
                String roomType = resultSetGetBookings.getString("room_type");
                String description = resultSetGetBookings.getString("description");
                Integer price = resultSetGetBookings.getInt("price");
                String userId = resultSetGetBookings.getString("user_id");
                String phone = resultSetGetBookings.getString("phone");
                String email = resultSetGetBookings.getString("email");
                String firstName = resultSetGetBookings.getString("first_name");
                String lastName = resultSetGetBookings.getString("last_name");
                String middleName = resultSetGetBookings.getString("middle_name");
                User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                bookings.add(booking);
            }
            return bookings;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }


    private Connection getNewConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/macbook";
        String user = "macbook";
        String passwd = "colidh10b";
        return DriverManager.getConnection(url, user, passwd);
    }
}
