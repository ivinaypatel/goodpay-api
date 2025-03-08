package in.co.goodpay.api.repository;

import in.co.goodpay.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	
    Optional<User> findByMobileNumber(String mobileNumber);

    List<User> findByStatus(String status); 
}
