package gordon.springsecurityjpa.controllers;

import gordon.springsecurityjpa.JwtUtil;
import gordon.springsecurityjpa.models.ApplicationUserDetails;
import gordon.springsecurityjpa.models.AuthenticationRequest;
import gordon.springsecurityjpa.models.AuthenticationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * RestController that handles user authentication.
 * 
 * DEPENDENCIES: AuthenticationManager, UserDetailsService
 * 
 */

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;
    Logger logging = LoggerFactory.getLogger(AuthController.class);

    /**
     * Issues an authentication token
     * 
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/users/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        Authentication auth;

        logging.info("/users/authenticate");
        logging.info(authenticationRequest.toString());
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        } catch (LockedException e) {
            throw new Exception("Locked", e);
        } catch (DisabledException e) {
            throw new Exception("Disabled", e);
        }

        final ApplicationUserDetails userDetails = (ApplicationUserDetails) userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = JwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
        return ResponseEntity.ok(authenticationResponse);
    }

    // Revoking an authentication token
    // TODO Not Implemented
    @DeleteMapping(value = "/users/authenticate")
    public ResponseEntity<?> revokeAuthenticationToken() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
