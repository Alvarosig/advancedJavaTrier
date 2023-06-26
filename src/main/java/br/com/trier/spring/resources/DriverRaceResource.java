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

import br.com.trier.spring.models.DriverRace;
import br.com.trier.spring.services.DriverRaceService;
import br.com.trier.spring.services.DriverService;
import br.com.trier.spring.services.RaceService;

@RestController
@RequestMapping("/pilotos/corridas")
public class DriverRaceResource {
	
	@Autowired
	private DriverRaceService service;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private RaceService raceService;
	
	@GetMapping("/{id}")
    public ResponseEntity<DriverRace> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    ResponseEntity<DriverRace> insert(@RequestBody DriverRace driverRace) {
        driverService.findById(driverRace.getDriver().getId());
        raceService.findById(driverRace.getRace().getId());
        return ResponseEntity.ok(service.insert(driverRace));
    }
    
    @GetMapping
    ResponseEntity<List<DriverRace>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }
    
    @PutMapping("/{id}")
    ResponseEntity<DriverRace> update(@PathVariable Integer id, @RequestBody DriverRace driverRace) {
        return ResponseEntity.ok(service.update(driverRace));
    }
    
    @DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
    
    @GetMapping("/colocacao/{placement}")
	public ResponseEntity<List<DriverRace>> findByPlacement(@PathVariable Integer placement) {
		return ResponseEntity.ok(service.findByPlacement(placement));
	}
    
    @GetMapping("/piloto/{id}")
	public ResponseEntity<List<DriverRace>> findByDriverOrderByPlacement(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByDriverOrderByPlacement(driverService.findById(id)));
	}
	
	@GetMapping("/corrida/{id}")
	public ResponseEntity<List<DriverRace>> findByRaceOrderByPlacement(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByRaceOrderByPlacement(raceService.findById(id)));
	}
}
