package gordon.api.web;

import gordon.api.security.AuthUserDetails;
import gordon.api.security.JwtUtil;

import gordon.api.validation.UserIdentityValidationGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * RestController that handles user authentication.
 * <p>
 * DEPENDENCIES: AuthenticationManager, UserDetailsService
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
   */
  @PostMapping(value = "/users/authenticate")
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody @Validated(UserIdentityValidationGroup.class) UserDto authenticationRequest)
          throws Exception {
    Authentication auth;

    auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(), authenticationRequest.getPassword()));

    final AuthUserDetails userDetails = (AuthUserDetails) userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());
    final String jwt = JwtUtil.generateToken(userDetails);
    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
    return ResponseEntity.ok(authenticationResponse);
  }
}
