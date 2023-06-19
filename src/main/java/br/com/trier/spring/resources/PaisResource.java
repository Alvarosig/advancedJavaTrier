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

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.services.PaisService;

@RestController
@RequestMapping(value = "/paises")
public class PaisResource {
    
    @Autowired
    private PaisService service;
    
    @PostMapping
    public ResponseEntity<Pais> insert (@RequestBody Pais pais) {
        Pais newPais = service.insert(pais);
        return ResponseEntity.ok(newPais);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pais> findById (@PathVariable Integer id) {
        Pais pais = service.findById(id);
        return ResponseEntity.ok(pais);
    }
    
    @GetMapping
    public ResponseEntity<List<Pais>> listAll () {
        List<Pais> lista = service.listAll();
        return ResponseEntity.ok(lista);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pais> update (@PathVariable Integer id, @RequestBody Pais pais) {
        pais.setId(id);
        return ResponseEntity.ok(service.update(pais));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/nome/{nameCountry}")
    public ResponseEntity<List<Pais>> findByNameStartingWithIgnoreCase (@PathVariable String nameCountry) {
        List<Pais> lista = service.findByNameCountryStartingWithIgnoreCase(nameCountry);
        return ResponseEntity.ok(lista);
    }
   
}
