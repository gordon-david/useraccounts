package gordon.api.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDataServiceUpdateTest {

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
  void Given_NullUsername_Then_ThrowConstraintViolationException() {
    User original = new User();
    User updated = new User();
    boolean constraintViolationThrown = false;

    original.setPassword(password);
    original.setUsername(null);

    updated.setUsername(usernameUpdated);
    updated.setPassword(passwordUpdated);

    try {
      userDataService.update(original, updated);
    } catch (ConstraintViolationException e) {
      if (e.getConstraintViolations().stream()
              .anyMatch(violation -> violation.getPropertyPath().toString().equals("username")
                      && javax.validation.constraints.NotNull.class
                      .equals(violation.getConstraintDescriptor().getAnnotation().annotationType()))
      ) constraintViolationThrown = true;
    }

    assertTrue(constraintViolationThrown);
  }

  @Autowired
  MethodValidationPostProcessor methodValidationPostProcessor;

  @Test
  void simple(){
    Assertions.assertNotNull(methodValidationPostProcessor);
    try {
      userDataService.test(null);
    } catch (ConstraintViolationException e) {
      log.error(e.getLocalizedMessage());
      assertTrue(true);
      return;
    }

    Assertions.fail();
  }


}