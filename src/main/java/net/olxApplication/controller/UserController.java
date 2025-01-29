package net.olxApplication.controller;

import net.olxApplication.Entity.User;
import net.olxApplication.Interfaces.UserService;
import net.olxApplication.RequestBodies.UserRequestBody;
import net.olxApplication.ResponseBodies.UserResponse;
import net.olxApplication.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("allUsers")
    public List<UserResponse> getAll(){
        return userService.getAll();
    }

    @PostMapping("addUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNew(@RequestBody UserRequestBody user) {
         return userService.createUser(user.getName(), user.getEmail());
    }

    @GetMapping("getUser")
    public UserResponse getUserById(@RequestParam("userId") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("deactivate")
    public void deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("updateUser")
    public UserResponse updateUser(@RequestParam("userId") Long id, @RequestBody UserRequestBody user) {
        return userService.updateUser(id, user);
    }
    @PutMapping("activate")
    public UserResponse activateUser(@RequestParam("userId") Long id) {
        return userService.activateUser(id);
    }

    @GetMapping("filterUsers")
    private List<UserResponse> filter(@RequestParam(required = false) Long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) throws RuntimeException{
        return userService.filterUsers(id, name, email);
    }

}
