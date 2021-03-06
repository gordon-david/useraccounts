package gordon.api.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gordon.api.Application;
import gordon.api.service.UserDataService;
import gordon.api.spring.SecurityConfiguration;
import gordon.api.persistence.User;
import gordon.api.persistence.UserRepository;
import gordon.api.web.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@SpringBootTest(classes = {Application.class, SecurityConfiguration.class, }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationIntegrationTest {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @LocalServerPort
  private int port;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserDataService userDataService;
  @Autowired
  ObjectMapper objectMapper;
  RestTemplate restTemplate;
  HttpHeaders headers;

  @BeforeEach
  void setup() {
    restTemplate = new RestTemplate();
    headers = new HttpHeaders();
    userRepository.deleteAll();
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(userRepository);
    Assertions.assertNotNull(userDataService);
    Optional<User> temp = userRepository.findByUsername("notindatabase");
    Assertions.assertTrue(temp.isEmpty());
  }

  @Test
  void Given_NotPersistedUser_When_UserCreationRequest_Then_UserIsPersisted() throws JsonProcessingException {
    UserDto newUser = new UserDto();
    newUser.setUsername("username");
    newUser.setPassword("password");

    String requestBody = objectMapper.writeValueAsString(newUser);
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.POST, httpEntity, String.class);
    Optional<User> fromRepository = userRepository.findByUsername(newUser.getUsername());

    Assertions.assertTrue(fromRepository.isPresent());
    Assertions.assertEquals(fromRepository.get().getUsername(), newUser.getUsername());
  }
}
