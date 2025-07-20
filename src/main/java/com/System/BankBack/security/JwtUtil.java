package com.System.BankBack.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 32+ chars → 256 bit key para HS256
    private static final String SECRET = "SuperClaveSecretaDe32Caracteres!!!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(UserDetails user) {
        String role = user.getAuthorities().iterator().next().getAuthority();   // ROLE_ADMIN …

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000)) // 24 h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();   //  ← IDE ya no marcará error
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()   // crea un parser
                .setSigningKey(key) // le indica la clave secreta para verificar la firma
                .build()            // construye el parser
                .parseClaimsJws(token) // valida el token HS256 y lo “abre”
                .getBody();            // devuelve el cuerpo → objeto Claims
    }

}
