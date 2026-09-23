package springtesting.learning.testing.repositroy;

import com.yourpackage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom methods later if needed
}