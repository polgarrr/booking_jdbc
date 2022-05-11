package src.repository;

import src.entities.User;
import src.services.exeptions.UserNotFoundException;

import java.util.List;

public interface UserRepository {
    User create(User user);
    User update(User user);
    void delete(String id) throws UserNotFoundException;
    User getUser(String id) throws UserNotFoundException;
    List<User> getUsers(String firstName, String lastName, String middleName);
}
