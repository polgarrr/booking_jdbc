package src.repository;

import src.entities.User;
import src.services.exeptions.UserNotFoundException;

import java.util.List;

public interface UserRepository {
    User create(User user);
    User update(User user);
    void deleteBy(String id) throws UserNotFoundException;
    User getBy(String id) throws UserNotFoundException;
    List<User> getUsersBy(String firstName, String lastName, String middleName);
}
