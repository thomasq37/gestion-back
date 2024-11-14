package fr.qui.gestion.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(String username, List<String> roles) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();

        // Construire le JWT sans expiration
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512);

        // Ajouter l'expiration si jwtExpirationMs n'est pas 0
        if (jwtExpirationMs > 0) {
            Date expirationDate = new Date(now.getTime() + jwtExpirationMs);
            jwtBuilder.setExpiration(expirationDate);
        }

        return jwtBuilder.compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Jeton JWT malformé : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Jeton JWT expiré : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jeton JWT non pris en charge : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Chaîne JWT vide : {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
