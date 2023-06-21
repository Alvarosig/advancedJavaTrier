package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Pista;

@Repository
public interface PistaRepository extends JpaRepository<Pista, Integer> {

    List<Pista> findByNameStartsWithIgnoreCase (String name);
    
    List<Pista> findBySizeBetween (Integer sizeIni, Integer sizeFin);
    
    List<Pista> findByPaisOrderBySizeDesc (Pais pais);
    
}
