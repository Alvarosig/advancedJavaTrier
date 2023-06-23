package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.repositories.CountryRepository;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository repository;
    
    private void findByNameCountryExists(Country country) {
        Country search = repository.findByNameCountry(country.getName());
        if (search != null && search.getId() != country.getId()) {
            throw new IntegrityViolation("Country already exists");
        }
    }
    
    @Override
    public Country findById(Integer id) {
        Optional<Country> country = repository.findById(id);
        return country.orElseThrow(() -> new ObjectNotFound("Country %s does not exist".formatted(id)));
    }

    @Override
    public Country insert(Country country) {
        findByNameCountryExists(country);
        return repository.save(country);
    }

    @Override
    public List<Country> listAll() {
        List<Country> list = repository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("No countries registered");
        }
        return list;
    }

    @Override
    public Country update(Country country) {
        findById(country.getId());
        findByNameCountryExists(country);
        return repository.save(country);
    }

    @Override
    public void delete(Integer id) {
        Country country = findById(id);
        repository.delete(country);
    }

    @Override
    public List<Country> findByNameStartingWithIgnoreCase(String countryName) {
        List<Country> list = repository.findByNameCountryStartingWithIgnoreCase(countryName);
        if (list.isEmpty()) {
            throw new ObjectNotFound("No country names start with %s".formatted(countryName));
        }
        return list;
    }

}
