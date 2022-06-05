package src.repository.impl;

import src.entities.Booking;
import src.entities.Room;
import src.entities.User;
import src.repository.RoomRepository;
import src.services.exeptions.InternalServiceException;
import src.services.exeptions.RoomNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RoomRepositoryImpl implements RoomRepository {

    @Override
    public Room create(Room room) {
        try (Connection connection = getNewConnection()) {
            String createRoomQuery = "insert into booking.rooms (id, room_number, floor, room_type, description, price) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement createRoomStatement = connection.prepareStatement(createRoomQuery);
            createRoomStatement.setString(1, room.getId());
            createRoomStatement.setString(2, room.getRoomNumber());
            createRoomStatement.setInt(3, room.getFloor());
            createRoomStatement.setString(4, room.getRoomType());
            createRoomStatement.setString(5, room.getDescription());
            createRoomStatement.setInt(6, room.getPrice());
            createRoomStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return room;
    }

    @Override
    public Room update(Room room) {
        try (Connection connection = getNewConnection()) {
            String updateRoomQuery = "update booking.rooms set room_number = ?, floor = ?, room_type = ?, description = ?, price = ? where id = ?";
            PreparedStatement updateRoomStatement = connection.prepareStatement(updateRoomQuery);
            updateRoomStatement.setString(1, room.getRoomNumber());
            updateRoomStatement.setInt(2, room.getFloor());
            updateRoomStatement.setString(3, room.getRoomType());
            updateRoomStatement.setString(4, room.getDescription());
            updateRoomStatement.setInt(5, room.getPrice());
            updateRoomStatement.setString(6, room.getId());
            updateRoomStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return room;

    }

    @Override
    public void deleteBy(String id) throws RoomNotFoundException {
        try (Connection connection = getNewConnection()) {
            String deleteRoomQuery = "delete from booking.rooms where id = ?";
            PreparedStatement deleteRoomStatement = connection.prepareStatement(deleteRoomQuery);
            deleteRoomStatement.setString(1, id);
            if (deleteRoomStatement.executeUpdate() == 0) {
                throw new RoomNotFoundException(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public Room getBy(String id) throws RoomNotFoundException {
        try (Connection connection = getNewConnection()) {
            String getRoomQuery = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where b.room_id = ?";
            PreparedStatement getRoomStatement = connection.prepareStatement(getRoomQuery);
            getRoomStatement.setString(1, id);
            ResultSet roomResultSet = getRoomStatement.getResultSet();
            List<Booking> bookings = new ArrayList<>();
            Room room = new Room();
            if (roomResultSet == null || roomResultSet.getRow() == 0) {
                throw new RoomNotFoundException(id);
            }
            while (roomResultSet.next()) {
                String bookingId = roomResultSet.getString("booking_id");
                Date checkIn = roomResultSet.getDate("check_in");
                Date checkOut = roomResultSet.getDate("check_out");
                String roomId = roomResultSet.getString("room_id");
                String roomNumber = roomResultSet.getString("room_number");
                Integer floor = roomResultSet.getInt("floor");
                String roomType = roomResultSet.getString("room_type");
                String description = roomResultSet.getString("description");
                Integer price = roomResultSet.getInt("price");
                String userId = roomResultSet.getString("user_id");
                String phone = roomResultSet.getString("phone");
                String email = roomResultSet.getString("email");
                String firstName = roomResultSet.getString("first_name");
                String lastName = roomResultSet.getString("last_name");
                String middleName = roomResultSet.getString("middle_name");
                User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                bookings.add(booking);
            }
            room.setBookings(bookings);
            return room;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public List<Room> getRoomsBy(String roomNumber, Integer floor, String roomType, Integer price) {
        try (Connection connection = getNewConnection()) {
            String getRoomsQuery = "select * from booking.rooms r where r.room_number = ? and r.floor = ? and r.room_type = ? and r.price = ?";
            PreparedStatement getRoomsStatement = connection.prepareStatement(getRoomsQuery);
            getRoomsStatement.setString(1, roomNumber);
            getRoomsStatement.setInt(2, floor);
            getRoomsStatement.setString(3, roomType);
            getRoomsStatement.setInt(4, price);
            ResultSet resultSet = getRoomsStatement.getResultSet();
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                String roomId = resultSet.getString("id");
                String description = resultSet.getString("description");
                String query = "select b.id booking_id, check_in, check_out, user_id, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where r.id = ?";
                PreparedStatement statementBook = connection.prepareStatement(query);
                statementBook.setString(1, roomId);
                ResultSet resultSetBook = statementBook.getResultSet();
                List<Booking> bookings = new ArrayList<>();
                while (resultSetBook.next()) {
                    String bookingId = resultSetBook.getString("booking_id");
                    Date checkIn = resultSetBook.getDate("check_in");
                    Date checkOut = resultSetBook.getDate("check_out");
                    String userId = resultSetBook.getString("user_id");
                    String phone = resultSetBook.getString("phone");
                    String email = resultSetBook.getString("email");
                    String firstName = resultSetBook.getString("first_name");
                    String lastName = resultSetBook.getString("last_name");
                    String middleName = resultSetBook.getString("middle_name");
                    User user = new User(userId, phone, email, firstName, lastName, middleName, new ArrayList<>());
                    Room room = new Room(roomId, roomNumber, floor, roomType, description, price, new ArrayList<>());
                    Booking booking = new Booking(bookingId, checkIn, checkOut, user, room);
                    bookings.add(booking);
                }
                rooms.add(new Room(roomId, roomNumber, floor, roomType, description, price, bookings));
            }
            return rooms;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

        private Connection getNewConnection () throws SQLException {
            String url = "jdbc:postgresql://localhost:5432/macbook";
            String user = "macbook";
            String passwd = "colidh10b";
            return DriverManager.getConnection(url, user, passwd);
        }
    }
