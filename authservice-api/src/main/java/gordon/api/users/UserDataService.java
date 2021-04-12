package gordon.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;


@Service
@Validated
public class UserDataService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> retrieve(String username) {

        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username Can Not Be Null");
        }

        return userRepository.findByUsername(username);
    }

    public User registerNewUser(User user) throws UsernameExistsException, ConstraintViolationException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<User>> violations = validator.validate(user, UserIdentityValidationGroup.class);

        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UsernameExistsException();
        }

        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setActive(true);
        userEntity.setRoles("ROLE_USER");

        return userRepository.save(userEntity);
    }
}
