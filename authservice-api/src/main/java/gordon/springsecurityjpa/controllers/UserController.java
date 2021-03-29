package gordon.springsecurityjpa.controllers;

import gordon.springsecurityjpa.JwtUtil;
import gordon.springsecurityjpa.models.User;
import gordon.springsecurityjpa.models.UserControllerResponse;
import gordon.springsecurityjpa.models.UserRepository;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    JwtUtil jwtUtil;

    Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user){
        UserControllerResponse responseData = new UserControllerResponse();
        user.setRoles("USER");
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        responseData.setUser(user);
        try {
            userRepository.save(user);
        } catch (EntityExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(responseData);
        }
        return ResponseEntity.ok(user);
//        log.info("POST /users" + accountDto.getUsername());
//        User user = new User();
//        user.setUsername(accountDto.getUsername());
//        user.setPassword(accountDto.getPassword());
//
//        try {
//            userRepository.save(user);
//        } catch (Exception e) {
//            log.error(e.getMessage ());
//            return ResponseEntity.badRequest().build();
//        }
//
//        user.setPassword(null);
//        return ResponseEntity.ok(user);
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
