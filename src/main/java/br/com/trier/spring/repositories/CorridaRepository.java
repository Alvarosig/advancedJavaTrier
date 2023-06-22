package br.com.trier.spring.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Pista;

public interface CorridaRepository extends JpaRepository<Corrida, Integer>{
	
	List<Corrida> findByDate(ZonedDateTime date);
    
    List<Corrida> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2);
    
    List<Corrida> findByPistaOrderByDate (Pista pista);
    
    List<Corrida> findByCampeonatoOrderByDate (Campeonato campeonato);
}
