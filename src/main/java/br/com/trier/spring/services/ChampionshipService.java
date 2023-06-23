package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Championship;

public interface ChampionshipService {
    
    Championship findById(Integer id);
    
    Championship insert(Championship championship);
    
    List<Championship> listAll();
    
    Championship update(Championship championship);
    
    void delete(Integer id);
    
    List<Championship> findByChampDescStartingWithIgnoreCase(String champDesc);
    
    List<Championship> findByYearBetweenOrderByYearAsc(Integer startYear, Integer endYear);
    
    List<Championship> findByYear(Integer year);

}
