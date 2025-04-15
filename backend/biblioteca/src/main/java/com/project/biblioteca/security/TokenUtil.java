package com.project.biblioteca.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.project.biblioteca.model.UsuarioGestor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenUtil {
    
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 60*60*1000;
    private static final String SECRET_KEY = "xT+xIhH0JpJUrUQNYsRtzyHqR6Y7/9Z4mKXcIUlQEpY=";
    private static final String EMISSOR = "biblioteca-api";

    public static String criarToken(UsuarioGestor usuario) {
        Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        String token = Jwts.builder()
            .setSubject(usuario.getCpf())
            .setIssuer(EMISSOR)
            .claim("roles", List.of("ROLE_GESTOR"))
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

    public String extrairCpfDoToken(String token) {
        try {
            Claims jwsClaims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

            return jwsClaims.getSubject(); 
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token JWT inválido ou expirado", e);
        }
    }

    public static Authentication validate (HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        
        if (token == null || !token.startsWith(PREFIX)) return null;
    
        token = token.replace(PREFIX, "");
    
        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token);
    
            String cpf = jwsClaims.getBody().getSubject();
            String issuer = jwsClaims.getBody().getIssuer();
            Date expiration = jwsClaims.getBody().getExpiration();
    
            if (isExpirationValid(expiration) && isEmissiorValid(issuer) && isUser(cpf)) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_GESTOR"));
                return new UsernamePasswordAuthenticationToken(cpf, null, authorities);
            }

            return null;
    
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Erro ao validar token: " + e.getMessage());
            return null;
        }
    }
}
