package com.example.springbootapirest.controller;

import com.example.springbootapirest.dto.DetalhesDoTopicoDto;
import com.example.springbootapirest.dto.TopicoDto;
import com.example.springbootapirest.form.AtualizacaoTopicoForm;
import com.example.springbootapirest.form.TopicoForm;
import com.example.springbootapirest.model.Topico;
import com.example.springbootapirest.repository.CursoRepository;
import com.example.springbootapirest.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

//Por padrao ele assume que todos os metodos tem o @ReponseBody.
@RestController                            //ResponseBody - indica que o retorno do metodo deve ser serializado e devolvido no corpo da resposnta.
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")  //sinaliza que o motedo a agora esta sendo cacheado e passamos o value, que vai ser um id do cache para a aplicacao
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, //numero da pagina //quantidade de itens por pagina
                                @PageableDefault(sort = "id",direction = Sort.Direction.DESC,page=0,size=10) Pageable paginacao){         //required = true (obrigatorio passar como parametro.)
        if (nomeCurso==null){
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        }else{
            Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso,paginacao);  //filtra os topicos que tiverem o nome do curso igual o passado na url.
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos",allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@Valid @RequestBody TopicoForm form, UriComponentsBuilder uriBuilder){  //sinaliza pra pegar no corpo da requisição

        Topico topico =form.converter(cursoRepository);    //chama o repositorio do curso, pra buscar o curso com aquele nome, e converte os dados em um objeto TOPICO
        topicoRepository.save(topico);                     //salvamos o TOPICO no banco de dados.

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); //cria e converte o id em uri
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()){
            return  ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos",allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,@Valid @RequestBody AtualizacaoTopicoForm form){   //atualiza no  banco de dados automaticamente
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()){
            Topico topico = form.atualizar(id,topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos",allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id){
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
