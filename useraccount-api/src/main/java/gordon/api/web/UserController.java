package gordon.api.web;

import gordon.api.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

  @Autowired
  UserDataService userDataService;

  Logger log = LoggerFactory.getLogger(UserController.class);

  @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> createUser(@RequestBody @Valid UserDto user) {
    userDataService.registerNewUser(user);
    return ResponseEntity.accepted().build();
  }
}
