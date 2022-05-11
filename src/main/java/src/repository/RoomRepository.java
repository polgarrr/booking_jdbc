package src.repository;
import src.entities.Room;
import src.services.exeptions.RoomNotFoundException;

import java.util.List;

public interface RoomRepository {
    Room createRoom(Room room);
    Room updateRoom(Room room);
    void deleteRoom(String id) throws RoomNotFoundException;
    Room getByRoom(String id) throws RoomNotFoundException;
    List<Room> getRooms(String roomNumber, Integer floor, String roomType, Integer price);
}
