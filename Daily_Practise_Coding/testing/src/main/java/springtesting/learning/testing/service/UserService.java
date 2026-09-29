package springtesting.learning.testing.service;


import org.springframework.stereotype.Service;
import springtesting.learning.testing.UserNotFoundException;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.repositroy.UserRepository;

import java.util.List;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    public User updateUser(Long id, String name, String email){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));

        user.setName(name);
        user.setEmail(email);

        return userRepository.save(user);
    }

    public List<User> searchUserByName(String name){
        return userRepository.findByNameContainingIgnoreCase(name);
    }
}