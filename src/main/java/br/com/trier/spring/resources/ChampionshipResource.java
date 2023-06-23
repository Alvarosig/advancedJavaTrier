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

import br.com.trier.spring.models.Championship;
import br.com.trier.spring.models.dto.ChampionshipDTO;
import br.com.trier.spring.services.ChampionshipService;

@RestController
@RequestMapping(value = "/championships")
public class ChampionshipResource {

    @Autowired
    private ChampionshipService service; 
    
    @PostMapping
    public ResponseEntity<ChampionshipDTO> insert(@RequestBody ChampionshipDTO championshipDTO) {
        Championship championship = new Championship(championshipDTO);
        Championship createdChampionship = service.insert(championship);
        return ResponseEntity.ok(createdChampionship.toDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChampionshipDTO> findById(@PathVariable Integer id) {
        Championship championship = service.findById(id);
        return ResponseEntity.ok(championship.toDTO());
    }
    
    @GetMapping
    public ResponseEntity<List<ChampionshipDTO>> listAll() {
        List<Championship> championships = service.listAll();
        List<ChampionshipDTO> championshipDTOs = championships.stream()
                .map(Championship::toDTO)
                .toList();
        return ResponseEntity.ok(championshipDTOs);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ChampionshipDTO> update(@PathVariable Integer id, @RequestBody ChampionshipDTO championshipDTO) {
        Championship championship = new Championship(championshipDTO);
        championship.setId(id);
        Championship updatedChampionship = service.update(championship);
        return ResponseEntity.ok(updatedChampionship.toDTO());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build(); 
    }
    
    @GetMapping("/nome/{champDesc}")
    public ResponseEntity<List<ChampionshipDTO>> findByChampDescStartingWithIgnoreCase(@PathVariable String champDesc) {
        List<Championship> championships = service.findByChampDescStartingWithIgnoreCase(champDesc);
        List<ChampionshipDTO> championshipDTOs = championships.stream()
                .map(Championship::toDTO)
                .toList();
        return ResponseEntity.ok(championshipDTOs);
    }
    
    @GetMapping("/entre/{startYear}/{endYear}")
    public ResponseEntity<List<ChampionshipDTO>> findByYearBetweenOrderByYearAsc(@PathVariable Integer startYear, @PathVariable Integer endYear) {
        List<Championship> championships = service.findByYearBetweenOrderByYearAsc(startYear, endYear);
        List<ChampionshipDTO> championshipDTOs = championships.stream()
                .map(Championship::toDTO)
                .toList();
        return ResponseEntity.ok(championshipDTOs);
    }
    
    @GetMapping("/ano/{year}")
    public ResponseEntity<List<ChampionshipDTO>> findByYear(@PathVariable Integer year) {
        List<Championship> championships = service.findByYear(year);
        List<ChampionshipDTO> championshipDTOs = championships.stream()
                .map(Championship::toDTO)
                .toList();
        return ResponseEntity.ok(championshipDTOs);
    }
}
