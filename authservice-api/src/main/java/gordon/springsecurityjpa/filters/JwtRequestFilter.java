package gordon.springsecurityjpa.filters;

import gordon.springsecurityjpa.JwtUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Adds user auth information to the Spring Security Context (once) before
 * hitting the resource controller.
 */
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    FilterChain filterChain
  )
    throws ServletException, IOException {
    final String authorizationHeader = httpServletRequest.getHeader(
      "Authorization"
    );
    String username = null;
    String jwt = null;

    if (
      authorizationHeader != null && authorizationHeader.startsWith(("Bearer "))
    ) {
      jwt = authorizationHeader.substring(7);
      username = JwtUtil.extractUsername(jwt);
    }

    if (
      username != null &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (JwtUtil.validateToken(jwt, userDetails)) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
        );
        SecurityContextHolder
          .getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
