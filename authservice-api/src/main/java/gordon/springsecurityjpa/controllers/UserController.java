package gordon.springsecurityjpa.controllers;

import gordon.springsecurityjpa.JwtUtil;
import gordon.springsecurityjpa.models.User;
import gordon.springsecurityjpa.models.UserRepository;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    JwtUtil jwtUtil;

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(User accountDto){
        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setPassword(accountDto.getPassword());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> retrieveCollection(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> retrieveSingle(@PathVariable("id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user;

        try {
            user = userRepository.findById(id).orElseThrow();
            if(!user.getUsername().equals(userDetails.getUsername())){
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        user.setPassword("");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<?> updatePartial(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
