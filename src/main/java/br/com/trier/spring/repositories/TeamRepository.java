package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    
    List<Team> findByTeamNameStartingWithIgnoreCase(String teamName);
    
    Team findByTeamName(String teamName);
}
