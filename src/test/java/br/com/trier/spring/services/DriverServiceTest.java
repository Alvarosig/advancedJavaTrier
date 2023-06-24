package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.Team;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabela.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
public class DriverServiceTest extends BaseTests {

    @Autowired
    DriverService service;

    Country country;
    Team team;

    @BeforeEach
    void init() {
        country = new Country(1, "Brazil");
        team = new Team(1, "Hotwheels");
    }

    @Test
    @DisplayName("Test find driver by ID")
    void findByIdTest() {
        var driver = service.findById(1);
        assertNotNull(driver);
        assertEquals(1, driver.getId());
        assertEquals("Alvaro", driver.getName());
    }

    @Test
    @DisplayName("Test find driver by non-existent ID")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Driver 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test insert driver")
    void insertDriverTest() {
        Driver driver = new Driver(1, "Driver insert", team, country);
        service.insert(driver);
        assertEquals(2, service.listAll().size());
        assertEquals(1, driver.getId());
        assertEquals("Driver insert", driver.getName());
    }

    @Test
    @DisplayName("Test insert null driver")
    void insertNullDriverTest() {
        Driver driver = new Driver(1, null, team , null);
        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(driver));
        assertEquals("Null driver", exception.getMessage());
    }

    @Test
    @DisplayName("Test insert duplicate driver")
    void insertSameDriverTest() {
        Driver existingDriver = new Driver(2, "Alvaro", team, country);
        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(existingDriver));
        assertEquals("Driver already registered", exception.getMessage());
    }

    @Test
    @DisplayName("Test remove driver")
    void removeDriverTest() {
        service.delete(1);
        List<Driver> list = service.listAll();
        assertEquals(1, list.size());
        assertEquals(2, list.get(0).getId());
    }

    @Test
    @DisplayName("Test remove non-existent driver")
    void removeNonExistsDriverTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Driver 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test list all drivers with registrations")
    void listAllDriversTest() {
        List<Driver> list = service.listAll();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Test list all drivers with no registrations")
    void listAllNoDriverTest() {
        List<Driver> list = service.listAll();
        assertEquals(2, list.size());
        service.delete(1);
        service.delete(2);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("No drivers registered", exception.getMessage());
    }

    @Test
    @DisplayName("Test update driver")
    void updateDriverTest() {
        var driver = service.findById(1);
        assertEquals("Alvaro", driver.getName());
        var modifiedDriver = new Driver(1, "modified", team, country);
        service.update(modifiedDriver);
        driver = service.findById(1);
        assertEquals("modified", driver.getName());
    }

    @Test
    @DisplayName("Test find drivers starting with")
    void findByDriverStartsWithTest() {
        List<Driver> list = service.findByNameStartsWithIgnoreCase("A");
        assertEquals(1, list.size());
        list = service.findByNameStartsWithIgnoreCase("Piloto");
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("z"));
        assertEquals("No drivers registered with this name", exception.getMessage());
    }

    @Test
    @DisplayName("Test find drivers by country")
    void findByCountryOrderByNameDesc() {
        Country countryT = new Country(4, "Tests");
        List<Driver> list = service.findByCountryOrderByNameDesc(country);
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByCountryOrderByNameDesc(countryT));
        assertEquals("No drivers registered in the country: Tests", exception.getMessage());
    }

    @Test
    @DisplayName("Test find drivers by team")
    void findByTeamOrderByNameDesc() {
        Team teamT = new Team(4, "Tests");
        List<Driver> list = service.findByTeamOrderByNameDesc(team);
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByTeamOrderByNameDesc(teamT));
        assertEquals("No drivers registered in the team: Tests", exception.getMessage());
    }
}
