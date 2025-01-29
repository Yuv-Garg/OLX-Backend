package net.olxApplication.controller;

import net.olxApplication.Entity.Wallet;
import net.olxApplication.Interfaces.WalletService;
import net.olxApplication.RequestBodies.WalletRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RequestMapping("/olx")
@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;



    @GetMapping("/addBalance")
    public ResponseEntity<?> addBalance(@RequestParam("userId") Long id,
                                        @RequestBody WalletRequestBody balance) throws RuntimeException{
        return walletService.addBalance(id, balance);
    }

    @GetMapping("allWallets")
    public List<Wallet> getAll(){
        return walletService.getAll();
    }
}
