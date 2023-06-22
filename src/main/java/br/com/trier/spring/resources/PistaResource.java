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

import br.com.trier.spring.models.Pista;
import br.com.trier.spring.services.CampeonatoService;
import br.com.trier.spring.services.PaisService;
import br.com.trier.spring.services.PistaService;

@RestController
@RequestMapping("/pistas")
public class PistaResource {
    
    @Autowired
    private PistaService service;
    
    @Autowired
    private CampeonatoService campeonatoService;
    
    @Autowired
    private PaisService paisService;
    
    @GetMapping ("/{id}")
    public ResponseEntity <Pista> findById (@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    ResponseEntity <Pista> insert (@RequestBody Pista pista) {
        paisService.findById(pista.getPais().getId());
        return ResponseEntity.ok(service.insert(pista));
    }
    
    @GetMapping
    ResponseEntity <List<Pista>> listAll () {
        return ResponseEntity.ok(service.listAll());
    }
    
    @PutMapping("/{id}")
    ResponseEntity <Pista> update (@PathVariable Integer id, @RequestBody Pista pista) {
        paisService.findById(pista.getPais().getId());
        pista.setId(id);
        return ResponseEntity.ok(service.update(pista));
    }
    
    @DeleteMapping
    ResponseEntity <Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping ("/nome/{name}")
    ResponseEntity <List<Pista>> findByNameStartsWithIgnoreCase (@PathVariable String name) {
        return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));
    }
    
    @GetMapping ("/tamanho/{sizeIni}/{sizeFin}")
    ResponseEntity <List<Pista>> findBySizeBetween (@PathVariable Integer sizeIni, @PathVariable Integer sizefin) {
        return ResponseEntity.ok(service.findBySizeBetween(sizeIni, sizefin));
    }
    
    @GetMapping ("/pais/{idPais}")
    ResponseEntity <List<Pista>> findByPaisOrderBySizeDesc (@PathVariable Integer idPais) {
        return ResponseEntity.ok(service.findByPaisOrderBySizeDesc(paisService.findById(idPais)));
    }
    
}
