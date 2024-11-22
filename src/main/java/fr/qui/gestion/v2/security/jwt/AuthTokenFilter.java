package fr.qui.gestion.v2.security.jwt;

import fr.qui.gestion.v2.security.CustomUtilisateurDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger authLogger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUtilisateurDetailsService customUtilisateurDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, CustomUtilisateurDetailsService customUtilisateurDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUtilisateurDetailsService = customUtilisateurDetailsService;
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = customUtilisateurDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Jeton JWT malformé");
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException("Jeton JWT expiré");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedOperationException("Jeton JWT non pris en charge");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Chaîne JWT vide ou invalide");
        }

        filterChain.doFilter(request, response);
    }


    private void handleAuthenticationError(HttpServletResponse response, int statusCode, String message) throws IOException {
        authLogger.error(message);
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
