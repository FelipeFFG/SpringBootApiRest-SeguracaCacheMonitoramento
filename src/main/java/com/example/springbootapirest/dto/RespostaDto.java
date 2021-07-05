package com.example.springbootapirest.dto;

import com.example.springbootapirest.model.Resposta;

import java.time.LocalDateTime;

public class RespostaDto {

    private Long id;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String nomeAutor;

    public RespostaDto(Resposta resposta) {
        id = resposta.getId();
        mensagem = resposta.getMensagem();
        dataCriacao = resposta.getDataCriacao();
        nomeAutor = resposta.getAutor().getNome();
    }


    public Long getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }
}
