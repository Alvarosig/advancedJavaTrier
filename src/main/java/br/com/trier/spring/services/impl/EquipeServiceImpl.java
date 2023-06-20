package br.com.trier.spring.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.repositories.EquipeRepository;
import br.com.trier.spring.services.EquipeService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class EquipeServiceImpl implements EquipeService {

    @Autowired
    private EquipeRepository repository;
    
    private void findByTeamNameExists(Equipe equipe) {
        Equipe busca = repository.findByTeamName(equipe.getTeamName());
        if (busca != null && busca.getId() != equipe.getId()) {
            throw new IntegrityViolation("Equipe já cadastrada");
        }
    }
    
    @Override
    public Equipe findById(Integer id) {
        Optional <Equipe> equipe = repository.findById(id);
        return equipe.orElseThrow(() -> new ObjectNotFound("A equipe %s não existe".formatted(id)));
    }

    @Override
    public Equipe insert(Equipe equipe) {
    	findByTeamNameExists(equipe);
        return repository.save(equipe);
    }

    @Override
    public List<Equipe> listAll() {
        List <Equipe> lista = repository.findAll();
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma equipe cadastrada");
        }
        return lista;
    }

    @Override
    public Equipe update(Equipe equipe) {
        findById(equipe.getId());
       	findByTeamNameExists(equipe);
        return repository.save(equipe);
    }

    @Override
    public void delete(Integer id) {
        Equipe equipe = findById(id);
        repository.delete(equipe);
    }
    
    @Override
    public List<Equipe> findByTeamNameStartingWithIgnoreCase (String teamName) {
        List <Equipe> lista = repository.findByTeamNameStartingWithIgnoreCase(teamName);
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum nome de equipe inicia com %s".formatted(teamName));
        }
        return lista;
    }

}
