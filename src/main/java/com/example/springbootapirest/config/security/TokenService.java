package com.example.springbootapirest.config.security;


import com.example.springbootapirest.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;




    public String gerarToken(Authentication authentication){
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataexpiracao = new Date(hoje.getTime() +Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("API do forum da Alura")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataexpiracao)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public  boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();  //retorna o objeto do token
        return Long.parseLong(claims.getSubject());  //Transformando o Claims em um Long
    }
}
