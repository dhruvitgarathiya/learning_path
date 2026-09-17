@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id){
        if(id <= 0){
            return ResponseEntity.badRequest().body("Invalid id");
        }
        return ResponseEntity.ok("User "+id);
    }
}