package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Pais;

public interface PaisService {
    
    Pais findById(Integer id);

    Pais insert(Pais pais);

    List<Pais> listAll();

    Pais update(Pais pais);

    void delete(Integer id);
    
    List<Pais> findByNameCountryStartingWithIgnoreCase(String nameCountry);
    
}
