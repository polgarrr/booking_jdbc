package src.services.exeptions;

public class UserNotFoundException extends Exception {
        public UserNotFoundException(String userId) {
            super("User with ID '" + userId + "' is not found.");
        }
}
