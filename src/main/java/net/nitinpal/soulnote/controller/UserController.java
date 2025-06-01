package net.nitinpal.soulnote.controller;

import net.nitinpal.soulnote.entity.User;
import net.nitinpal.soulnote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

//    @GetMapping
//    public ResponseEntity<?> getAllUser() {
//        try {
//            return ResponseEntity.ok(Collections.singletonMap("users found: ", userService.getAll()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error: ", e));
//        }
//    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User userInDB = userService.findByUsername(username);
            userInDB.setUsername(user.getUsername());
            userInDB.setPassword(user.getPassword());
            userService.updateUser(userInDB);
            return ResponseEntity.ok(Collections.singletonMap("message", "User Updated Successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("Error: ", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUsername(authentication.getName());
        return ResponseEntity.ok("User Deleted Successfully");
    }
}
