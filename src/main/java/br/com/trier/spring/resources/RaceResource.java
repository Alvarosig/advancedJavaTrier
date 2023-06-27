package br.com.trier.spring.resources;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.dto.RaceDTO;
import br.com.trier.spring.services.ChampionshipService;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.TrackService;

@RestController
@RequestMapping("/corridas")
public class RaceResource {
	
	@Autowired
	private RaceService service;
	
	@Autowired
	private TrackService trackService;
	
	@Autowired
	private ChampionshipService championshipService;
	
	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
    public ResponseEntity<RaceDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id).toDTO());
    }
    
	@Secured({"ROLE_ADMIN"})
    @PostMapping
    ResponseEntity<RaceDTO> insert(@RequestBody Race race) {
        trackService.findById(race.getTrack().getId());
        championshipService.findById(race.getChampionship().getId());
        return ResponseEntity.ok(service.insert(race).toDTO());
    }
    
	@Secured({"ROLE_USER"})
    @GetMapping
    ResponseEntity<List<RaceDTO>> listAll() {
        return ResponseEntity.ok(service.listAll().stream().map(race -> race.toDTO()).toList());
    }
    
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    ResponseEntity<RaceDTO> update(@PathVariable Integer id, @RequestBody Race race) {
        trackService.findById(race.getTrack().getId());
        championshipService.findById(race.getChampionship().getId());
        race.setId(id);
        return ResponseEntity.ok(service.update(race).toDTO());
    }
    
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
    
    @Secured({"ROLE_USER"})
    @GetMapping("/campeonato/{id}")
    public ResponseEntity<List<RaceDTO>> findByChampionshipOrderByDate(@PathVariable Integer id){
    	return ResponseEntity.ok(service.findByChampionshipOrderByDate(championshipService.findById(id)).stream().map(race -> race.toDTO()).toList());
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/pista/{id}")
    public ResponseEntity<List<RaceDTO>> findByTrackOrderByDate(@PathVariable Integer id){
    	return ResponseEntity.ok(service.findByTrackOrderByDate(trackService.findById(id)).stream().map(race -> race.toDTO()).toList());
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/data/{date1}/{date2}")
	public ResponseEntity<List<RaceDTO>> findByDateBetween(@PathVariable ZonedDateTime date1, @PathVariable ZonedDateTime date2){
		return ResponseEntity.ok(service.findByDateBetween(date1, date2).stream().map(race -> race.toDTO()).toList());
	}
    
    @Secured({"ROLE_USER"})
    @GetMapping("/data/{date}")
	public ResponseEntity<List<RaceDTO>> findByDate(@PathVariable ZonedDateTime date){
		return ResponseEntity.ok(service.findByDate(date).stream().map(race -> race.toDTO()).toList());
	}
}
