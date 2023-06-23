package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Track;

public interface TrackService {
    
    List<Track> findByNameStartsWithIgnoreCase(String name);
    
    List<Track> findBySizeBetween(Integer sizeIni, Integer sizeFin);
    
    List<Track> findByCountryOrderBySizeDesc(Country country);
    
    Track findById(Integer id);
    
    Track insert(Track track);
    
    List<Track> listAll();
    
    Track update(Track track);
    
    void delete(Integer id);
}
