package br.com.trier.spring.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Pista;

public interface CorridaService {
	
	Corrida findById (Integer id);
    
	Corrida insert(Corrida corrida);
    
    List<Corrida> listAll ();
    
    Corrida update (Corrida corrida);
    
    void delete (Integer id);
    
    List<Corrida> findByDate(ZonedDateTime date);
    
    List<Corrida> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2);
    
    List<Corrida> findByPistaOrderByDate (Pista pista);
    
    List<Corrida> findByCampeonatoOrderByDate (Campeonato campeonato);
    
}
