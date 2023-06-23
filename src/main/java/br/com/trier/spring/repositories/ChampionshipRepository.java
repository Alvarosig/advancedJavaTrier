package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Championship;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Integer> {
    
    List<Championship> findByChampDescStartingWithIgnoreCase(String champDesc);
    
    List<Championship> findByYearBetweenOrderByYearAsc(Integer startYear, Integer endYear);
    
    List<Championship> findByYear(Integer year);
    
}
