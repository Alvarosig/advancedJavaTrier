package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Campeonato;

public interface CampeonatoService {
    
    Campeonato findById (Integer id);
    
    Campeonato insert(Campeonato campeonato);
    
    List<Campeonato> listAll ();
    
    Campeonato update (Campeonato campeonato);
    
    void delete (Integer id);

}
