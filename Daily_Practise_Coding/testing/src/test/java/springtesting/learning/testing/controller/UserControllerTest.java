package springtesting.learning.testing.controller;

import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import springtesting.learning.testing.UserNotFoundException;
import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.repositroy.UserRepository;
import springtesting.learning.testing.service.UserService;

import java.util.List;
import java.util.Optional;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @Autowired
    private UserRepository userRepository;

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

    @Test
    void getUser_whenUserDoesNotExists_shouldReturn404() throws Exception{
        when(userService.getUserById(999L))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 99999"));
    }

    @Test
    void createUser_withValidData_shouldReturn201() throws Exception{
        User createdUser = new User(1L, "Alice", "alice@example.com");
        when(userService.createUser(anyString(), anyString())).thenReturn(createdUser);

        String requestBody = """
                {
                "name" : "Alice",
                "email" : "alice@example.com
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void createUser_withInvalidData_shouldReturn400() throws Exception {
        String requestBody = """
            {
                "name": "",
                "email": "invalid-email"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers(){
        List<User> users = List.of(
                new User(1L,"John", "john@example.com"),
                new User(2L, "Alice", "alice@example.com")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("john", result.get(0).getName());
    }

    @Test
    void updateUser_whenUserExists_shouldUpdateAndReturnUser(){
        User existingUser = new User(1L, "Old Name", "old@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments(0));

        User result = userService.updateUser(1L, "new name","new@example.com");

        assertEquals("new name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrowException(){
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> {
            userService.updateUser(99L, "Name", "email@example.com");
        })
    }

    @Test
    void updateUser_shouldReturn200AndUpdatedUser() throws Exception{
        User updatedUser = new User(1L, "Updated Name", "updated@example.com");
        when(userService.updateUser(eq(1L),anyString(),anyString())).thenReturn(updatedUser);

        String requestBody = """
                {
                "name" : "Updated Name",
                "email" : "updated@example.com"
                }
                """;

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void searchUserByName_shouldReturnMatchingUsers(){
        List<User> users = List.of(
                new User(1L, "John doe", "john@example.com"),
                new User(2L, "Johnny","johnny@example.com")
        );

        when(userRepository.findByNameContainingIgnoreCase("john")).thenReturn(users);

        List<User> result = userService.searchUserByName("john");

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());


    }


    @Test
    void searchUser_shouldReturnMatchingUsers() throws Exception{
        List<User> users = List.of(new User(1L,"John","john@example.com"));
        when(userService.searchUserByName("john")).thenReturn(users);

        mockMvc.perform(get("/api/users/search")
                .param("name", "john")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$[0].name").value("john"));
    }
}
