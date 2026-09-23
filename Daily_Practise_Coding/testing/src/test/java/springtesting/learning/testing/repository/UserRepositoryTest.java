package springtesting.learning.testing.repository;


import com.yourpackage.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import springtesting.learning.testing.repositroy.UserRepository;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepoitory;

    @Test
    void saveUser_shouldPersistAndGenerateId(){
        User user = new User("John", "john@example.com");

        User savedUser = userRepoitory.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }


    @Test
    void findById_whenUserExists_shouldReturnUser(){
        User user = userRepoitory.save(new User("Alice", "alice@example.com"));

        Optional<User> found = userRepoitory.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findById_whenUserDoesNotExists_shouldReturnEmpty(){
        Optional<User> found = userRepoitory.findById(999L);

        assertThat(found).isEmpty();
    }

}
