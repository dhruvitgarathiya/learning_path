package springtesting.learning.testing.service;


import org.springframework.stereotype.Service;
import springtesting.learning.testing.UserNotFoundException;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.repositroy.UserRepository;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


}