package net.olxApplication.Interfaces;

import net.olxApplication.Entity.Transaction;
import net.olxApplication.Entity.User;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.RequestBodies.WalletRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface WalletService {
     ResponseEntity<?> addBalance(Long id, WalletRequestBody balance) throws RuntimeException;
}
