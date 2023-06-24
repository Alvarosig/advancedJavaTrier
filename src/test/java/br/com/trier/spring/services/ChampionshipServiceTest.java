package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Championship;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
class ChampionshipServiceTest extends BaseTests {

	@Autowired
	ChampionshipService championshipService;

	@Test
	@DisplayName("Test find championship by ID")
	void findByIdTest() {
		var championship = championshipService.findById(1);
		assertNotNull(championship);
		assertEquals(1, championship.getId());
		assertEquals("Campeonato Veneza", championship.getChampDesc());
	}

	@Test
	@DisplayName("Test find non-existent championship by ID")
	void findByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.findById(10));
		assertEquals("Championship not found", exception.getMessage());
	}

	@Test
	@DisplayName("Test insert championship")
	@Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
	void insertChampionshipTest() {
		Championship championship = new Championship(null, "insert", 2005);
		championshipService.insert(championship);
		assertEquals(1, championshipService.listAll().size());
		assertEquals(1, championship.getId());
		assertEquals("insert", championship.getChampDesc());
		assertEquals(2005, championship.getYear());
	}

	@Test
	@DisplayName("Test insert championship with year less than 1990")
	void insertChampionshipLessThanTest() {
		Championship championship = new Championship(null, "insert", 1989);
		var exception = assertThrows(IntegrityViolation.class, () -> championshipService.insert(championship));
		assertEquals("Invalid year", exception.getMessage());
	}

	@Test
	@DisplayName("Test remove championship")
	void removeChampionshipTest() {
		championshipService.delete(1);
		List<Championship> list = championshipService.listAll();
		assertEquals(2, list.size());
		assertEquals(2, list.get(0).getId());
		assertEquals(3, list.get(1).getId());
	}

	@Test
	@DisplayName("Test remove non-existent championship")
	void removeChampionshipNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.delete(10));
		assertEquals("Championship not found", exception.getMessage());
	}

	@Test
	@DisplayName("Test list all championships")
	void listAllChampionshipTest() {
		List<Championship> list = championshipService.listAll();
		assertEquals(3, list.size());
	}

	@Test
	@DisplayName("Test list all without any championship")
	void listAllNoChampionshipTest() {
		List<Championship> list = championshipService.listAll();
		assertEquals(3, list.size());
		championshipService.delete(1);
		championshipService.delete(2);
		championshipService.delete(3);
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.listAll());
		assertEquals("No championships found", exception.getMessage());
	}

	@Test
	@DisplayName("Test update championship")
	void updateChampionshipTest() {
		var championship = championshipService.findById(1);
		assertEquals("Campeonato Veneza", championship.getChampDesc());
		var updatedChampionship = new Championship(1, "update", 2010);
		championshipService.update(updatedChampionship);
		championship = championshipService.findById(1);
		assertEquals("update", championship.getChampDesc());
	}

	@Test
	@DisplayName("Test find championships that start with")
	void findByChampionshipStartsWithTest() {
		List<Championship> list = championshipService.findByChampDescStartingWithIgnoreCase("b");
		assertEquals(1, list.size());
		list = championshipService.findByChampDescStartingWithIgnoreCase("f1");
		assertEquals(1, list.size());
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.findByChampDescStartingWithIgnoreCase("z"));
		assertEquals("No championships found", exception.getMessage());
	}

	@Test
	@DisplayName("Test find championships by year")
	void findByChampionshipYearTest() {
		assertEquals(1, championshipService.findByYear(2012).size());
	}

	@Test
	@DisplayName("Test find championships by null year")
	void findByChampionshipYearNullTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.findByYear(1500));
		assertEquals("No championships found", exception.getMessage());
	}

	@Test
	@DisplayName("Test find championships between years")
	void findByChampionshipYearBetweenTest() {
		List<Championship> championships = championshipService.findByYearBetweenOrderByYearAsc(2010, 2015);
		List<Championship> expectedResults = new ArrayList<>();
		expectedResults.add(new Championship(2, "F1 Racers", 2012));
		expectedResults.add(new Championship(1, "Campeonato Veneza", 2015));
		assertEquals(expectedResults.size(), championships.size());
		assertArrayEquals(expectedResults.toArray(), championships.toArray());
	}

	@Test
	@DisplayName("Test find championships between non-existent years")
	void findByChampionshipYearBetweenNoExistTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.findByYearBetweenOrderByYearAsc(2000, 2002));
		assertEquals("No championships found", exception.getMessage());
	}

	@Test
	@DisplayName("Insert new championship with null year")
	void insertNullYearTest() {	
		Championship championship = new Championship(null, "Championship", null);
		var exception = assertThrows(IntegrityViolation.class, () -> championshipService.insert(championship));
		assertEquals("Year cannot be null", exception.getMessage());
	}
}
