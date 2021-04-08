package gordon.api.users;

import gordon.api.auth.AuthUserDetails;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
public class UserDataService {
    private final Logger log = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    UserRepository userRepository;

    public User retrieve(String username) throws Exception {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new Exception("Not Found: " + username));
        return user.get();
    }

    public User create(@Valid User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameExistsException();
        }

        return userRepository.save(user);
    }


}
