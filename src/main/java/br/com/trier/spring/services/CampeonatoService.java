package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Campeonato;

public interface CampeonatoService {
    
    Campeonato findById (Integer id);
    
    Campeonato insert(Campeonato campeonato);
    
    List<Campeonato> listAll ();
    
    Campeonato update (Campeonato campeonato);
    
    void delete (Integer id);
    
    List<Campeonato> findByChampDescStartingWithIgnoreCase(String champDesc);
    
    List<Campeonato> findByYearBetweenOrderByYearAsc(Integer startYear, Integer endYear);
    
    List<Campeonato> findByYear (Integer year);

}
