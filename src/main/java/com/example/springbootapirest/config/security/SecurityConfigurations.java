package com.example.springbootapirest.config.security;


import antlr.Token;
import com.example.springbootapirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {       //todas as configuracaoes de seguracao ficam nessa classe

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //configuracoes de Autenticacao
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
            .passwordEncoder(new BCryptPasswordEncoder());

    }


    //configuracaoes de Autorizacao
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/topicos").permitAll()   //tudo que for "/topicos" ele libera o acesso.
                .antMatchers(HttpMethod.GET,"/topicos/*").permitAll() //requisicoes get para /topicos/algumacoisa, está liberado para todos.
                .antMatchers(HttpMethod.POST,"/auth").permitAll()     //liberando para qualquer pessoa poder acessar a pagina de login.
                .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/topicos/*").hasRole("MODERADOR")       //Autorizacao baseada no perfil do usuario.Para apenas moderador poder deletar.
                .anyRequest().authenticated()  //qualquer outra requisicao o usuario deve estar autenticado.
                .and().csrf().disable()   //csrf- contra ataque hack, porem como vamos usar o token, nao precisamos deixar habilitadado, pois o token ja nos protege contra este tipo de ataque.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //nao é pra criar sessao, pois iremos usar token.
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService,usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    //configuracaoes de recursos estaticos(js,css,imagens,etc..)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/webjars/**");
    }

   /* public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }*/
}
