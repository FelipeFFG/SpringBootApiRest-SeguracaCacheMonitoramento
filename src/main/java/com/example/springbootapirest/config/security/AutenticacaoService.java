package com.example.springbootapirest.config.security;

import com.example.springbootapirest.model.Usuario;
import com.example.springbootapirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service        //servico de autenticacao
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;


    //o proprio spring chama essa classe, assim que o usuario tentar se logar.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //filtra no banco de dados pelo email e realiza a checagem da senha em memoria.
        Optional<Usuario> usuario = repository.findByEmail(username);                           //Obs: em uma aplicaoca real, normalmente usamos a filtragem pelo email e pela senha
        if (usuario.isPresent()){  //se tiver um usuario com esse username, ele retorna o usuario
            return usuario.get();
        }
        throw new UsernameNotFoundException("Dados Invalidos!");  //solta um erro dizendo que os dados estao errados.


    }




}
