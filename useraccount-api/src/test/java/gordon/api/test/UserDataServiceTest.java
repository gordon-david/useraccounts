package gordon.api.test;

import gordon.api.persistence.User;
import gordon.api.persistence.UserRepository;
import gordon.api.service.UserDataService;
import gordon.api.validation.UsernameExistsException;
import gordon.api.web.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserDataServiceTest {

  private final String username = "testuser";
  private final String password = "testuserpassword";
  private UserDto userDto;
  private User userEntity;

  @InjectMocks
  UserDataService userDataService;

  @MockBean(answer = Answers.RETURNS_SMART_NULLS)
  UserRepository mockUserRepository;

  @MockBean PasswordEncoder passwordEncoder;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @BeforeEach
  void setupTests() {
    MockitoAnnotations.initMocks(this);
    userDto = new UserDto();
    userDto.setUsername(username);
    userDto.setPassword(password);

    userEntity = new User();
    userEntity.setUsername(username);
    userEntity.setPassword(password);
    userEntity.setActive(true);
    userEntity.setRoles("ROLE_USER");
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(userDataService);
  }

  @Test
  void Given_UserExists_When_UserIsRetrievedByUsername_Then_UserIsReturned() throws Exception {
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

    // Act
    User userFound = userDataService.retrieve(username).get();

    // Assert
    Assertions.assertNotNull(userFound);
    Assertions.assertEquals(userFound.getUsername(), username);
  }

  @Test
  void Given_UserDoesNotExist_When_UserIsRetrievedByUsername_Then_ReturnEmptyOptional() {
    Optional<User> userRetrieved;
    boolean exceptionThrown = false;
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());
    userRetrieved = userDataService.retrieve(username);
    Assertions.assertTrue(userRetrieved.isEmpty());
  }

  @Test
  void Given_UsernameIsNotInRepository_When_UserIsCreated_Then_UserIsReturned() throws Exception {
    Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenReturn(userEntity);
    User userReturned = userDataService.registerNewUser(userDto);
    Assertions.assertNotNull(userReturned);
  }

  @Test
  void Given_UsernameIsInRepository_When_UserIsCreated_Then_UsernameExistsExceptionIsThrown() {
    boolean usernameExistsExceptionThrown = false;
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

    try {
      userDataService.registerNewUser(userDto);
    } catch (UsernameExistsException e) {
      usernameExistsExceptionThrown = true;
    }

    Assertions.assertTrue(usernameExistsExceptionThrown);
  }

  @Test
  void When_UserIsCreated_Then_PasswordIsEncoded(){
    String passwordEncoded = "password_encoded";
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());
    Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(passwordEncoded);
    Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0, User.class));

    User userReturned = userDataService.registerNewUser(userDto);

    Assertions.assertEquals(userReturned.getPassword(), passwordEncoded);
  }
}