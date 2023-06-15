package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Equipe;

public interface EquipeService {
    
    Equipe findById (Integer id);
    
    Equipe insert(Equipe equipe);
    
    List<Equipe> listAll ();
    
    Equipe update (Equipe equipe);
    
    void delete (Integer id);
}
