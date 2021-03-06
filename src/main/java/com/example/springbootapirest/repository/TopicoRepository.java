package com.example.springbootapirest.repository;

import com.example.springbootapirest.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico,Long> {




    Page<Topico> findByCurso_Nome(String nomeCurso, Pageable pageable);   //  _ :sinaliza que Curso é um tipo e que nome esta dentro dessa classe.

//    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
//    List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
}
