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

import br.com.trier.spring.models.Team;
import br.com.trier.spring.models.dto.TeamDTO;
import br.com.trier.spring.services.TeamService;

@RestController
@RequestMapping(value = "/teams")
public class TeamResource {
    
    @Autowired
    private TeamService service;
    
    @PostMapping
    public ResponseEntity<TeamDTO> insert (@RequestBody TeamDTO team) {
        return ResponseEntity.ok(service.insert(new Team(team)).toDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findById (@PathVariable Integer id) {
        Team team = service.findById(id);
        return ResponseEntity.ok(team.toDTO());
    }
    
    @GetMapping
    public ResponseEntity<List<TeamDTO>> listAll () {
        return ResponseEntity.ok(service.listAll().stream().map((team) -> team.toDTO()).toList());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update (@PathVariable Integer id, @RequestBody TeamDTO teamDTO) {
        Team team = new Team(teamDTO);
        team.setId(id);
        return ResponseEntity.ok(service.update(team).toDTO());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/nome/{teamName}")
    public ResponseEntity<List<TeamDTO>> findByTeamNameStartingWithIgnoreCase (@PathVariable String teamName) {
        return ResponseEntity.ok(service.findByTeamNameStartingWithIgnoreCase(teamName).stream().map((team) -> team.toDTO()).toList());
    }
    
}
