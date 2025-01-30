package net.olxApplication.controller;

import lombok.AllArgsConstructor;
import net.olxApplication.Interfaces.UserService;
import net.olxApplication.RequestBodies.UserRequestBody;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/allUsers")
    public List<UserResponse> getAll(){
        return userService.getAll();
    }

    @PostMapping("/addUser")
    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<MessageResponse> createNew(@RequestBody UserRequestBody user) {
    public ResponseEntity<UserResponse> createNew(@RequestBody UserRequestBody user) {
         return userService.createUser(user.getName(), user.getEmail());
    }

    @GetMapping("/getUser")
    public UserResponse getUserById(@RequestParam("user_id") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/deactivate")
    public MessageResponse deleteUser(@RequestParam("user_id") Long id) {
       return  userService.deleteUser(id);
    }

    @PutMapping("/updateUser")
    public UserResponse updateUser(@RequestParam("user_id") Long id, @RequestBody UserRequestBody user) {
        return userService.updateUser(id, user);
    }
    @PutMapping("/activate")
    public UserResponse activateUser(@RequestParam("user_id") Long id) {
        return userService.activateUser(id);
    }

    @GetMapping("/filterUsers")
    private List<UserResponse> filter(@RequestParam(required = false) Long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) throws RuntimeException{
        return userService.filterUsers(id, name, email);
    }

}
