package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Track;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
class TrackServiceTest extends BaseTests{
    
    @Autowired
    TrackService service;
    
    @Autowired
    CountryService countryService;
    
    Country country;
    
    @Test
    @DisplayName("Test find track by ID")
    void findByIdTest() {
        var track = service.findById(1);
        assertNotNull(track);
        assertEquals(1, track.getId());
        assertEquals("Interlagos", track.getName()); 
    }
    
    @Test
    @DisplayName("Test find track by non-existent ID")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Track 10 does not exist", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Test insert track")
    @Sql ({"classpath:/resources/sqls/limpa_tabela.sql"})
    void insertTrackTest() {
        Track track = new Track(4, "Lagoinha", 5000, null);
        service.insert(track);
        track = service.findById(1);
        assertEquals("Lagoinha", track.getName());
    }
    
    @Test
    @DisplayName("Test insert null or negative size track")
    void insertTrackNullTest() {
        Track track = new Track(null, "Caixa", -10, null);
        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(track));
        assertEquals("Invalid track size", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Test remove track")
    void removeTrackTest() {
        service.delete(1);
        List<Track> list = service.listAll();
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
        assertEquals(3, list.get(1).getId());
    }
    
    @Test
    @DisplayName("Test remove non-existent track")
    void removeTrackNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Track 10 does not exist", exception.getMessage());
    }
    
    @Test
    @DisplayName("Test list all tracks with records")
    void listAllTrackTest() {
        List<Track> list = service.listAll();
        assertEquals(3, list.size());  
    }
    
    @Test
    @DisplayName("Test list all tracks with no records")
    void listAllNoTrackTest() {
        List<Track> list = service.listAll();
        assertEquals(3, list.size());
        service.delete(1);
        service.delete(2);
        service.delete(3);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("No tracks registered", exception.getMessage());
    }
    
    @Test
    @DisplayName("Test update track")
    void updateTrackTest() {
        var track = service.findById(1);
        assertEquals("Interlagos", track.getName());
        var updatedTrack = new Track(1, "altera", 500, null);
        service.update(updatedTrack);
        track = service.findById(1);
        assertEquals("altera", track.getName());
    }
    
    @Test
    @DisplayName("Test find track starting with")
    void findByTrackStartsWithTest() {
        List<Track> list = service.findByNameStartsWithIgnoreCase("I");
        assertEquals(1, list.size());
        list = service.findByNameStartsWithIgnoreCase("Junipero");
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("z"));
        assertEquals("No tracks found with this name", exception.getMessage());
    }
    
    @Test
    @DisplayName("Test find tracks by size by country in order")
    void findByCountryOrderBySizeTest() {
        List<Track> list = service.findByCountryOrderBySizeDesc(countryService.findById(1));
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Test find tracks by size by country in order (no track)")
    @Sql ({"classpath:/resources/sqls/limpa_tabela.sql"})
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByCountryOrderEmptyTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByCountryOrderBySizeDesc(countryService.findById(1)));
        assertEquals("No tracks registered in the country: Brasil", exception.getMessage());
    }
    
    @Test
    @DisplayName("Test find tracks by size between")
    void findBySizeBetweenTest() {
        List<Track> tracks = service.findBySizeBetween(2000, 8000);
        assertEquals(2, tracks.size());
        assertEquals("Interlagos", tracks.get(0).getName());
        assertEquals(5000, tracks.get(0).getSize());
        assertEquals("Madero", tracks.get(1).getName());
        assertEquals(7500, tracks.get(1).getSize());
    }
    
    @Test
    @DisplayName("Test find tracks by invalid size range")
    void findBySizeBetweenInvalidTest() {
        Integer sizeIni = 10000;
        Integer sizeFin = 12000;
        assertThrows(ObjectNotFound.class, () -> service.findBySizeBetween(sizeIni, sizeFin));
    }
}
