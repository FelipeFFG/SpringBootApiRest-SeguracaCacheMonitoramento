package com.example.springbootapirest.repository;

import com.example.springbootapirest.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Long> {


    Curso findBynome(String nomeCurso);
}
