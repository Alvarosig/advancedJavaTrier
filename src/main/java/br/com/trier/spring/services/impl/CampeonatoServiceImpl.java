package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.repositories.CampeonatoRepository;
import br.com.trier.spring.services.CampeonatoService;

public class CampeonatoServiceImpl implements CampeonatoService {
    
    @Autowired
    private CampeonatoRepository repository;
    
    @Override
    public Campeonato findById (Integer id) {
        Optional <Campeonato> campeonato = repository.findById(id);
        return campeonato.orElse(null);
    }

    @Override
    public Campeonato insert(Campeonato campeonato) {
        return repository.save(campeonato);
    }

    @Override
    public List<Campeonato> listAll() {
        return repository.findAll();
    }

    @Override
    public Campeonato update(Campeonato campeonato) {
        return repository.save(campeonato);
    }

    @Override
    public void delete(Integer id) {
        Campeonato campeonato = findById(id);
        if (campeonato != null) {
            repository.delete(campeonato);
        }
    }

}
