package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Piloto;

public interface PilotoService {
    
    Piloto findById(Integer id);

    Piloto insert(Piloto piloto);

    List<Piloto> listAll();

    Piloto update(Piloto piloto);

    void delete(Integer id);
    
    List<Piloto> findByNameStartsWithIgnoreCase (String name);
    
    List<Piloto> findByPaisOrderByNameDesc (Pais pais);
 
    List<Piloto> findByEquipeOrderByNameDesc (Equipe equipe);
}
