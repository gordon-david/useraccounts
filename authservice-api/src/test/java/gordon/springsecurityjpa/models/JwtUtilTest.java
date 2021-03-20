package gordon.springsecurityjpa.models;

import gordon.springsecurityjpa.JwtUtil;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private ApplicationUserDetails makeTestUserSubject() {
        return new ApplicationUserDetails(new User() {{
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