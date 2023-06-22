package br.com.trier.spring.resources;

import java.time.ZonedDateTime;
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

import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.services.CampeonatoService;
import br.com.trier.spring.services.CorridaService;
import br.com.trier.spring.services.PistaService;

@RestController
@RequestMapping("/corridas")
public class CorridaResource {
	
	@Autowired
	private CorridaService service;
	
	@Autowired
	private PistaService pistaService;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@GetMapping ("/{id}")
    public ResponseEntity <Corrida> findById (@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    ResponseEntity <Corrida> insert (@RequestBody Corrida corrida) {
        pistaService.findById(corrida.getPista().getId());
        campeonatoService.findById(corrida.getCampeonato().getId());
        return ResponseEntity.ok(service.insert(corrida));
    }
    
    @GetMapping
    ResponseEntity <List<Corrida>> listAll () {
        return ResponseEntity.ok(service.listAll());
    }
    
    @PutMapping("/{id}")
    ResponseEntity <Corrida> update (@PathVariable Integer id, @RequestBody Corrida corrida) {
    	pistaService.findById(corrida.getPista().getId());
        campeonatoService.findById(corrida.getCampeonato().getId());
        corrida.setId(id);
        return ResponseEntity.ok(service.update(corrida));
    }
    
    @DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
    
    @GetMapping("/corrida/{id}")
    public ResponseEntity<List<Corrida>> findByCampeonatoOrderByName(Integer id){
    	Campeonato campeonato = service.findById(id);
        List<Corrida> corridas = service.findByCampeonatoOrderByName(campeonato);
        return ResponseEntity.ok(corridas);
    }

    @GetMapping("/data/{date1}/{date2}")
	public ResponseEntity<List<Corrida>> findByDateBetween(@PathVariable ZonedDateTime date1, @PathVariable ZonedDateTime date2){
		return ResponseEntity.ok(service.findByDateBetween(date1, date2));
	}
    
    @GetMapping("/data/{date}")
	public ResponseEntity<List<Corrida>> findByDate(@PathVariable ZonedDateTime date){
		return ResponseEntity.ok(service.findByDate(date));
	}
}
