package gordon.api.users;

import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Validated
public class UserDataService {
    private final Logger log = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    UserRepository userRepository;

    public Optional<User> retrieve(String username) {
        Optional<User> fromRepository;

        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username Can Not Be Null");
        }

        return userRepository.findByUsername(username);
    }

    public User create(User user) throws UsernameExistsException, ConstraintViolationException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


        Set<ConstraintViolation<User>> violations = validator.validate(user, UserIdentityValidationGroup.class);

        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UsernameExistsException();
        }

        return userRepository.save(user);
    }
}
