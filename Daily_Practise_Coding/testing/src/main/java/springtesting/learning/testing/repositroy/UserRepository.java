package springtesting.learning.testing.repositroy;

import org.springframework.stereotype.Repository;
import springtesting.learning.testing.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findById(Long id);
    User save(User user);
}
