package springtesting.learning.testing.repositroy;


import org.springframework.data.jpa.repository.JpaRepository;
import springtesting.learning.testing.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
List<User> findByNameContainingIgnoreCase(String name);

List<User> findByEmail(String email);
}