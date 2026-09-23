package springtesting.learning.testing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springtesting.learning.testing.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<com.yourpackage.model.User> getUser(@PathVariable Long id){
        com.yourpackage.model.User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<com.yourpackage.model.User> createUser(@RequestBody UserRequest request){
        com.yourpackage.model.User user = userService.createUser(request.getName(), request.getEmail());
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
}
