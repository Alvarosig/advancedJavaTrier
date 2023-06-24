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
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.DriverRace;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.Team;
import br.com.trier.spring.models.Track;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import br.com.trier.spring.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto-corrida.sql")
public class DriverRaceServiceTest extends BaseTests{
	
	@Autowired
	DriverRaceService service;

	@Autowired
	DriverService driverService;

	@Autowired
	RaceService raceService;

	@Autowired
	CountryService countryService;
	
	@Autowired
	ChampionshipService championService;

	Driver driver;
	Race race;
	Team team;
	Country country;
	Track track;
	Championship championship;


	@Test
	@DisplayName("Test find driver/race by ID")
	void findByIdTest() {
	    var driverRace = service.findById(1);
	    assertNotNull(driverRace);
	    assertEquals(1, driverRace.getId());
	    assertEquals(1, driverRace.getPlacement()); 
	}

	@Test
	@DisplayName("Test find driver/race by non-existing ID")
	void findByIdNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
	    assertEquals("Race/Driver 10 does not exist", exception.getMessage()); 
	}

	@Test
	@DisplayName("Test insert driver/race")
	void insertDriverRaceTest() {
	    DriverRace driverRace4 = new DriverRace(1, 1, driver, race);
	    service.insert(driverRace4);
	    driverRace4 = service.findById(1);
	    assertEquals(1, driverRace4.getId());
	    assertEquals(1, driverRace4.getPlacement());
	}

	@Test
	@DisplayName("Test remove driver/race")
	void removeDriverRaceTest() {
	    service.delete(1);
	    List<DriverRace> lista = service.listAll();
	    assertEquals(2, lista.size());
	    assertEquals(2, lista.get(0).getId());
	}

	 @Test
	 @DisplayName("Test remove non-existing driver/race")
	 void removeDriverRaceNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
	    assertEquals("Race/Driver 10 does not exist", exception.getMessage());
	}

	 @Test
	 @DisplayName("Test list all drivers/race with registration")
	 void listAllDriversRacesTest() {
	    List<DriverRace> lista = service.listAll();
	    assertEquals(3, lista.size());  
	 }
	    
	 @Test
	 @DisplayName("Test list all with no registrations")
	 void listAllNoDriverTest() {
	    List<DriverRace> lista = service.listAll();
	    assertEquals(3, lista.size());
	    service.delete(1);
	    service.delete(2);
	    service.delete(3);
	    var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
	    assertEquals("No race/driver registered", exception.getMessage());
	 }
	    
	 @Test
	 @DisplayName("Test update driver")
	 void updateDriverRaceTest() {
	    var driverRace = service.findById(1);
	    assertEquals(1, driverRace.getPlacement());
	    var driverRaceAlter = new DriverRace(1, 2, driver, race);
	    service.update(driverRaceAlter);
	    driverRace = service.findById(1);
	    assertEquals(2, driverRace.getPlacement());
	 }
	 
	 @Test
	 @DisplayName("Test search by placement")
	 void findByPlacementTest () {
	     List<DriverRace> lista = service.findByPlacement(1);
	     assertEquals(1, lista.size());
	     lista = service.findByPlacement(2);
	     assertEquals(1, lista.size());
	     var exception = assertThrows(ObjectNotFound.class, () -> service.findByPlacement(5));
	     assertEquals("No driver registered with placement: 5", exception.getMessage());
	 }
	 
	 @Test
	 @DisplayName("Test search by race and order by placement")
	 @Sql ({"classpath:/resources/sqls/limpa_tabela.sql"})
	 void findByRaceOrderByPlacement () {
		 String dateStr = "03/12/2018";
	     ZonedDateTime date = DateUtils.strToZoneDateTime(dateStr);
	     Championship championship = new Championship(null, "CampTest", 2018);
	     championService.insert(championship);
	     Race raceT = new Race(1, date, null, championship);
	     Race raceT2 = new Race(3, null, null, null);	     
	     raceService.insert(raceT);
	     DriverRace dr = new DriverRace(null, 1, driver, raceT);
	     service.insert(dr);
	     List<DriverRace> lista = service.findByRaceOrderByPlacement(raceT);
	     assertEquals(1, lista.size());
	     var exception = assertThrows(ObjectNotFound.class, () -> service.findByRaceOrderByPlacement(raceT2));
	     assertEquals("No driver registered in the race with id: 3", exception.getMessage());
	 }
	 
	 @Test
	 @DisplayName("Test search by driver and order by placement")
	 @Sql({"classpath:/resources/sqls/pais.sql"})
	 @Sql({"classpath:/resources/sqls/equipe.sql"})
	 @Sql({"classpath:/resources/sqls/piloto.sql"})
	 void findByDriverOrderByPlacement () {
	     Country country = new Country(1,"Brazil");
	     countryService.insert(country);
	     assertEquals("Brazil", country.getName());
	     Driver driverT1 = new Driver(1, "Alvaro", team, country);
	     Driver driverT2 = new Driver(3, "B", team, country);
	     driverService.insert(driverT1);
	     DriverRace dr = new DriverRace(null, 1, driverT1, race);
	     service.insert(dr);
	     List<DriverRace> lista = service.findByDriverOrderByPlacement(driverT1);
	     assertEquals(1, lista.size());
	     var exception = assertThrows(ObjectNotFound.class, () -> service.findByDriverOrderByPlacement(driverT2));
	     assertEquals("No driver registered in this position with id: 3", exception.getMessage());
	 }
	 
}