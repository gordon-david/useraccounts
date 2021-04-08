package gordon.api.users;

import gordon.api.common.JwtUtil;

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
    UserDataService userDataService;

    @Autowired
    PasswordEncoder passwordEncoder;

    JwtUtil jwtUtil;

    Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserControllerResponse> createUser(@RequestBody User user){
        /* Local Variable Declaration */
        UserControllerResponse responseData = new UserControllerResponse();

        /* Assert required parameters present in request */
        if(user == null){
            responseData.setMessage("user data required");
            return ResponseEntity.badRequest().body(responseData);
        }
        if(user.getPassword() == null || user.getPassword().equals("")){
            responseData.setMessage("password required");
            return ResponseEntity.badRequest().body(responseData);
        }
        if(user.getUsername() == null || user.getUsername().equals("")) {
            responseData.setMessage("username required");
            return ResponseEntity.badRequest().body(responseData);
        }

        /* auto set certain user parameters to defaults */
        user.setRoles("USER");
        user.setActive(true);

        /* encode the password received in the request */
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /* place the user in the response data we will return to the client */
        responseData.setUser(user);

        /* save user to persistence, handle exceptions */
        try {
            userDataService.create(user);
        } catch (EntityExistsException e){
            responseData.setMessage("username exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

        responseData.setMessage("accepted");
        return ResponseEntity.accepted().body(responseData);
    }

//    @GetMapping(value = "/users")
//    public ResponseEntity<?> retrieveCollection(){
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }

//    @GetMapping(value = "/users/{id}")
//    public ResponseEntity<?> retrieveSingle(@PathVariable("id") String id, @AuthenticationPrincipal UserDetails userDetails) {
//        User user;
//
//        try {
//            user = user(id).orElseThrow();
//            if(!user.getUsername().equals(userDetails.getUsername())){
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        user.setPassword("");
//        return ResponseEntity.status(HttpStatus.OK).body(user);
//    }
//
//    @PutMapping(value = "/users/{id}")
//    public ResponseEntity<?> update(@PathVariable("id") String id) {
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }
//
//    @PatchMapping(value = "/users/{id}")
//    public ResponseEntity<?> updatePartial(@PathVariable("id") String id) {
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }
//
//    @DeleteMapping(value = "/users/{id}")
//    public ResponseEntity<?> delete(@PathVariable("id") String id) {
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }
}
