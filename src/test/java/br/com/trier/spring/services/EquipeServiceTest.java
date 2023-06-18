package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Equipe;
import jakarta.transaction.Transactional;

@Transactional
class EquipeServiceTest extends BaseTests {

	@Autowired
	EquipeService equipeService;
	
	@Test
	@DisplayName("Teste buscar equipe por ID")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findByIdTest() {
		var equipe = equipeService.findById(1);
		assertNotNull(equipe);
		assertEquals(1, equipe.getId());
		assertEquals("Hotwheels", equipe.getTeamName());
	}
	
	@Test
	@DisplayName("Teste buscar equipe por ID inexistente")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findByIdNonExistsTest() {
		var equipe = equipeService.findById(10);
		assertNull(equipe);
	}
	
	@Test
	@DisplayName("Teste inserir equipe")
	void insertPaisTest() {
		Equipe equipe = new Equipe (null, "insert");
		equipeService.insert(equipe);
		equipe = equipeService.findById(1);
		assertEquals(1, equipe.getId());
		assertEquals("insert", equipe.getTeamName());
	}
	
	@Test
	@DisplayName("Teste Remover equipe")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void removeEquipeTest() {
		equipeService.delete(1);
		List<Equipe> lista = equipeService.listAll();
		assertEquals(2, lista.size());
		assertEquals(2, lista.get(0).getId());
		assertEquals(3, lista.get(1).getId());
	}
	
	@Test
	@DisplayName("Teste remover equipe inexistente")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void removeEquipeNonExistsTest() {
		equipeService.delete(10);
		List<Equipe> lista = equipeService.listAll();
		assertEquals(3, lista.size());
		assertEquals(2, lista.get(0).getId());
	}
	
	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void listAllEquipeTest() {
		List<Equipe> lista = equipeService.listAll();
		assertEquals(3, lista.size());
	}
	
	@Test
	@DisplayName("Teste alterar equipe")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void updateEquipeTest() {
		var equipe = equipeService.findById(1);
		assertEquals("Hotwheels", equipe.getTeamName());
		var equipeAltera = new Equipe(1, "altera");
		equipeService.update(equipeAltera);
		equipe = equipeService.findById(1);
		assertEquals("altera", equipe.getTeamName());
	}

	@Test
	@DisplayName("Teste buscar por equipe que inicia com")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findByEquipeStartsWithTest() {
		List<Equipe> lista = equipeService.findByTeamNameStartingWithIgnoreCase("b");
		assertEquals(1, lista.size());
		lista = equipeService.findByTeamNameStartingWithIgnoreCase("ferrari");
		assertEquals(1, lista.size());
		lista = equipeService.findByTeamNameStartingWithIgnoreCase("h");
		assertEquals(1, lista.size());
	}
}
