package gordon.api.service;

import gordon.api.persistence.User;
import gordon.api.persistence.UserRepository;
import gordon.api.validation.UserIdentityValidationGroup;
import gordon.api.validation.UsernameExistsException;
import gordon.api.web.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Id;
import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;


@Service
@Validated
public class UserDataService {

    Logger log = LoggerFactory.getLogger(this.getClass());

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

    public User registerNewUser(@Valid UserDto user) throws UsernameExistsException, ConstraintViolationException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user, UserIdentityValidationGroup.class);

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

    public User update(@NotNull int userId, @NotNull UserDto updated) {
        User toUpdate = userRepository.getById(userId);

        toUpdate.setUsername(updated.getUsername());

        return userRepository.save(toUpdate);
    }
}
