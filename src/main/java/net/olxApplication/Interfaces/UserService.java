package net.olxApplication.Interfaces;

import net.olxApplication.Entity.User;
import net.olxApplication.RequestBodies.UserRequestBody;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.UserResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getUserById(Long id) throws RuntimeException;
//     ResponseEntity<MessageResponse> createUser(String name, String email) throws RuntimeException;
     ResponseEntity<UserResponse> createUser(String name, String email) throws RuntimeException;
     MessageResponse deleteUser(Long id) throws RuntimeException;
     UserResponse updateUser(Long id, UserRequestBody user) throws RuntimeException;
     UserResponse activateUser(Long id) throws RuntimeException;
    List<UserResponse> filterUsers(Long userId, String name, String email) throws RuntimeException;
}
