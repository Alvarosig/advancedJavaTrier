package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.repositories.PaisRepository;
import br.com.trier.spring.services.PaisService;

@Service
public class PaisServiceImpl implements PaisService {

    @Autowired
    private PaisRepository repository;
    
    @Override
    public Pais findById(Integer id) {
        Optional <Pais> pais = repository.findById(id);
        return pais.orElse(null);
    }

    @Override
    public Pais insert(Pais pais) {
        return repository.save(pais);
    }

    @Override
    public List<Pais> listAll() {
        return repository.findAll();
    }

    @Override
    public Pais update(Pais pais) {
        return repository.save(pais);
    }

    @Override
    public void delete(Integer id) {
        Pais pais = findById(id);
        if (pais != null) {
            repository.delete(pais);
        }
    }

    @Override
    public List<Pais> findByNameCountryStartingWithIgnoreCase(String nameCountry) {
        return repository.findByNameCountryStartingWithIgnoreCase(nameCountry);
    }

}
