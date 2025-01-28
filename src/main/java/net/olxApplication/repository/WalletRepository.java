package net.olxApplication.repository;

import net.olxApplication.Entity.Order;
import net.olxApplication.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
