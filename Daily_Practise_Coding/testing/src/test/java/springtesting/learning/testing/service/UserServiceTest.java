package springtesting.learning.testing.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.repositroy.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; //fake repo

    @InjectMocks
    private UserService userService; //real service , but with the fake repo injected

    @Test
    void getUserById_whenUserExists_shouldReturnUser(){
        //given
        User fakeUser = new User(1L, "john", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));

        //when
        User result = userService.getUserById(1L);

        //Then
        assertNotNull(result);
        assertEquals("john",result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository).findById(1L); //make sure method was really called
    }

    @Test
    void getUserById_whenUserDoesNotExists_shoudlThrowException(){

        //given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //when+then

        RuntimeException exception = assertThrows(RuntimeException.class, ()-> userService.getUserById(99L));

        assertTrue(exception.getMessage().contains("user not found"));
        verify(userRepository).findById(99L);
    }

    @Test
    void createUser_shouldSaveAndReturnUser(){

        //given
        User userToSave = new User(null, "Alice", "alice@example.com");
        User saveUser = new User(10L, "Alice", "alice@example.com");

        when(userRepository.save(any(User.class))).thenReturn(saveUser);


        //when
        User result = userService.createUser("Alice", "alice@example.com");

        //then
        assertNotNull(result.getId());
        assertEquals(10L, result.getId());
        assertEquals("Alice",result.getName());
        verify(userRepository).save(any(User.class));
    }

}
