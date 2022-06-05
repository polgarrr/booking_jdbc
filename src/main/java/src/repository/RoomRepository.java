package src.repository;
import src.entities.Room;
import src.services.exeptions.RoomNotFoundException;

import java.util.List;

public interface RoomRepository {
    Room create(Room room);
    Room update(Room room);
    void deleteBy(String id) throws RoomNotFoundException;
    Room getBy(String id) throws RoomNotFoundException;
    List<Room> getRoomsBy(String roomNumber, Integer floor, String roomType, Integer price);
}
