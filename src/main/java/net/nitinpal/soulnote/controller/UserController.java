package net.nitinpal.soulnote.controller;

import net.nitinpal.soulnote.entity.User;
import net.nitinpal.soulnote.service.UserService;
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
            return ResponseEntity.ok(Collections.singletonMap("users found: ", userService.getAll()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ", e));
        }
    }

    @PostMapping
    public ResponseEntity<?> savUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error: ", e.getMessage()));
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username) {
        try {
            User userInDB = userService.findByUsername(username);
            if (userInDB == null) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error: ", "User not Found"));
            } else {
                userInDB.setUsername(user.getUsername());
                if ( user.getPassword() == null || user.getPassword().isEmpty()) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error: ", "Password cannot be empty"));
                }
                userInDB.setPassword(user.getPassword());
                userService.saveUser(userInDB);
            }
            return ResponseEntity.ok(Collections.singletonMap("message", "User Updated Successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Error: ", e.getMessage()));
        }
    }
}
