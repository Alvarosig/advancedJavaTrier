package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Piloto;

public interface PilotoRepository extends JpaRepository <Piloto, Integer>{
    
    List<Piloto> findByNameStartsWithIgnoreCase (String name);
    
    Piloto findByNameEqualsIgnoreCase (String name);
    
    List<Piloto> findByPaisOrderByNameDesc (Pais pais);
 
    List<Piloto> findByEquipeOrderByNameDesc (Equipe equipe);
}
