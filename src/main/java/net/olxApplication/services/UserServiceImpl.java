
package net.olxApplication.services;
import net.olxApplication.Entity.User;
import net.olxApplication.Entity.Wallet;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.UserService;
import net.olxApplication.RequestBodies.UserRequestBody;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.repository.UserRepository;
import net.olxApplication.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private static final Double DEFAULT_BALANCE = 10000.0;

    // Get All the Users
    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    //Get a User with Id
    @Override
    public Optional<User> getUserById(Long id) throws RuntimeException{
        User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        if(user.getStatus().equals("DeActive")){
            throw new BadRequest("User is DeActive");
        }
        return userRepository.findById(id);
    }

    // Add New User
    @Override
    @Transactional
    public ResponseEntity<?> createUser(String name, String email) throws RuntimeException{
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new BadRequest("Invalid email format");
        }
        try{
//            if(userRepository.findByEmail(email).isPresent()) throw new BadRequest("Email Already exist");
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
    public User updateUser(Long id, UserRequestBody user) throws RuntimeException{
        User curr = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        curr.setEmail(user.getEmail());
        curr.setName(user.getName());
        userRepository.save(curr);
        return curr;
    }

    @Override
    public User activateUser(Long id) throws RuntimeException{
        User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User not exist"));
        if(user.getStatus().equals("Active")){
            throw new BadRequest("User is Already Active");
        }
        user.setStatus("Active");
        return userRepository.save(user);
    }

    @Override
    public List<User> filterUsers(Long userId, String name , String email ) throws RuntimeException{
        try{
            if (userId != null) {
                return userRepository.findById(userId)
                        .map(List::of)
                        .orElseThrow(() -> new NotExist("User with ID " + userId + " not found."));
            } else if (email != null) {
                return userRepository.findByEmail(email).map(List::of).orElseThrow(() -> new NotExist(" No User exist for this email"));
            } else if (name != null) {
                List<User> users = userRepository.findByName(name);
                if (users.isEmpty()) {
                    throw new NotExist("No users found with name " + name + ".");
                }
                return users;
            } else {
                throw new BadRequest("At least one filter (userId, name, or email) must be provided.");
            }
        }catch (BadRequest e){
            throw new BadRequest(e.getMessage());

        }
        catch (Exception e) {
            throw new RuntimeException("unknown Error");
        }

    }
}
