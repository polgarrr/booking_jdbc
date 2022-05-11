package src.services.impl;

import src.entities.Room;
import src.repository.RoomRepository;
import src.services.RoomService;
import src.services.exeptions.RequiredFieldMissedException;
import src.services.exeptions.RoomNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) throws RequiredFieldMissedException {
        if (room.getRoomNumber() == null || room.getFloor() == null || room.getRoomType() == null || room.getPrice() == null) {
            throw new RequiredFieldMissedException("Room is not completed with all required fields: roomNumber = " + room.getRoomNumber() + ", floor, type, price");
        }
        room.setId(UUID.randomUUID().toString());
        return roomRepository.updateRoom(room);
    }

    @Override
    public Room updateRoom(Room room) throws RequiredFieldMissedException {
        return roomRepository.updateRoom(room);
    }

    @Override
    public void deleteRoom(String id) throws RoomNotFoundException {
        roomRepository.deleteRoom(id);
    }

    @Override
    public Room getByRoom(String id) throws RoomNotFoundException {
        Room room = roomRepository.getByRoom(id);
        if (room == null) {
            throw new RoomNotFoundException("Room with id: " + id + " not found");
        }
        return room;
    }

    @Override
    public List<Room> getRooms(String roomNumber, Integer floor, String roomType, Integer price) throws RoomNotFoundException {
        return roomRepository.getRooms(roomNumber, floor, roomType, price);
    }
}
