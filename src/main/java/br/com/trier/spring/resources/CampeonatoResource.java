package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.services.CampeonatoService;

@RestController
@RequestMapping(value = "/campeonatos")
public class CampeonatoResource {

    @Autowired
    private CampeonatoService service;
    
    @PostMapping
    public ResponseEntity<Campeonato> insert (@RequestBody Campeonato campeonato) {
        Campeonato newCampeonato = service.insert(campeonato);
        return newCampeonato != null ? ResponseEntity.ok(newCampeonato) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Campeonato> findById (@PathVariable Integer id) {
        Campeonato campeonato = service.findById(id);
        return campeonato != null ? ResponseEntity.ok(campeonato) : ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Campeonato>> listAll () {
        List<Campeonato> lista = service.listAll();
        return lista.size() > 0 ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Campeonato> update (@PathVariable Integer id, @RequestBody Campeonato campeonato) {
        campeonato.setId(id);
        campeonato = service.update(campeonato);
        return campeonato != null ? ResponseEntity.ok(campeonato) : ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
}
