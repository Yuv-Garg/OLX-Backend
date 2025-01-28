package net.olxApplication.services;

import lombok.extern.slf4j.Slf4j;
import net.olxApplication.Entity.Transaction;
import net.olxApplication.Entity.User;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.WalletService;
import net.olxApplication.RequestBodies.WalletRequestBody;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.repository.TransactionRepository;
import net.olxApplication.repository.UserRepository;
import net.olxApplication.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> addBalance(Long id, WalletRequestBody wallet) throws RuntimeException{
        if(wallet.getNewBalance() == null){
            throw new BadRequest("Wrong field");
        }
        if(wallet.getNewBalance() <= 0){
            throw new BadRequest("Amount Can't Be negative");
        }
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User Not exist"));
            user.getWallet().setBalance(user.getWallet().getBalance() + wallet.getNewBalance());
            walletRepository.save(user.getWallet());
            MessageResponse messageResponse = new MessageResponse("Your Updated Balance is " + user.getWallet().getBalance());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (NotExist e) {
            throw new NotExist(e.getMessage());
        } catch (BadRequest e) {
            throw new BadRequest(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
