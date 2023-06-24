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
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
class CountryServiceTest extends BaseTests {

    @Autowired
    CountryService countryService;

    @Test
    @DisplayName("Test find country by ID")
    void findByIdTest() {
        var country = countryService.findById(1);
        assertNotNull(country);
        assertEquals(1, country.getId());
        assertEquals("Brasil", country.getName());
    }

    @Test
    @DisplayName("Test find country by non-existent ID")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.findById(10));
        assertEquals("Country 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test insert country")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    void insertCountryTest() {
        Country country = new Country(3, "insert");
        countryService.insert(country);
        assertEquals(3, country.getId());
        assertEquals("insert", country.getName());
    }

    @Test
    @DisplayName("Test insert existing country")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    void insertCountryExistTest() {
        Country country1 = new Country(null, "insert");
        Country country2 = new Country(null, "insert");
        countryService.insert(country1);
        assertEquals(1, country1.getId());
        assertEquals("insert", country1.getName());
        var exception = assertThrows(IntegrityViolation.class, () -> countryService.insert(country2));
        assertEquals("Country already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test remove country")
    void removeCountryTest() {
        countryService.delete(1);
        List<Country> list = countryService.listAll();
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
        assertEquals(3, list.get(1).getId());
    }

    @Test
    @DisplayName("Test remove non-existent country")
    void removeCountryNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.delete(10));
        assertEquals("Country 10 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test list all countries")
    void listAllCountryTest() {
        List<Country> list = countryService.listAll();
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Test list all with no countries")
    void listAllNoCountryTest() {
        List<Country> list = countryService.listAll();
        assertEquals(3, list.size());
        countryService.delete(1);
        countryService.delete(2);
        countryService.delete(3);
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.listAll());
        assertEquals("No countries registered", exception.getMessage());
    }

    @Test
    @DisplayName("Test update country")
    void updateCountryTest() {
        var country = countryService.findById(1);
        assertEquals("Brasil", country.getName());
        var modifiedCountry = new Country(1, "modified");
        countryService.update(modifiedCountry);
        country = countryService.findById(1);
        assertEquals("modified", country.getName());
    }

    @Test
    @DisplayName("Test find countries starting with")
    void findByCountryStartsWithTest() {
        List<Country> list = countryService.findByNameStartingWithIgnoreCase("brasil");
        assertEquals(1, list.size());
        list = countryService.findByNameStartingWithIgnoreCase("n");
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class, () -> countryService.findByNameStartingWithIgnoreCase("z"));
        assertEquals("No country names start with z", exception.getMessage());
    }
}
