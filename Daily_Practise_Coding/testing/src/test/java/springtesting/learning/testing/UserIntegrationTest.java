package springtesting.learning.testing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.repositroy.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc     // Gives us MockMvc even in full context
@Transactional                 // Rolls back database changes after each test
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();   // Clean start for every test
    }

    @Test
    void createUser_thenGetUser_shouldWorkEndToEnd() throws Exception {
        // 1. Create a user via POST
        String requestBody = """
                {
                    "name": "Bob",
                    "email": "bob@example.com"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.id").isNumber());

        // 2. Verify it was actually saved in the database
        User savedUser = userRepository.findAll().get(0);
        Long userId = savedUser.getId();

        // 3. Get the user via GET
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void getUser_whenUserDoesNotExist_shouldReturn404OrError() throws Exception {
        // Depending on how your controller handles exceptions
        mockMvc.perform(get("/api/users/99999"))
                .andExpect(status().is5xxServerError());   // or isNotFound() if you handle it properly
    }

    @Test
    void isEmailExists_whenEmailExists_shouldReturnsTrue(){
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        boolean result = userService.isEmailExists("john@example.com");

        assertTrue(result);
    }

    @Test
    void isEmailExists_whenEmailDoesNotExists_shouldReturnsFalse(){
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = userService.isEmailExists("unknwo@example.com");

        assertFalse(result);
    }

    @Test
    void getAllUsers_whenUsersExist_shouldReturnUsers() throws Exception {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].email").value("bob@example.com"));
    }

    @Test
    void deleteUser_whenUserExists_shouldDeeteUser() throws Exception{
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@example.com");

        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        mockMvc.perform(delete("/api/users" + userId))
                .andExpect(status().isNotContent());

        assertTrue(userRepository.findById((userId)).isEmpty());
    }

}