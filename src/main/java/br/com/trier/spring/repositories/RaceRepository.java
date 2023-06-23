package br.com.trier.spring.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Championship;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.Track;

public interface RaceRepository extends JpaRepository<Race, Integer> {
    
    List<Race> findByDate(ZonedDateTime date);
    
    List<Race> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2);
    
    List<Race> findByTrackOrderByDate(Track track);
    
    List<Race> findByChampionshipOrderByDate(Championship championship);
}
