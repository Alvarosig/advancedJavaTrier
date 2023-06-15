package br.com.trier.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Integer> {

}
