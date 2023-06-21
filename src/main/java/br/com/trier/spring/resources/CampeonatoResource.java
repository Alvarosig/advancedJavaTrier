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
import br.com.trier.spring.models.dto.CampeonatoDTO;
import br.com.trier.spring.services.CampeonatoService;

@RestController
@RequestMapping(value = "/campeonatos")
public class CampeonatoResource {

    @Autowired
    private CampeonatoService service; 
    
    @PostMapping
    public ResponseEntity<CampeonatoDTO> insert (@RequestBody CampeonatoDTO campeonato) {
    	return ResponseEntity.ok(service.insert(new Campeonato (campeonato)).toDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CampeonatoDTO> findById (@PathVariable Integer id) {
    	Campeonato campeonato = service.findById(id);
        return ResponseEntity.ok(campeonato.toDTO());
    }
    
    @GetMapping
    public ResponseEntity<List<CampeonatoDTO>> listAll () {
    	return ResponseEntity.ok(service.listAll().stream().map((campeonato) -> campeonato.toDTO()).toList());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CampeonatoDTO> update (@PathVariable Integer id, @RequestBody CampeonatoDTO campeonatoDTO) {
    	Campeonato campeonato = new Campeonato (campeonatoDTO);
    	campeonato.setId(id);
        return ResponseEntity.ok(service.update(campeonato).toDTO());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build(); 
    }
    
    @GetMapping("/nome/{champDesc}")
    public ResponseEntity<List<CampeonatoDTO>> findByChampDescStartingWithIgnoreCase(@PathVariable String champDesc){
    	return ResponseEntity.ok(service.findByChampDescStartingWithIgnoreCase(champDesc).stream().map((campeonato) -> campeonato.toDTO()).toList());
    }
    
    @GetMapping("/entre/{startYear}/{endYear}")
    public ResponseEntity <List<CampeonatoDTO>> findByYearBetweenOrderByYearAsc(@PathVariable Integer startYear, @PathVariable Integer endYear) {
    	return ResponseEntity.ok(service.findByYearBetweenOrderByYearAsc(startYear, endYear).stream().map((campeonato) -> campeonato.toDTO()).toList());
    }
}
