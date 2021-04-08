package gordon.api.auth;

import gordon.api.users.User;
import gordon.api.users.UserDataService;
import gordon.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataService userDataService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        try {
         user = userDataService.retrieve(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Not Found: " + username);
        }

        return new AuthUserDetails(user);
    }

}
