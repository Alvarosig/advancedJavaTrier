package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Track;
import br.com.trier.spring.repositories.TrackRepository;
import br.com.trier.spring.services.TrackService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class TrackServiceImpl implements TrackService {
    
    @Autowired
    private TrackRepository trackRepository;
    
    private void validateTrack(Track track) {
        if (track.getSize() == null || track.getSize() <= 0) {
            throw new IntegrityViolation("Invalid track size");
        }
    }
       
    @Override
    public List<Track> findByNameStartsWithIgnoreCase(String name) {
        List<Track> lista = trackRepository.findByNameStartsWithIgnoreCase(name); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("No tracks found with this name");
        }
        return lista;
    }

    @Override
    public List<Track> findBySizeBetween(Integer sizeIni, Integer sizeFin) {
        List<Track> lista = trackRepository.findBySizeBetween(sizeIni, sizeFin); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("No tracks found within these measurements");
        }
        return lista;
    }

    @Override
    public List<Track> findByCountryOrderBySizeDesc(Country country) {
        List<Track> lista = trackRepository.findByCountryOrderBySizeDesc(country); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("No tracks registered in the country: %s".formatted(country.getName()));
        }
        return lista;
    }

    @Override
    public Track findById(Integer id) {
       return trackRepository.findById(id).orElseThrow(() -> new ObjectNotFound ("Track %s does not exist".formatted(id)));
    }

    @Override
    public Track insert(Track track) {
       validateTrack(track);
       return trackRepository.save(track);
    }

    @Override
    public List<Track> listAll() {
       List<Track> lista = trackRepository.findAll(); 
       if (lista.isEmpty()) {
           throw new ObjectNotFound("No tracks registered");
       }
       return lista;
    }

    @Override
    public Track update(Track track) {
       findById(track.getId());
       validateTrack(track);
       return trackRepository.save(track);
    }

    @Override
    public void delete(Integer id) {
        Track track = findById(id);
        trackRepository.delete(track);
    }

}
