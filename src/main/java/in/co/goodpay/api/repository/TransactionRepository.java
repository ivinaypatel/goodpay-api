package in.co.goodpay.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.goodpay.api.entity.Transaction;
import in.co.goodpay.api.entity.Wallet;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySenderWallet_User_MobileNumber(String mobileNumber);
    List<Transaction> findByReceiverWallet_User_MobileNumber(String mobileNumber);
    
 // Fetch all transactions for a user (as sender)
    List<Transaction> findBySenderWallet(Wallet senderWallet);

    // Fetch transactions with optional filters
    @Query("SELECT t FROM Transaction t WHERE t.senderWallet = :wallet " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (:startDate IS NULL OR t.createdOn >= :startDate) " +
            "AND (:endDate IS NULL OR t.createdOn <= :endDate)")
    List<Transaction> findTransactionsByFilters(
            @Param("wallet") Wallet wallet,
            @Param("type") String type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
