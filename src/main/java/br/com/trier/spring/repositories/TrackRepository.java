package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

    List<Track> findByNameStartsWithIgnoreCase(String name);

    List<Track> findBySizeBetween(Integer sizeIni, Integer sizeFin);

    List<Track> findByCountryOrderBySizeDesc(Country country);
}
