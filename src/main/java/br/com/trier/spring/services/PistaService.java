package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Pista;

public interface PistaService {
    
    List<Pista> findByNameStartsWithIgnoreCase (String name);
    
    List<Pista> findBySizeBetween (Integer sizeIni, Integer sizeFin);
    
    List<Pista> findByPaisOrderBySizeDesc (Pais pais);
    
    Pista findById (Integer id);
    
    Pista insert(Pista pista);
    
    List<Pista> listAll ();
    
    Pista update (Pista pista);
    
    void delete (Integer id);
}
