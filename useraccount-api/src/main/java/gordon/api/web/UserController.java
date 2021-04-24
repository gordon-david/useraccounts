package gordon.api.web;

import gordon.api.security.AuthUserDetails;
import gordon.api.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.validation.Validator;

@RestController
public class UserController {

  @Autowired
  UserDataService userDataService;

  Logger log = LoggerFactory.getLogger(UserController.class);

  @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createUser(@RequestBody UserDto user) {
    userDataService.registerNewUser(user);
    return ResponseEntity.accepted().build();
  }

  @PatchMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
  public ResponseEntity<Void> updateUser(@RequestBody UserDto userDto) {
    Object principleTemp = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    AuthUserDetails principle;

    if(userDto.getUsername().isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    if (!(principleTemp instanceof AuthUserDetails)) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    principle = (AuthUserDetails)principleTemp;

    userDataService.update(principle.getId(), userDto);

    return ResponseEntity.ok().build();
  }
}
