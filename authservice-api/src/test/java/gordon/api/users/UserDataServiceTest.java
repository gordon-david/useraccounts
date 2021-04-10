package gordon.api.users;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManagerFactory;
import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@SpringBootTest
class UserDataServiceTest {

  private final String username = "testuser";
  private final String usernameUpdated = "testuserupdated";
  private final String password = "testuserpassword";
  private final String passwordUpdated = "testuserpasswordupdated";

  @InjectMocks
  UserDataService userDataService;

  @MockBean(answer = Answers.RETURNS_SMART_NULLS)
  UserRepository mockUserRepository;

  /*EntityManagerFactory is mocked to avoid requiring a running database connection,
  these tests do not rely on a live database connection*/
  @MockBean
  EntityManagerFactory entityManagerFactory;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @BeforeEach
  void setupTests() {
    MockitoAnnotations.initMocks(this);

  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(userDataService);
  }

  @Test
  void Given_UserExists_When_UserIsRetrievedByUsername_Then_UserIsReturned() throws Exception {
    User user = new User();

    // Arrange
    user.setUsername(username);
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Act
    User userFound = userDataService.retrieve(username).get();

    // Assert
    Assertions.assertNotNull(userFound);
    Assertions.assertEquals(userFound.getUsername(), user.getUsername());
  }

  @Test
  void Given_UserDoesNotExist_When_UserIsRetrievedByUsername_Then_ExceptionThrown() {

    boolean exceptionThrown = false;

    // Arrange
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act
    try {
      userDataService.retrieve(username);
    } catch (Exception e) {
      exceptionThrown = true;
    }

    // Assert
    Assertions.assertTrue(exceptionThrown);
  }

  @Test
  void Given_UsernameIsNotInRepository_When_UserIsCreated_Then_UserIsReturned() throws Exception {
    User user = new User();

    user.setUsername(username);
    user.setPassword(password);
    Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenReturn(user);

    User userReturned = userDataService.create(user);

    Assertions.assertNotNull(userReturned);
    Assertions.assertEquals(user.getUsername(), userReturned.getUsername());
  }

  @Test
  void Given_UsernameIsInRepository_When_UserIsCreated_Then_UsernameExistsExceptionIsThrown() {
    User user = new User();
    User userReturned;
    boolean usernameExistsExceptionThrown = false;
    user.setUsername(username);
    user.setPassword(password);
    Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

    try {
      userReturned = userDataService.create(user);
    } catch (UsernameExistsException e) {
      usernameExistsExceptionThrown = true;
    }

    Assertions.assertTrue(usernameExistsExceptionThrown);
  }

  @Test
  void Given_UsernameIsNull_When_UserIsCreated_Then_ConstraintViolationExceptionIsThrown() {
    User user = new User();
    boolean constraintViolationExceptionThrown = false;

    try {
      userDataService.create(user);
    } catch (javax.validation.ConstraintViolationException e) {
      if (e.getConstraintViolations().stream().anyMatch(violation
              -> checkConstrainViolation(
              violation,
              "username",
              javax.validation.constraints.NotNull.class)
      )) {
        constraintViolationExceptionThrown = true;
      }
    }

    Assertions.assertTrue(constraintViolationExceptionThrown);
    Mockito.verifyNoInteractions(mockUserRepository);
  }

  @Test
  void Given_PasswordIsNull_When_UserIsCreated_Then_ConstrainViolationExceptionIsThrown() {
    User user = new User();
    user.setPassword(null);
    boolean constraintViolationExceptionThrown = false;

    try {
      userDataService.create(user);
    } catch (ConstraintViolationException e) {
      if (e.getConstraintViolations().stream()
              .anyMatch(violation
                      -> checkConstrainViolation(
                      violation,
                      "password",
                      javax.validation.constraints.NotNull.class))) {
        constraintViolationExceptionThrown = true;
      }
    }

    Assertions.assertTrue(constraintViolationExceptionThrown);
    Mockito.verifyNoInteractions(mockUserRepository);
  }

  private boolean checkConstrainViolation(ConstraintViolation<?> violation, String property, Class<?> constraintType) {
    return violation.getPropertyPath().toString().equals(property)
            && constraintType.equals(violation.getConstraintDescriptor().getAnnotation().annotationType());
  }
}