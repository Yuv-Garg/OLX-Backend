package net.olxApplication.controller;

import net.olxApplication.Entity.User;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.WalletService;
import net.olxApplication.RequestBodies.WalletRequestBody;
import net.olxApplication.services.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
