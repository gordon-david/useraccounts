package gordon.api.auth;

import gordon.api.users.User;
import gordon.api.users.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    UserDataService userDataService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user;

         user = userDataService.retrieve(username);
         if(user.isEmpty()) throw new UsernameNotFoundException("Not Found: " + username);

        return new AuthUserDetails(user.get());
    }

}
