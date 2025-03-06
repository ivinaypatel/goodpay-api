package in.co.goodpay.api.repository;

import in.co.goodpay.api.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByUser_MobileNumber(String mobileNumber);
}
