package gordon.api.test;

import gordon.api.Application;
import gordon.api.persistence.User;
import gordon.api.persistence.UserRepository;
import gordon.api.service.UserDataService;
import gordon.api.web.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class})
public class UserDataServiceIntegrationTest {

  @Autowired
  UserDataService userDataService;

  @Autowired
  UserRepository userRepository;

  User userInDatabase;

  @BeforeEach
  void setup(){
    userInDatabase = userDataService.registerNewUser((new UserDto()).setUsername("user").setPassword("password"));
  }

  @Test
  void canUpdateUsername(){
    UserDto userDto = new UserDto()
            .setUsername("user_updated");

    userDataService.update(userInDatabase.getId(), userDto);

    Optional<User> updatedEntity = userDataService.retrieve(userDto.getUsername());

    assertTrue(updatedEntity.isPresent());
    assertEquals(updatedEntity.get().getUsername(), userDto.getUsername());
  }

  @Test
  void canDeleteAUser(){
    userDataService.deleteById(userInDatabase.getId());
    assertTrue(userRepository.findByUsername(userInDatabase.getUsername()).isEmpty());
  }
}