package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.repositories.PaisRepository;
import br.com.trier.spring.services.PaisService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class PaisServiceImpl implements PaisService {

    @Autowired
    private PaisRepository repository;
    
    private void findByNameCountry(Pais pais) {
        Pais busca = repository.findByNameCountry(pais.getNameCountry());
        if (busca != null && busca.getId() != pais.getId()) {
            throw new IntegrityViolation("País já existente");
        }
    }
    
    @Override
    public Pais findById(Integer id) {
        Optional <Pais> pais = repository.findById(id);
        return pais.orElseThrow(() -> new ObjectNotFound("O país %s não existe".formatted(id)));
    }

    @Override
    public Pais insert(Pais pais) {
        return repository.save(pais);
    }

    @Override
    public List<Pais> listAll() {
        List<Pais> lista = repository.findAll();
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum país cadastrado");
        }
        return lista;
    }

    @Override
    public Pais update(Pais pais) {
        findById(pais.getId());
        return repository.save(pais);
    }

    @Override
    public void delete(Integer id) {
        Pais pais = findById(id);
        repository.delete(pais);
    }

    @Override
    public List<Pais> findByNameCountryStartingWithIgnoreCase(String nameCountry) {
        List<Pais> lista = repository.findByNameCountryStartingWithIgnoreCase(nameCountry);
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum nome de país inicia com %s".formatted(nameCountry));
        }
        return lista;
    }

}
