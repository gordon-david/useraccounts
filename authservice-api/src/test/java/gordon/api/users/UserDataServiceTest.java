package gordon.api.users;

import org.aspectj.bridge.MessageUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;

@SpringBootTest
class UserDataServiceTest {

    private final String username = "testuser";
    private final String usernameUpdated = "testuserupdated";
    private final String password = "testuserpassword";
    private final String passwordUpdated = "testuserpasswordupdated";

    @InjectMocks
    UserDataService userDataService;

    @Mock
    UserRepository mockUserRepository;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeEach
    void setupTests() throws AssertionError {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void contextLoads(){
        Assert.assertNotNull(userDataService);
    }

    @Test
    void Given_UserExists_When_UserIsRetrievedByUsername_Then_UserIsReturned() throws Exception {
        User user = new User();

        // Arrange
        user.setUsername(username);
        Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User userFound = userDataService.retrieve(username);

        // Assert
        Assert.assertNotNull(userFound);
        Assert.assertEquals(userFound.getUsername(), user.getUsername());
    }

    @Test
    void Given_UserDoesNotExist_When_UserIsRetrievedByUsername_Then_ExceptionThrown(){
        User user = new User();
        Boolean exceptionThrown = false;

        // Arrange
        user.setUsername(username);
        Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        try {
            User userFound = userDataService.retrieve(username);
        } catch (Exception e) {
            exceptionThrown = true;
        }

        // Assert
        Assert.assertTrue(exceptionThrown);
    }

    @Test
    void Given_UsernameIsNotInRepository_When_UserIsCreated_Then_UserIsReturned() throws Exception {
        User user = new User();

        user.setUsername(username);
        Mockito.when(mockUserRepository.save(Mockito.any(User.class))).thenReturn(user);

        User userReturned = userDataService.create(user);

        Assert.assertNotNull(userReturned);
        Assert.assertEquals(user.getUsername(), userReturned.getUsername());
    }

    @Test
    void Given_UsernameIsInRepository_When_UserIsCreated_Then_ExceptionIsThrown () {
        User user = new User();
        User userReturned;
        Boolean usernameExistsExceptionThrown = false;
        user.setUsername(username);
        Mockito.when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(mockUserRepository.save(Mockito.any())).thenThrow(ConstraintViolationException.class);

        try {
            userReturned = userDataService.create(user);
        } catch (UsernameExistsException e){
            usernameExistsExceptionThrown = true;
        }

        Assert.assertTrue(usernameExistsExceptionThrown);
    }

    @Test
    void Given_UsernameIsNull_When_UserIsCreated_Then_ConstraintViolationExceptionIsThrown(){
        User user = new User();
        Boolean constraintViolationExceptionThrown = false;

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        assert validator.validate(user).toArray().length != 0;

        for (ConstraintViolation<User> violation : validator.validate(user)) {
            log.error(violation.getPropertyPath().toString());
        }

        try {
            userDataService.create(user);
        } catch (Exception e) {
            constraintViolationExceptionThrown = true;
        }

        Assert.assertTrue(constraintViolationExceptionThrown);
    }
}