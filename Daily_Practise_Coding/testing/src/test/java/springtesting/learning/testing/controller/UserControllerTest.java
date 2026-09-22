package springtesting.learning.testing.controller;

import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.service.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class) //only loads the web layer + this controller
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getUser_whenUserExists_shouldReturn200AndUser() throws Exception {
        // given

        User user = new User(1L, "John", "john@example.com");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUser_shouldReturn201AndCreatedUser() throws Exception{
        User createdUser = new User(10L, "Alice", "alice@example.com");
        when(userService.createUser(anyString(),anyString())).thenReturn(createdUser);

        String requestBody = """
                {
                                    "name": "Alice",
                                    "email": "alice@example.com"
                                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

    }

}
