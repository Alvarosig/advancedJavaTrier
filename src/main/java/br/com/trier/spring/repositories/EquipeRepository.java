package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Integer>{
    
    List<Equipe> findByTeamNameStartingWithIgnoreCase(String teamName);
    
    Equipe findByTeamName (String teamName);
}
