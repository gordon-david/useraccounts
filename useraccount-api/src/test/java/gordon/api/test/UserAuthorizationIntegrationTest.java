package gordon.api.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gordon.api.Application;
import gordon.api.security.AuthUserDetails;
import gordon.api.service.UserDataService;
import gordon.api.web.AuthenticationRequest;
import gordon.api.web.AuthenticationResponse;
import gordon.api.security.JwtUtil;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

@SpringBootTest(classes = {Application.class, SecurityConfiguration.class, }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAuthorizationIntegrationTest {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @LocalServerPort private int port;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserDataService userDataService;
  @Autowired ObjectMapper objectMapper;
  TestRestTemplate restTemplate;
  HttpHeaders headers;
  UserDto userDto;
  User userEntity;
  AuthUserDetails authUserDetails;

  @BeforeEach
  void setup() {
    restTemplate = new TestRestTemplate();
    headers = new HttpHeaders();
    userRepository.deleteAll();

    userDto = new UserDto();
    userDto.setUsername("username");
    userDto.setPassword("password");

    userEntity = new User();
    userEntity.setPassword("password");
    userEntity.setUsername("username");
    userEntity.setRoles("ROLE_USER");
    userEntity.setActive(true);

    authUserDetails = new AuthUserDetails(userEntity);
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(userRepository);
    Assertions.assertNotNull(userDataService);
    Optional<User> temp = userRepository.findByUsername("notindatabase");
    Assertions.assertTrue(temp.isEmpty());
  }

  @Test
  void Given_PersistedUser_When_UserRequestsJwt_Then_ResponseContainsJwt() throws JsonProcessingException {
    userDataService.registerNewUser(userDto);

    AuthenticationRequest requestBody = new AuthenticationRequest();
    requestBody.setUsername(userDto.getUsername());
    requestBody.setPassword(userDto.getPassword());

    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

    ResponseEntity<AuthenticationResponse> response = restTemplate.exchange("http://localhost:" + port + "/users/authenticate", HttpMethod.POST, httpEntity, AuthenticationResponse.class);
    String jwt = response.getBody().getJwt();

    Assertions.assertNotNull(jwt);
    Assertions.assertTrue(JwtUtil.validateToken(jwt, authUserDetails));
  }

  @Test
  void Given_UserAuthenticatedWithJwt_When_UserResourceRequests_Then_Returns200(){
    userDataService.registerNewUser(userDto);

    String jwt = JwtUtil.generateToken(authUserDetails);

    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/usertestresource", HttpMethod.GET, httpEntity, String.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}