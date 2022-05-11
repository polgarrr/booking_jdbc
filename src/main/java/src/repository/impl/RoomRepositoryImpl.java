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
    public Room createRoom(Room room) {
        try (Connection connection = getNewConnection()) {
            String queryCreateR = "insert into booking.rooms (id, room_number, floor, room_type, description, price) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statementCrtR = connection.prepareStatement(queryCreateR);
            statementCrtR.setString(1, room.getId());
            statementCrtR.setString(2, room.getRoomNumber());
            statementCrtR.setInt(3, room.getFloor());
            statementCrtR.setString(4, room.getRoomType());
            statementCrtR.setString(5, room.getDescription());
            statementCrtR.setInt(6, room.getPrice());
            statementCrtR.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return room;
    }

    @Override
    public Room updateRoom(Room room) {
        try (Connection connection = getNewConnection()) {
            String queryUpdateR = "update booking.rooms set room_number = ?, floor = ?, room_type = ?, description = ?, price = ? where id = ?";
            PreparedStatement statementUpdR = connection.prepareStatement(queryUpdateR);
            statementUpdR.setString(1, room.getRoomNumber());
            statementUpdR.setInt(2, room.getFloor());
            statementUpdR.setString(3, room.getRoomType());
            statementUpdR.setString(4, room.getDescription());
            statementUpdR.setInt(5, room.getPrice());
            statementUpdR.setString(6, room.getId());
            statementUpdR.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
        return room;

    }

    @Override
    public void deleteRoom(String id) throws RoomNotFoundException {
        try (Connection connection = getNewConnection()) {
            String queryDeleteR = "delete from booking.rooms where id = ?";
            PreparedStatement statementDltR = connection.prepareStatement(queryDeleteR);
            statementDltR.setString(1, id);
            int i = statementDltR.executeUpdate(); // TODO: В случае если сущность не найдена (i==0) выкинуть UserNotFoundException.
            if (i == 0) {
                throw new RoomNotFoundException(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServiceException(e.getMessage());
        }
    }

    @Override
    public Room getByRoom(String id) throws RoomNotFoundException {
        try (Connection connection = getNewConnection()) {
            String queryGetByR = "select b.id booking_id, check_in, check_out, room_id, room_number, floor, room_type, description, price, user_id, phone, email, first_name, last_name, middle_name from booking.bookings b inner join booking.rooms r on r.id = b.room_id inner join booking.users u on u.id = b.user_id where b.room_id = ?";
            PreparedStatement statementGetByR = connection.prepareStatement(queryGetByR);
            statementGetByR.setString(1, id);
            ResultSet resultSetGetByR = statementGetByR.getResultSet();
            List<Booking> bookings = new ArrayList<>();
            Room room = new Room();
            if (resultSetGetByR == null || resultSetGetByR.getRow() == 0) {
                throw new RoomNotFoundException(id);
            }
            while (resultSetGetByR.next()) {
                String bookingId = resultSetGetByR.getString("booking_id");
                Date checkIn = resultSetGetByR.getDate("check_in");
                Date checkOut = resultSetGetByR.getDate("check_out");
                String roomId = resultSetGetByR.getString("room_id");
                String roomNumber = resultSetGetByR.getString("room_number");
                Integer floor = resultSetGetByR.getInt("floor");
                String roomType = resultSetGetByR.getString("room_type");
                String description = resultSetGetByR.getString("description");
                Integer price = resultSetGetByR.getInt("price");
                String userId = resultSetGetByR.getString("user_id");
                String phone = resultSetGetByR.getString("phone");
                String email = resultSetGetByR.getString("email");
                String firstName = resultSetGetByR.getString("first_name");
                String lastName = resultSetGetByR.getString("last_name");
                String middleName = resultSetGetByR.getString("middle_name");
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
    public List<Room> getRooms(String roomNumber, Integer floor, String roomType, Integer price) {
        try (Connection connection = getNewConnection()) {
            String queryRoom = "select * from booking.rooms r where r.room_number = ? and r.floor = ? and r.room_type = ? and r.price = ?";
            PreparedStatement statementRoom = connection.prepareStatement(queryRoom);
            statementRoom.setString(1, roomNumber);
            statementRoom.setInt(2, floor);
            statementRoom.setString(3, roomType);
            statementRoom.setInt(4, price);
            ResultSet resultSet = statementRoom.getResultSet();
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
