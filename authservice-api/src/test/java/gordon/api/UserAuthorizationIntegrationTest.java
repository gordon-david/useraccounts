package gordon.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gordon.api.auth.AuthUserDetails;
import gordon.api.auth.AuthenticationRequest;
import gordon.api.auth.AuthenticationResponse;
import gordon.api.common.JwtUtil;
import gordon.api.auth.SecurityConfiguration;
import gordon.api.users.*;
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

@SpringBootTest(classes = {Application.class, SecurityConfiguration.class, UserConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAuthorizationIntegrationTest {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @LocalServerPort private int port;
  @Autowired UserRepository userRepository;
  @Autowired UserDataService userDataService;
  @Autowired ObjectMapper objectMapper;
  TestRestTemplate restTemplate;
  HttpHeaders headers;

  @BeforeEach
  void setup() {
    restTemplate = new TestRestTemplate();
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
  void Given_PersistedUser_When_UserRequestsJwt_Then_ResponseContainsJwt() throws JsonProcessingException {
    User user = new User();
    user.setUsername("username");
    user.setPassword("password");
    user.setRoles("USER");
    user.setActive(true);
    userDataService.registerNewUser(user);

    AuthenticationRequest requestBody = new AuthenticationRequest();
    requestBody.setUsername(user.getUsername());
    requestBody.setPassword(user.getPassword());

    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

    ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/users/authenticate", HttpMethod.POST, httpEntity, String.class);

    log.info(response.toString());

    AuthenticationResponse authenticationResponse = objectMapper.readValue(response.getBody(), AuthenticationResponse.class);
    String jwt = authenticationResponse.getJwt();

    Assertions.assertNotNull(jwt);
    Assertions.assertTrue(JwtUtil.validateToken(jwt, new AuthUserDetails(user)));
  }

  @Test
  void Given_UserAuthenticatedWithJwt_When_UserResourceRequests_Then_Returns200(){
    User user = new User();
    user.setUsername("username");
    user.setPassword("password");
    user.setRoles("USER");
    user.setActive(true);
    userDataService.registerNewUser(user);

    AuthUserDetails userDetails = new AuthUserDetails(user);

    String jwt = JwtUtil.generateToken(userDetails);

    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/usertestresource", HttpMethod.GET, httpEntity, String.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}