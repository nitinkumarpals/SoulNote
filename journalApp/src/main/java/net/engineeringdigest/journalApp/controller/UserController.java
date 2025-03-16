package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        try {
            return ResponseEntity.ok(Collections.singletonMap("Users found: ",userService.getAll()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error: ", e));
        }
    }

    @PostMapping
    public ResponseEntity<?> savUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("Error: ", e.getMessage()));
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username) {
        try {
            User userInDB = userService.findByUsername(username);
            if(userInDB ==null){
                return ResponseEntity.badRequest().body(Collections.singletonMap("Error: ","User not Found"));
            }
            else {
                userInDB.setUsername(user.getUsername());
                userInDB.setPassword(user.getPassword());
                userService.saveUser(userInDB);
            }
            return ResponseEntity.ok(Collections.singletonMap("Message", "User Updated Successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Error: ", e.getMessage()));
        }
    }
}
