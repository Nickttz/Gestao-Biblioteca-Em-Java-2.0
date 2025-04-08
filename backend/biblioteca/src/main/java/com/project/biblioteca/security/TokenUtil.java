package com.project.biblioteca.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.project.biblioteca.model.UsuarioGestor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class TokenUtil {
    
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 60*60*1000;
    private static final String SECRECT_KEY = "xT+xIhH0JpJUrUQNYsRtzyHqR6Y7/9Z4mKXcIUlQEpY=";
    private static final String EMISSOR = "biblioteca-api";

    public static String criarToken(UsuarioGestor usuario) {
        Key secretKey = Keys.hmacShaKeyFor(SECRECT_KEY.getBytes());

        String token = Jwts.builder()
            .setSubject(usuario.getCpf())
            .setIssuer(EMISSOR)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
        
        return PREFIX + token;
    }

    private static Boolean isExpirationValid (Date expiration) {
        return expiration.after(new Date(System.currentTimeMillis()));
    }

    private static Boolean isEmissiorValid (String emissor) {
        return emissor.equals(EMISSOR);
    }

    private static Boolean isUser (String cpf) {
        return (cpf != null && cpf.length() > 0);
    }

    public static Authentication validate (HttpServletRequest request) {
        String token = request.getHeader(HEADER);

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRECT_KEY.getBytes()))
            .build()
            .parseClaimsJws(token);
    
        String cpf = jwsClaims.getBody().getSubject();
        String issuer = jwsClaims.getBody().getIssuer();
        Date expiration = jwsClaims.getBody().getExpiration();

        if (isExpirationValid(expiration) && isEmissiorValid(issuer) && isUser(cpf)) {
            return new UsernamePasswordAuthenticationToken(cpf, null, Collections.emptyList());
        }
        return null;
    }
}
