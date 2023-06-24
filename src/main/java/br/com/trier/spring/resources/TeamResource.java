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
import br.com.trier.spring.services.TeamService;

@RestController
@RequestMapping(value = "/equipes")
public class TeamResource {
    
    @Autowired
    private TeamService service;
    
    @PostMapping
    public ResponseEntity<Team> insert (@RequestBody Team team) {
        return ResponseEntity.ok(service.insert((team)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Team> findById (@PathVariable Integer id) {
        Team team = service.findById(id);
        return ResponseEntity.ok(team);
    }
    
    @GetMapping
    public ResponseEntity<List<Team>> listAll () {
        return ResponseEntity.ok(service.listAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Team> update (@PathVariable Integer id, @RequestBody Team team) {
        team.setId(id);
        return ResponseEntity.ok(service.update(team));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/nome/{teamName}")
    public ResponseEntity<List<Team>> findByTeamNameStartingWithIgnoreCase (@PathVariable String teamName) {
        return ResponseEntity.ok(service.findByTeamNameStartingWithIgnoreCase(teamName));
    }
    
}
