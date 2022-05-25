package com.core.rest.eden.utility;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.base.AbstractLogComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class TokenUtil extends AbstractLogComponent {

    //private final HttpServletRequest request;
    //private final HttpServletResponse response;

    //private final FilterChain filterChain;
    //private final Algorithm algorithm;

    public static Map<String, String> decodeToken(String accessToken) {

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        return new HashMap<>(){{
           put("username", username);
           Integer identifier = 0;
           for (String role : roles) {
                put("role"+(++identifier), role);
           }
        }};
    }

    public static Map<String, String> extractTokensFromCookie(Cookie[] cookies) throws NullPointerException {
        String accessToken = null;
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt-token")) {
                accessToken = cookie.getValue();
            }else if (cookie.getName().equals("refresh-Token")) {
                refreshToken = cookie.getValue();
            }
            //log.info("Cookie name: {} and value: {}", cookie.getName(), cookie.getValue());
        }

        String finalAccessToken  = accessToken;
        String finalRefreshToken = refreshToken;
        return new HashMap<>(){{
            put("access-token"  , finalAccessToken);
            put("refresh-token" , finalRefreshToken);
        }};
    }

    public Boolean generateToken(String authorizationHeader) throws IOException {
        //String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            /* Pass the token and filter out the word Bearer */
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                //Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                Algorithm algorithm = Algorithm.HMAC256("secret". getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return Boolean.TRUE;
                //filterChain.doFilter(request, response);
            } catch (Exception authenticationException) {
                //response.setHeader("Authorization Error", authenticationException.getMessage());
                //response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", authenticationException.getMessage());
                //response.setContentType(APPLICATION_JSON_VALUE);
                //new ObjectMapper().writeValue(response.getOutputStream(), error);
                //response.sendError(FORBIDDEN.value());
            }
        }
        return Boolean.FALSE;
    }

}
