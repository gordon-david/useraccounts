package gordon.springsecurityjpa;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "thisismyhardcodedsecretkeybutinproductionthisisobviouslyadumbidea";
    private static final Key KEY = new SecretKeySpec(JwtUtil.SECRET_KEY.getBytes(), "HmacSHA256");
    private static final long expirationTime = 1000 * 60 * 60 * 1; // expiration time length set to one hour

    public static String generateToken(UserDetails userDetails) {
        var claims = new HashMap<String, Object>();
        return JwtUtil.createToken(claims, userDetails.getUsername());
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = JwtUtil.extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !JwtUtil.isTokenExpired(token);
    }

    static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static String extractUsername(String token) {
        return JwtUtil.extractClaim(token, Claims::getSubject);
    }

    /*Private Methods*/

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtUtil.expirationTime))
                .signWith(JwtUtil.KEY)
                .compact();
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(JwtUtil.KEY).build().parseClaimsJws(token).getBody();
    }
}
