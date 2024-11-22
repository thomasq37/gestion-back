package fr.qui.gestion.v2.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService {

	private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private static String authToken;
    @Value("${api.key}")
    public void setAuthToken(String authToken) {
    	AuthenticationService.authToken = authToken;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(authToken)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}