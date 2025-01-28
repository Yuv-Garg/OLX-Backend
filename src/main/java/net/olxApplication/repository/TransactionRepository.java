package net.olxApplication.repository;

import net.olxApplication.Entity.Transaction;
import net.olxApplication.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId")
//@Query("SELECT t FROM Transaction t JOIN FETCH t.order o JOIN FETCH o.product p WHERE t.wallet.id = :walletId")
    List<Transaction> findByWalletId(Long walletId);
}
