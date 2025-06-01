package net.nitinpal.soulnote.controller;

import net.nitinpal.soulnote.entity.User;
import net.nitinpal.soulnote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> savUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error: ", e.getMessage()));
        }
    }
}
