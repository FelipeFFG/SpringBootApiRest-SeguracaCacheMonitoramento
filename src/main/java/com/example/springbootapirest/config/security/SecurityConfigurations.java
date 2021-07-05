package com.example.springbootapirest.config.security;


import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {       //todas as configuracaoes de seguracao ficam nessa classe


    //configuracoes de Autenticacao
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }


    //configuracaoes de Autorizacao
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()   //tudo que for "/topicos" ele libera o acesso.
                .antMatchers(HttpMethod.GET,"/topicos/*").permitAll(); //requisicoes get para /topicos/algumacoisa, está liberado para todos.
    }

    //configuracaoes de recursos estaticos(js,css,imagens,etc..)
    @Override
    public void configure(WebSecurity web) throws Exception {

    }
}
