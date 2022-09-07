package com.core.rest.eden.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.helpers.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccessTokenServiceImpl extends AbstractLogComponent implements AccessTokenService {

    /**
     * Bad practice,
     * should be replaced with a .env value.
     * Spring IoC needs initialisation of TOKEN_SECRET
     **/
    private static String TOKEN_SECRET = "secret";

    private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET.getBytes());
    private static final JWTVerifier verifier = JWT.require(algorithm).build();



    @Override
    public DecodedJWT decodeToken(@NotNull String token) throws TokenExpiredException {
        return verifier.verify(token);
    }

    @Override
    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    @Override
    public Date extractExpiration(String token) {
        return decodeToken(token).getExpiresAt();
    }

    @Override
    public Map<String, Claim> extractClaims(String token) {
        return decodeToken(token).getClaims();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public String generateToken(User user, String issuer) {
        Map<String, List<String>> claims = new HashMap<>() {{
            put("roles", user
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList())
            );
        }};
        return createToken(claims, user.getUsername(), issuer);
    }

    @Override
    public String generateRefreshToken(User user, String issuer) {
        Map<String, List<String>> claims = new HashMap<>() {{
            put("roles", user
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList())
            );
        }};
        return createRefreshToken(claims, user.getUsername(), issuer);
    }

    @Override
    public String generateToken(com.core.rest.eden.domain.User user, String issuer) {
        Map<String, List<String>> claims = new HashMap<>() {{
            put("roles", user
                    .getRoles()
                    .stream()
                    .map(Role::toString)
                    .collect(Collectors.toList())
            );
        }};
        return createToken(claims, user.getUsername(), issuer);
    }

    @Override
    public String generateRefreshToken(com.core.rest.eden.domain.User user, String issuer) {
        Map<String, List<String>> claims = new HashMap<>() {{
            put("roles", user
                    .getRoles()
                    .stream()
                    .map(Role::toString)
                    .collect(Collectors.toList())
            );
        }};
        return createRefreshToken(claims, user.getUsername(), issuer);
    }

    @Override
    public String createToken(Map<String, List<String>> claims, String subject, String issuer) {
        return JWT
                .create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", claims.get("roles"))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    @Override
    public String createRefreshToken(Map<String, List<String>> claims, String subject, String issuer) {
        return JWT
                .create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withClaim("roles", claims.get("roles"))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    @Override
    public Boolean validateToken(String token, String username) {
        return decodeToken(token).getSubject().equals(username) && !isTokenExpired(token);
    }
}
