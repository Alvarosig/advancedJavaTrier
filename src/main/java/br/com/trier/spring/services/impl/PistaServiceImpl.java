package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Pista;
import br.com.trier.spring.repositories.PistaRepository;
import br.com.trier.spring.services.PistaService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class PistaServiceImpl implements PistaService {
    
    @Autowired
    private PistaRepository pistaRepository;
    
    private void validatePista (Pista pista) {
        if (pista.getSize() == null || pista.getSize() <= 0) {
            throw new IntegrityViolation("Tamanho da pista inválido");
        }
    }
       
    @Override
    public List<Pista> findByNameStartsWithIgnoreCase(String name) {
        List<Pista> lista = pistaRepository.findByNameStartsWithIgnoreCase(name); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista cadastrada com esse nome");
        }
        return lista;
    }

    @Override
    public List<Pista> findBySizeBetween(Integer sizeIni, Integer sizefin) {
        List<Pista> lista = pistaRepository.findBySizeBetween(sizeIni, sizefin); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma Pista Cadastrada com essas medidas");
        }
        return lista;
    }

    @Override
    public List<Pista> findByPaisOrderBySizeDesc(Pais pais) {
        List<Pista> lista = pistaRepository.findByPaisOrderBySizeDesc(pais); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma pista cadastrada no país: %s".formatted(pais.getNameCountry()));
        }
        return lista;
    }

    @Override
    public Pista findById(Integer id) {
       return pistaRepository.findById(id).orElseThrow(() -> new ObjectNotFound ("Pista %s não existe".formatted(id)));
    }

    @Override
    public Pista insert(Pista pista) {
       validatePista(pista);
       return pistaRepository.save(pista);
    }

    @Override
    public List<Pista> listAll() {
       List<Pista> lista = pistaRepository.findAll(); 
       if (lista.isEmpty()) {
           throw new ObjectNotFound("Nenhuma pista cadastrada");
       }
       return lista;
    }

    @Override
    public Pista update(Pista pista) {
       findById(pista.getId());
       validatePista(pista);
       return pistaRepository.save(pista);
    }

    @Override
    public void delete(Integer id) {
        Pista pista = findById(id);
        pistaRepository.delete(pista);
        
    }

}
