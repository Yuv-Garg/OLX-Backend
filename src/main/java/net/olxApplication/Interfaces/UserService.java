package net.olxApplication.Interfaces;

import net.olxApplication.Entity.User;
import net.olxApplication.RequestBodies.UserRequestBody;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getUserById(Long id) throws RuntimeException;
     ResponseEntity<?> createUser(String name, String email) throws RuntimeException;
     void deleteUser(Long id) throws RuntimeException;
     User updateUser(Long id, UserRequestBody user) throws RuntimeException;
     User activateUser(Long id) throws RuntimeException;
    List<User> filterUsers(Long userId, String name, String email) throws RuntimeException;
}
