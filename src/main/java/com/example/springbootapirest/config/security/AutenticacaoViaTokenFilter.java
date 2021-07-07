package com.example.springbootapirest.config.security;

import antlr.Token;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    private TokenService tokenService;

    public AutenticacaoViaTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token= recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);
        System.out.println(valido);

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }




    private String recuperarToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token==null || token.isEmpty() || !token.startsWith("Bearer ")){   //se esta vindo o token || se ele nao é vazio || nao começa com Bearer
                return null;
        }
        return token.substring(7,token.length());
    }
}
