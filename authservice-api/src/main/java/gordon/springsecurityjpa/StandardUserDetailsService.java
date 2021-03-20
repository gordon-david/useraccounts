package gordon.springsecurityjpa;

import gordon.springsecurityjpa.models.ApplicationUserDetails;
import gordon.springsecurityjpa.models.User;
import gordon.springsecurityjpa.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StandardUserDetailsService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(StandardUserDetailsService.class);
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + username));
        return user.map(ApplicationUserDetails::new).get();
    }
}
