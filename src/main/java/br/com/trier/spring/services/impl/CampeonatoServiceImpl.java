package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.repositories.CampeonatoRepository;
import br.com.trier.spring.services.CampeonatoService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class CampeonatoServiceImpl implements CampeonatoService {
    
    @Autowired
    private CampeonatoRepository repository;
    
    private void findByYear (Campeonato campeonato) {
        Campeonato busca = repository.findByYear(campeonato.getYear());
        if (busca != null && busca.getId() != campeonato.getId()) {
            throw new IntegrityViolation("Equipe já existente");
        }
    }
    
    @Override
    public Campeonato findById (Integer id) {
        Optional <Campeonato> campeonato = repository.findById(id);
        return campeonato.orElseThrow(() -> new ObjectNotFound("O campeonato %s não existe".formatted(id)));
    }

    @Override
    public Campeonato insert(Campeonato campeonato) {
        return repository.save(campeonato);
    }

    @Override
    public List<Campeonato> listAll() {
        List <Campeonato> lista = repository.findAll();
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum campeonato cadastrado");
        }
        return lista;
    }

    @Override
    public Campeonato update(Campeonato campeonato) {
        findById(campeonato.getId());
        return repository.save(campeonato);
    }

    @Override
    public void delete(Integer id) {
        Campeonato campeonato = findById(id);
            repository.delete(campeonato);
    }
    
    @Override
    public List<Campeonato> findByChampDescStartingWithIgnoreCase(String champDesc) {
        List <Campeonato> lista = repository.findByChampDescStartingWithIgnoreCase(champDesc);
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum nome de campeonato inicia com %s".formatted(champDesc));
        }
        return lista;
    }
    
    @Override
    public List<Campeonato> findByYearBetweenOrderByYearAsc(Integer startYear, Integer endYear){
    	List<Campeonato> lista = repository.findByYearBetweenOrderByYearAsc(startYear, endYear);
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum campeonato encontrado entre %d e %d".formatted(startYear, endYear));
        }
        return lista;
    }

}
