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
            String createUserQuery = "insert into booking.users (id, phone, email, first_name, last_name, middle_name) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement createUserStatement = connection.prepareStatement(createUserQuery);
            createUserStatement.setString(1, user.getId());
            createUserStatement.setString(2, user.getPhone());
            createUserStatement.setString(3, user.getEmail());
            createUserStatement.setString(4, user.getFirstName());
            createUserStatement.setString(5, user.getLastName());
            createUserStatement.setString(6, user.getMiddleName());
            createUserStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (Connection connection = getNewConnection()) {
            String updateUserQuery = "update booking.users set phone = ?, email = ?, first_name = ?, last_name = ?, middle_name = ? where id = ?";
            PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
            updateUserStatement.setString(1, user.getPhone());
            updateUserStatement.setString(2, user.getEmail());
            updateUserStatement.setString(3, user.getFirstName());
            updateUserStatement.setString(4, user.getLastName());
            updateUserStatement.setString(5, user.getMiddleName());
            updateUserStatement.setString(6, user.getId());
            updateUserStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return user;
    }

    @Override
    public void deleteBy(String id) throws UserNotFoundException {
        try (Connection connection = getNewConnection()) {
            String deleteUserQuery = "delete from booking.users where id = ?";
            PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery);
            deleteUserStatement.setString(1, id);
            if (deleteUserStatement.executeUpdate() == 0) {
                throw new UserNotFoundException(id);
            }; // TODO: В случае если сущность не найдена (i==0) выкинуть UserNotFoundException.
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public User getBy(String id) throws UserNotFoundException {
        try (Connection connection = getNewConnection()) {
            String getUserQuery = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where user_id = ?";
            PreparedStatement getUserStatement = connection.prepareStatement(getUserQuery);
            getUserStatement.setString(1, id);
            ResultSet userResultSet = getUserStatement.getResultSet();
            if (userResultSet == null || userResultSet.getRow() == 0) {
                throw new UserNotFoundException(id);
            }
            while (userResultSet.next()) {
                String bookingId = userResultSet.getString("booking_id");
                Date checkIn = userResultSet.getDate("check_in");
                Date checkOut = userResultSet.getDate("check_out");
                String roomId = userResultSet.getString("room_id");
                String roomNumber = userResultSet.getString("room_number");
                Integer floor = userResultSet.getInt("floor");
                String roomType = userResultSet.getString("room_type");
                String description = userResultSet.getString("description");
                Integer price = userResultSet.getInt("price");
                String userId = userResultSet.getString("user_id");
                String phone = userResultSet.getString("phone");
                String email = userResultSet.getString("email");
                String firstName = userResultSet.getString("first_name");
                String lastName = userResultSet.getString("last_name");
                String middleName = userResultSet.getString("middle_name");
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
    public List<User> getUsersBy(String firstName, String lastName, String middleName) {
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
