package src.services;

import src.entities.User;
import src.services.exeptions.RequiredFieldMissedException;
import src.services.exeptions.UserNotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Создать пользователя.
     *
     * @param user пользователь
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return созданный пользователь с присвоенным идентификатором
     */
    User createUser(User user) throws RequiredFieldMissedException;

    /**
     * Обновить данные пользователя.
     *
     * @param user обновленный пользователь
     * @throws RequiredFieldMissedException если поля не заполнены до конца
     * @return обновленная комната
     */
    User updateUser(User user)  throws RequiredFieldMissedException;

    /**
     * Удалить пользователя.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь с таким id не найден
     */
    void deleteUser(String id) throws UserNotFoundException;

    /**
     * Получить пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь с таким id не найден
     * @return пользователь
     */
    User getByUser(String id) throws UserNotFoundException;

    /**
     * Получить бронирование пользователя с возможностью фильтрации по ФИО.
     *
     * @param firstName имя
     * @param lastName фамилия
     * @param middleName отчество
     * @throws UserNotFoundException если пользователь с такими параметрами не найден
     * @return бронирование
     */
    List<User> getUsers(String firstName, String lastName, String middleName) throws UserNotFoundException;
}
