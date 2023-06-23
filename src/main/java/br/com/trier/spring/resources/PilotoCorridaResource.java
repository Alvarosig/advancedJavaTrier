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

import br.com.trier.spring.models.PilotoCorrida;
import br.com.trier.spring.services.CorridaService;
import br.com.trier.spring.services.PilotoCorridaService;
import br.com.trier.spring.services.PilotoService;

@RestController
@RequestMapping("/pilotos/corridas")
public class PilotoCorridaResource {
	
	@Autowired
	private PilotoCorridaService service;
	
	@Autowired
	private PilotoService pilotoService;
	
	@Autowired
	private CorridaService corridaService;
	
	@GetMapping ("/{id}")
    public ResponseEntity <PilotoCorrida> findById (@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    ResponseEntity <PilotoCorrida> insert (@RequestBody PilotoCorrida pilotoCorrida) {
        pilotoService.findById(pilotoCorrida.getPiloto().getId());
        corridaService.findById(pilotoCorrida.getCorrida().getId());
        return ResponseEntity.ok(service.insert(pilotoCorrida));
    }
    
    @GetMapping
    ResponseEntity <List<PilotoCorrida>> listAll () {
        return ResponseEntity.ok(service.listAll());
    }
    
    @PutMapping("/{id}")
    ResponseEntity <PilotoCorrida> update (@PathVariable Integer id, @RequestBody PilotoCorrida pilotoCorrida) {
		return ResponseEntity.ok(service.update(pilotoCorrida));
    }
    
    @DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
    
    @GetMapping("/colocacao/{placement}")
	public ResponseEntity<List<PilotoCorrida>> findByPlacement(@PathVariable Integer placement){
		return ResponseEntity.ok(service.findByPlacement(placement));
	}
    
    @GetMapping("/piloto/{id}")
	public ResponseEntity<List<PilotoCorrida>> findByPilotoOrderByPlacement(@PathVariable Integer id){
		return ResponseEntity.ok(service.findByPilotoOrderByPlacement(pilotoService.findById(id)));
	}
	
	@GetMapping("/race/{id}")
	public ResponseEntity<List<PilotoCorrida>> findByCorridaOrderByPlacement(@PathVariable Integer id){
		return ResponseEntity.ok(service.findByCorridaOrderByPlacement(corridaService.findById(id)));
	}
}
