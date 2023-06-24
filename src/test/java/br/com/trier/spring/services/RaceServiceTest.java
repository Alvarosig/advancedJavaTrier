package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Championship;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.Track;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import br.com.trier.spring.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
public class RaceServiceTest extends BaseTests {

    @Autowired
    RaceService service;

    @Autowired
    ChampionshipService championshipService;

    @Autowired
    TrackService trackService;

    Track track;
    Championship championship;

    @Test
    @DisplayName("Test find race by ID")
    void findByIdTest() {
        var race = service.findById(1);
        assertNotNull(race);
        assertEquals(1, race.getId());
    }

    @Test
    @DisplayName("Test find race by non-existent ID")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Race with ID 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test insert race")
    void insertRaceTest() {
        String dateStr = "03/12/2018";
        ZonedDateTime date = DateUtils.strToZoneDateTime(dateStr);
        Race race = new Race(1, date, trackService.findById(1), championshipService.findById(3));
        service.insert(race);
        assertEquals(2, service.listAll().size());
        assertEquals(1, race.getId());
        assertEquals(date, race.getDate());
    }
    
    @Test
    @DisplayName("Test insert invalid date race")
    void insertInvalidDateRaceTest() {
        String dateStr = "03/12/2019";
        ZonedDateTime date = DateUtils.strToZoneDateTime(dateStr);
        Race race = new Race(1, date, trackService.findById(1), championshipService.findById(3));
        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(race));
        assertEquals("Race year: 2019 must be the same as the championship year: 2018", exception.getMessage());
    }

    @Test
    @DisplayName("Test remove race")
    void removeRaceTest() {
        service.delete(1);
        List<Race> list = service.listAll();
        assertEquals(1, list.size());
        assertEquals(2, list.get(0).getId());
    }

    @Test
    @DisplayName("Test remove non-existent race")
    void removeRaceNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Race with ID 10 does not exist", exception.getMessage());
    }
    
    @Test
    @DisplayName("Test list all registered races")
    void listAllRaceTest() {
        List<Race> list = service.listAll();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Test list all with no races")
    void listAllNoRaceTest() {
        List<Race> list = service.listAll();
        assertEquals(2, list.size());
        service.delete(1);
        service.delete(2);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("No races registered", exception.getMessage());
    }

    @Test
    @DisplayName("Test update race")
    void updateRaceTest() {
        Race race = new Race(1, DateUtils.strToZoneDateTime("03/12/2015"), trackService.findById(2), championshipService.findById(1));
        service.update(race);
        assertEquals(2, service.listAll().size());
        assertEquals(1, race.getId());
        assertEquals(2015, race.getDate().getYear());
    }

    @Test
    @DisplayName("Test find races by date")
    void findByDateRaceTest() {
        List<Race> list = service.findByDate(DateUtils.strToZoneDateTime("03/12/2018"));
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test find races by non-existent date")
    void findByDateNoExistRaceTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByDate(DateUtils.strToZoneDateTime("03/10/2010")));
        assertEquals("No races found on the selected date", exception.getMessage());
    }

    @Test
    @DisplayName("Test find races between dates")
    void findByDateBetweenRaceTest() {
        List<Race> list = service.findByDateBetween(DateUtils.strToZoneDateTime("03/12/2010"), DateUtils.strToZoneDateTime("03/12/2020"));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Test find races between non-existent dates")
    void findByDateBetweenNoExistRaceTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByDateBetween(DateUtils.strToZoneDateTime("03/10/2000"), DateUtils.strToZoneDateTime("03/12/2005")));
        assertEquals("No races found between the selected dates", exception.getMessage());
    }

    @Test
    @DisplayName("Test find races sorted by track")
    void findByTrackOrderByDateTest() {
        Track track1 = new Track(1, "Track 1", 2000, null);
        trackService.insert(track1);
        List<Race> list = service.findByTrackOrderByDate(track1);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test find races sorted by non-existent track")
    void findByTrackOrderByDateNoExistTest() {
        Track nonExistentTrack = new Track(999, "Non-existent Track", 5000, null);
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByTrackOrderByDate(nonExistentTrack));
        assertEquals("No races registered on the track: Non-existent Track", exception.getMessage());
    }

    @Test
    @DisplayName("Test find races sorted by championship")
    void findByChampionshipOrderByDateTest() {
    	List<Race> list = service.findByChampionshipOrderByDate(championshipService.findById(2));
		assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test find races sorted by non-existent championship")
    void findByChampionshipOrderByDateNoExistTest() {
        Championship nonExistentChampionship = new Championship(999, "Non-existent Championship", 3000);
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByChampionshipOrderByDate(nonExistentChampionship));
        assertEquals("No races registered in the championship: Non-existent Championship", exception.getMessage());
    }
    
}
