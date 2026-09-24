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
}