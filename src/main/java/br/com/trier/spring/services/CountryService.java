package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Country;

public interface CountryService {
    
    Country findById(Integer id);

    Country insert(Country country);

    List<Country> listAll();

    Country update(Country country);

    void delete(Integer id);
    
    List<Country> findByNameStartingWithIgnoreCase(String countryName);
    
}
