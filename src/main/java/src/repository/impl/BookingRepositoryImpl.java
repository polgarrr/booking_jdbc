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
    public Booking create(Booking booking) {
        try (Connection connection = getNewConnection()) {
            String createBookingQuery = "insert into booking.bookings (id, check_in, check_out, user_id, room_id) values (?, ?, ?, ?, ?)";
            PreparedStatement createBookingStatement = connection.prepareStatement(createBookingQuery);
            buildCreateBookingStatement(createBookingStatement, booking); // и так аналогично надо везде в принципе
            createBookingStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return booking;
    }

    private void buildCreateBookingStatement(PreparedStatement createBookingStatement, Booking booking) throws SQLException {
        createBookingStatement.setString(1, booking.getId());
        createBookingStatement.setDate(2, booking.getCheckIn());
        createBookingStatement.setDate(3, booking.getCheckOut());
        createBookingStatement.setString(4, booking.getUser().getId());
        createBookingStatement.setString(5, booking.getRoom().getId());
    }

    @Override
    public Booking update(Booking booking) {
        try (Connection connection = getNewConnection()) {
            String updateBookingQuery = "update booking.bookings set check_in = ?, check_out = ?, user_id = ?, room_id = ? where id = ?";
            PreparedStatement updateBookingStatement = connection.prepareStatement(updateBookingQuery);
            buildUpdateBookingStatement(updateBookingStatement, booking);
            updateBookingStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return booking;
    }

    private void buildUpdateBookingStatement(PreparedStatement updateBookingStatement, Booking booking) throws SQLException {
        updateBookingStatement.setDate(1, booking.getCheckIn());
        updateBookingStatement.setDate(2, booking.getCheckOut());
        updateBookingStatement.setString(3, booking.getUser().getId());
        updateBookingStatement.setString(4, booking.getRoom().getId());
        updateBookingStatement.setString(5, booking.getId());
    }

    @Override
    public void deleteBy(String id) {
        try (Connection connection = getNewConnection()) {
            String deleteBookingQuery = "delete from booking.bookings where id = ?";
            PreparedStatement deleteBookingStatement = connection.prepareStatement(deleteBookingQuery);
            deleteBookingStatement.setString(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public Booking getBy(String id) throws BookingNotFoundException {
        try (Connection connection = getNewConnection()) {
            String getBookingQuery = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where b.id = ?";
            PreparedStatement getBookingStatement = connection.prepareStatement(getBookingQuery);
            getBookingStatement.setString(1, id);
            ResultSet bookingResultSet = getBookingStatement.getResultSet();
            if (bookingResultSet == null || bookingResultSet.getRow() == 0) {
                throw new BookingNotFoundException(id);
            }
            while (bookingResultSet.next()) {
                return createBookingFrom(bookingResultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return null;
    }

    private Booking createBookingFrom(ResultSet resultSet) throws SQLException { // и далее как-то аналогично. большие функции надо разбивать
        String bookingId = resultSet.getString("booking_id");
        Date checkIn = resultSet.getDate("check_in");
        Date checkOut = resultSet.getDate("check_out");
        String roomId = resultSet.getString("room_id");
        String roomNumber = resultSet.getString("room_number");
        Integer floor = resultSet.getInt("floor");
        String roomType = resultSet.getString("room_type");
        String description = resultSet.getString("description");
        Integer price = resultSet.getInt("price");
        String userId = resultSet.getString("user_id");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String middleName = resultSet.getString("middle_name");
        User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
        Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
        return new Booking(bookingId, checkIn, checkOut, user, room);
    }
    @Override
    public List<Booking> getBookingsBy(Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo) {
        try (Connection connection = getNewConnection()) {
            String getBookingsQuery = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where check_in >= ? and check_in <= ? and check_out >= ? and check_out <= ?";
            PreparedStatement getBookingsStatement = connection.prepareStatement(getBookingsQuery);
            getBookingsStatement.setDate(1, checkInFrom);
            getBookingsStatement.setDate(2, checkInTo);
            getBookingsStatement.setDate(3, checkOutFrom);
            getBookingsStatement.setDate(4, checkOutTo);
            ResultSet bookingsResultSet = getBookingsStatement.getResultSet();
            List<Booking> bookings = new ArrayList<>();
            while (bookingsResultSet.next()) {
                String bookingId = bookingsResultSet.getString("booking_id");
                Date checkIn = bookingsResultSet.getDate("check_in");
                Date checkOut = bookingsResultSet.getDate("check_out");
                String roomId = bookingsResultSet.getString("room_id");
                String roomNumber = bookingsResultSet.getString("room_number");
                Integer floor = bookingsResultSet.getInt("floor");
                String roomType = bookingsResultSet.getString("room_type");
                String description = bookingsResultSet.getString("description");
                Integer price = bookingsResultSet.getInt("price");
                String userId = bookingsResultSet.getString("user_id");
                String phone = bookingsResultSet.getString("phone");
                String email = bookingsResultSet.getString("email");
                String firstName = bookingsResultSet.getString("first_name");
                String lastName = bookingsResultSet.getString("last_name");
                String middleName = bookingsResultSet.getString("middle_name");
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
