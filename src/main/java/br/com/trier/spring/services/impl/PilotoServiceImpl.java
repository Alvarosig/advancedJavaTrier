package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.repositories.PilotoRepository;
import br.com.trier.spring.services.PilotoService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class PilotoServiceImpl implements PilotoService {
    
    @Autowired
    private PilotoRepository pilotoRepository;
    
    
    @Override
    public Piloto insert(Piloto piloto) {
        return pilotoRepository.save(piloto);
    }
    
    @Override
    public Piloto update(Piloto piloto) {
        findById(piloto.getId());
        return pilotoRepository.save(piloto);
    }
    
    @Override
    public void delete(Integer id) {
        Piloto piloto = findById(id);
        pilotoRepository.delete(piloto);   
    }
    
    @Override
    public List<Piloto> listAll() {
        List<Piloto> lista = pilotoRepository.findAll(); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado");
        }
        return lista;
    }
    
    @Override
    public Piloto findById(Integer id) {
        return pilotoRepository.findById(id).orElseThrow(() -> new ObjectNotFound ("Piloto %s não existe".formatted(id)));
    }

    @Override
    public List<Piloto> findByNameStartsWithIgnoreCase(String name) {
        List<Piloto> lista = pilotoRepository.findByNameStartsWithIgnoreCase(name); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado com esse nome");
        }
        return lista;
    }

    @Override
    public List<Piloto> findByPaisOrderByNameDesc(Pais pais) {
        List<Piloto> lista = pilotoRepository.findByPaisOrderByNameDesc(pais); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado no país: %s".formatted(pais.getNameCountry()));
        }
        return lista;
    }

    @Override
    public List<Piloto> findByEquipeOrderByTeamNameDesc(Equipe equipe) {
        List<Piloto> lista = pilotoRepository.findByEquipeOrderByTeamNameDesc(equipe); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado na equipe: %s".formatted(equipe.getTeamName()));
        }
        return lista;
    }

}
