package br.com.trier.spring.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.services.PaisService;

@RestController
@RequestMapping(value = "/paises")
public interface PaisResource {
    
    @Autowired
    private PaisService service;
    
    @PostMapping
    public ResponseEntity<Pais> insert (@RequestBody Pais pais) {
        Pais newPais = service.insert(pais);
        return newPais != null ? ResponseEntity.ok(newPais) : ResponseEntity.noContent().build();
    }
    
}
