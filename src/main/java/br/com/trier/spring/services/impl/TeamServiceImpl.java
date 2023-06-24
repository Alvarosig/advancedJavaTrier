package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Team;
import br.com.trier.spring.repositories.TeamRepository;
import br.com.trier.spring.services.TeamService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository repository;
    
    private void validateTeamName(Team team) {
        Team existingTeam = repository.findByTeamName(team.getTeamName());
        if (existingTeam != null && !existingTeam.getId().equals(team.getId())) {
            throw new IntegrityViolation("Team already exists");
        }
    }
    
    @Override
    public Team findById(Integer id) {
        Optional<Team> team = repository.findById(id);
        return team.orElseThrow(() -> new ObjectNotFound("Team %s does not exist".formatted(id)));
    }

    @Override
    public Team insert(Team team) {
        validateTeamName(team);
        return repository.save(team);
    }

    @Override
    public List<Team> listAll() {
        List<Team> teamList = repository.findAll();
        if (teamList.isEmpty()) {
            throw new ObjectNotFound("No teams registered");
        }
        return teamList;
    }

    @Override
    public Team update(Team team) {
        findById(team.getId());
        validateTeamName(team);
        return repository.save(team);
    }

    @Override
    public void delete(Integer id) {
        Team team = findById(id);
        repository.delete(team);
    }
    
    @Override
    public List<Team> findByTeamNameStartingWithIgnoreCase(String teamName) {
        List<Team> teamList = repository.findByTeamNameStartingWithIgnoreCase(teamName);
        if (teamList.isEmpty()) {
            throw new ObjectNotFound("No team names start with %s".formatted(teamName));
        }
        return teamList;
    }

}
