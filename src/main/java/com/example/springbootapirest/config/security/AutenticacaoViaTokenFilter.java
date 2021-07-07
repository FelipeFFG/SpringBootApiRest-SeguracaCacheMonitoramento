package com.example.springbootapirest.config.security;

import antlr.Token;
import com.example.springbootapirest.model.Usuario;
import com.example.springbootapirest.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    private TokenService tokenService;

    private UsuarioRepository usuarioRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService,UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token= recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);
        if (valido){  //se o tokes estiver valido-true
            autenticarCliente(token);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);
         Usuario usuario = usuarioRepository.findById(idUsuario).get();
         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
         SecurityContextHolder.getContext().setAuthentication(authentication);
        }



    private String recuperarToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token==null || token.isEmpty() || !token.startsWith("Bearer ")){   //se esta vindo o token || se ele nao é vazio || nao começa com Bearer
                return null;
        }
        return token.substring(7,token.length());
    }
}
