package src.repository.impl;

import src.entities.Booking;
import src.entities.Room;
import src.entities.User;
import src.repository.UserRepository;
import src.services.exeptions.InternalServiceException;
import src.services.exeptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User create(User user) {
        try (Connection connection = getNewConnection()) {
            String queryCreateU = "insert into booking.users (id, phone, email, first_name, last_name, middle_name) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statementCrtU = connection.prepareStatement(queryCreateU);
            statementCrtU.setString(1, user.getId());
            statementCrtU.setString(2, user.getPhone());
            statementCrtU.setString(3, user.getEmail());
            statementCrtU.setString(4, user.getFirstName());
            statementCrtU.setString(5, user.getLastName());
            statementCrtU.setString(6, user.getMiddleName());
            statementCrtU.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (Connection connection = getNewConnection()) {
            String queryUpdateU = "update booking.users set phone = ?, email = ?, first_name = ?, last_name = ?, middle_name = ? where id = ?";
            PreparedStatement statementUpdU = connection.prepareStatement(queryUpdateU);
            statementUpdU.setString(1, user.getPhone());
            statementUpdU.setString(2, user.getEmail());
            statementUpdU.setString(3, user.getFirstName());
            statementUpdU.setString(4, user.getLastName());
            statementUpdU.setString(5, user.getMiddleName());
            statementUpdU.setString(6, user.getId());
            statementUpdU.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return user;
    }

    @Override
    public void delete(String id) throws UserNotFoundException {
        try (Connection connection = getNewConnection()) {
            String queryDeleteU = "delete from booking.users where id = ?";
            PreparedStatement statementDltU = connection.prepareStatement(queryDeleteU);
            statementDltU.setString(1, id);
            int i = statementDltU.executeUpdate(); // TODO: В случае если сущность не найдена (i==0) выкинуть UserNotFoundException.
            if (i == 0) {
                throw new UserNotFoundException(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public User getUser(String id) throws UserNotFoundException {
        try (Connection connection = getNewConnection()) {
            String queryGetByU = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where user_id = ?";
            PreparedStatement statementGetByU = connection.prepareStatement(queryGetByU);
            statementGetByU.setString(1, id);
            ResultSet resultSetU = statementGetByU.getResultSet();
            if (resultSetU == null || resultSetU.getRow() == 0) {
                throw new UserNotFoundException(id);
            }
            while (resultSetU.next()) {
                String bookingId = resultSetU.getString("booking_id");
                Date checkIn = resultSetU.getDate("check_in");
                Date checkOut = resultSetU.getDate("check_out");
                String roomId = resultSetU.getString("room_id");
                String roomNumber = resultSetU.getString("room_number");
                Integer floor = resultSetU.getInt("floor");
                String roomType = resultSetU.getString("room_type");
                String description = resultSetU.getString("description");
                Integer price = resultSetU.getInt("price");
                String userId = resultSetU.getString("user_id");
                String phone = resultSetU.getString("phone");
                String email = resultSetU.getString("email");
                String firstName = resultSetU.getString("first_name");
                String lastName = resultSetU.getString("last_name");
                String middleName = resultSetU.getString("middle_name");
                User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                user.getBookings().add(booking);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getUsers(String firstName, String lastName, String middleName) {
        try (Connection connection = getNewConnection()) {
            String queryUsers = "select * from booking.users u where u.first_name = ? and u.last_name = ? and u.middle_name = ?";
            PreparedStatement statementUser = connection.prepareStatement(queryUsers);
            statementUser.setString(1, firstName);
            statementUser.setString(2, lastName);
            statementUser.setString(3, middleName);
            ResultSet resultSet = statementUser.getResultSet();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String query = "select b.id booking_id, check_in, check_out, user_id, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where user_id = ?";
                PreparedStatement statementBook = connection.prepareStatement(query);
                statementBook.setString(1, userId);
                ResultSet resultSetBook = statementBook.getResultSet();
                List<Booking> bookings = new ArrayList<>();
                while (resultSetBook.next()) {
                    String bookingId = resultSetBook.getString("booking_id");
                    Date checkIn = resultSetBook.getDate("check_in");
                    Date checkOut = resultSetBook.getDate("check_out");
                    String roomId = resultSetBook.getString("room_id");
                    String roomNumber = resultSetBook.getString("room_number");
                    Integer floor = resultSetBook.getInt("floor");
                    String roomType = resultSetBook.getString("room_type");
                    String description = resultSetBook.getString("description");
                    Integer price = resultSetBook.getInt("price");
                    User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                    Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                    Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                    bookings.add(booking);
                }
                users.add(new User(userId, phone, email, firstName, lastName, middleName, bookings));
            }
            return users;
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
