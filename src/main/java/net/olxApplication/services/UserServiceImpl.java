
package net.olxApplication.services;
import net.olxApplication.Entity.User;
import net.olxApplication.Entity.Wallet;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.UserService;
import net.olxApplication.RequestBodies.UserRequestBody;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.ResponseBodies.UserResponse;
import net.olxApplication.controller.ConvertResponses;
import net.olxApplication.repository.UserRepository;
import net.olxApplication.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ConvertResponses convertResponses;


    @Autowired
    private static final Double DEFAULT_BALANCE = 0.0;

    // Get All the Users
    @Override
    public List<UserResponse> getAll(){
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user : userRepository.findAll()){
            userResponses.add(convertResponses.covertUser(user));
        }
        return userResponses;
    }

    //Get a User with Id
    @Override
    public UserResponse getUserById(Long id) throws RuntimeException{
        User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        if(user.getStatus().equals("DeActive")){
            throw new BadRequest("User is DeActive");
        }
        return convertResponses.covertUser(userRepository.findById(id).get());
    }

    // Add New User
    @Override
    @Transactional
    public ResponseEntity<?> createUser(String name, String email) throws RuntimeException{
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new BadRequest("Invalid email format");
        }
        try{
            Wallet wallet = new Wallet();
            wallet.setBalance(DEFAULT_BALANCE);
            walletRepository.save(wallet);

            User user = User.builder()
                    .name(name)
                    .email(email)
                    .wallet(wallet)
                    .status("Active")
                    .build();
            userRepository.save(user);
            return new ResponseEntity<>(new MessageResponse("User Created Successfully"), HttpStatus.CREATED);
        } catch (BadRequest e) {
            throw new BadRequest(e.getMessage());
        }
        catch(Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }

//        return Mono.defer(() -> {
//                            Wallet wallet = new Wallet();
//                            wallet.setBalance(DEFAULT_BALANCE);
//
//                            // Save Wallet Reactively
//                            return walletRepository.save(wallet)
//                                    .wait(savedWallet -> {
//                                        User user = User.builder()
//                                                .name(name)
//                                                .email(email)
//                                                .wallet(savedWallet)
//                                                .status("Active")
//                                                .build();
//
//                                        // Save User Reactively
//                                        return userRepository.save(user)
//                                                .map(savedUser -> new ResponseEntity<>(
//                                                        new MessageResponse("User Created Successfully"),
//                                                        HttpStatus.CREATED
//                                                ));
//                                    });
//                        })
//                .onErrorResume(BadRequest.class, e ->
//                        Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST)))
//                .onErrorResume(e ->
//                        Mono.just(new ResponseEntity<>("Unexpected Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }


    // Changes the Status of the User ("Active" , "DeActive" )
    @Override
    @Transactional
    public void deleteUser(Long id) throws RuntimeException{
        User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        if(user.getStatus().equals("DeActive")) {
            throw new BadRequest("DeActive Already");
        }
        user.setStatus("DeActive");
        userRepository.save(user);
    }


    @Override
    public UserResponse updateUser(Long id, UserRequestBody user) throws RuntimeException{
        User curr = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        curr.setEmail(user.getEmail());
        curr.setName(user.getName());
        userRepository.save(curr);
        return convertResponses.covertUser(curr);
    }

    @Override
    public UserResponse activateUser(Long id) throws RuntimeException{
        User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        if(user.getStatus().equals("Active")){
            throw new BadRequest("User is Already Active");
        }
        user.setStatus("Active");
        return convertResponses.covertUser(userRepository.save(user));
    }

    @Override

    // Id ka error aaara
    public List<UserResponse> filterUsers(Long userId, String name , String email ) throws RuntimeException{
        try{
            List<User> users = new ArrayList<>();
            if (userId != null) {
                User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User with ID " + userId + " not found."));
                users.add(user);
            } else if (email != null) {
                users = userRepository.findByEmail(email).map(List::of).orElseThrow(() -> new NotExist(" No User exist for this email"));
            } else if (name != null) {
                if (userRepository.findByName(name).isEmpty()) {
                    throw new NotExist("No users found with name " + name + ".");
                }
            } else {
                throw new BadRequest("At least one filter (userId, name, or email) must be provided.");
            }
            List<UserResponse> userResponses = new ArrayList<>();
            for(User user : users){
                userResponses.add(convertResponses.covertUser(user));
            }
            return userResponses;
        }catch (BadRequest e){
            throw new BadRequest(e.getMessage());

        }
        catch (Exception e) {
            throw new RuntimeException("unknown Error");
        }

    }
}
