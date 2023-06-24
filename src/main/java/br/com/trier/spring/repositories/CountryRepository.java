package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    
    List<Country> findByNameStartingWithIgnoreCase(String name);
    
    Country findByName(String name);
    
}
