package src.services;

import src.entities.Room;
import src.services.exeptions.BookingNotFoundException;
import src.services.exeptions.RequiredFieldMissedException;
import src.services.exeptions.RoomNotFoundException;

import java.util.List;

public interface RoomService {

    /**
     * Создать комнату.
     *
     * @param room комната
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return созданная комната с присвоенным идентификатором
     */
    Room createRoom(Room room) throws RequiredFieldMissedException;

    /**
     * Обновить данные комнаты.
     *
     * @param room обновленная комната
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return обновленная комната
     */
    Room updateRoom(Room room)  throws RequiredFieldMissedException;

    /**
     * Удалить комнату.
     *
     * @param id идентификатор комнаты
     * @throws RoomNotFoundException если комната с таким id не найдена
     */
    void deleteRoom(String id) throws RoomNotFoundException;

    /**
     * Получить комнату по идентификатору.
     *
     * @param id идентификатор комнаты
     * @throws RoomNotFoundException если комната с таким id не найдена
     * @return комната
     */
    Room getByRoom(String id) throws RoomNotFoundException;

    /**
     * Получить комнату и ее бронирования с возможностью фильтрации по номеру комнаты/этажу/типу комнаты/цены.
     *
     * @param roomNumber номер комнаты
     * @param floor этаж
     * @param roomType тип комнаты
     * @param price цена
     * @throws RoomNotFoundException если комната с такими параметрами не найдена
     * @return бронирование
     */
    List<Room> getRooms(String roomNumber, Integer floor, String roomType, Integer price) throws RoomNotFoundException;
}
