package src.services.impl;

import src.entities.User;
import src.services.UserService;
import src.services.exeptions.RequiredFieldMissedException;
import src.services.exeptions.UserNotFoundException;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public User createUser(User user) throws RequiredFieldMissedException {
        return null;
    }

    @Override
    public User updateUser(User user) throws RequiredFieldMissedException {
        return null;
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {

    }

    @Override
    public User getByUser(String id) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<User> getUsers(String firstName, String lastName, String middleName) throws UserNotFoundException {
        return null;
    }
}
