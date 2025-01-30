package net.olxApplication.controller;

import lombok.AllArgsConstructor;
import net.olxApplication.Entity.Wallet;
import net.olxApplication.Interfaces.WalletService;
import net.olxApplication.RequestBodies.WalletRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/addBalance")
    public ResponseEntity<?> addBalance(@RequestParam("user_id") Long id,
                                        @RequestBody WalletRequestBody balance) throws RuntimeException{
        return walletService.addBalance(id, balance);
    }

    @GetMapping("/allWallets")
    public List<Wallet> getAll(){
        return walletService.getAll();
    }
}
