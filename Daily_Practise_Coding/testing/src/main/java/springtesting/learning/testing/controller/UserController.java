package springtesting.learning.testing.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springtesting.learning.testing.entity.User;
import springtesting.learning.testing.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request){
        User user = userService.createUser(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    public static class UserRequest{
        private String name;
        private String email;

        public String getName() {return name;}
        public void setName(String name) {this.name = name;}

        public String getEmail() {return email;}
        public void setEmail(String email) {this.email = email;}
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    ){
        User updatedUser = userService.updateUser(id, request.getName(), request.getEmail());
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUser(@RequestParam String name){
        List<User> users = userService.searchUserByName(name);
        return ResponseEntity.ok(users);

    }

    @GetMapping("/email")
    public ResponseEntity<List<User>> searchUserByEmail(@RequestParam String email){
        List<User> users = userService.findByEmail(email);
        return ResponseEntity.ok(users);
    }
}
