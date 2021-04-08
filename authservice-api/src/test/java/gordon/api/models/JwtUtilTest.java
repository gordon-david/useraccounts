package gordon.api.models;

import gordon.api.auth.AuthUserDetails;
import gordon.api.common.JwtUtil;
import gordon.api.users.User;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private AuthUserDetails makeTestUserSubject() {
        return new AuthUserDetails(new User() {{
            setUsername("testUser");
            setRoles("ROLE_USER");
        }});
    }

    @Test
    void generateToken() {
        var userDetails = this.makeTestUserSubject();
        String result = JwtUtil.generateToken(userDetails);
        Assert.isTrue(result.length() > 3);
    }

    @Test
    void extractUsername() {
        var userDetails = this.makeTestUserSubject();
        String token = JwtUtil.generateToken(userDetails);
        String result = JwtUtil.extractUsername(token);
        Assert.isTrue(
                result.equals(userDetails.getUsername()),
                "result:" + result + ", expected: " + userDetails.getUsername());
    }

}