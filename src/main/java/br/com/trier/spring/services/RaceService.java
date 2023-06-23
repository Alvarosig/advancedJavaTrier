package br.com.trier.spring.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.spring.models.Championship;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.Track;

public interface RaceService {
	
	Race findById(Integer id);
    
	Race insert(Race race);
    
    List<Race> listAll();
    
    Race update(Race race);
    
    void delete(Integer id);
    
    List<Race> findByDate(ZonedDateTime date);
    
    List<Race> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2);
    
    List<Race> findByTrackOrderByDate(Track track);
    
    List<Race> findByChampionshipOrderByDate(Championship championship);
    
}
