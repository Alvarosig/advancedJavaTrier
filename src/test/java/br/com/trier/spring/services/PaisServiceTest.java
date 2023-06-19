package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class PaisServiceTest extends BaseTests {

	@Autowired
	PaisService paisService;

	@Test
	@DisplayName("Teste buscar país por ID")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByIdTest() {
		var pais = paisService.findById(1);
		assertNotNull(pais);
		assertEquals(1, pais.getId());
		assertEquals("Brasil", pais.getNameCountry());
	}

	@Test
	@DisplayName("Teste buscar país por ID inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByIdNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> paisService.findById(10));
        assertEquals("O país 10 não existe", exception.getMessage());     
	}

	@Test
	@DisplayName("Teste inserir país")
	void insertPaisTest() {
		Pais pais = new Pais(null, "insert");
		paisService.insert(pais);
		pais = paisService.findById(1);
		assertEquals(1, pais.getId());
		assertEquals("insert", pais.getNameCountry());
	}

	@Test
	@DisplayName("Teste Remover país")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void removePaisTest() {
		paisService.delete(1);
		List<Pais> lista = paisService.listAll();
		assertEquals(2, lista.size());
		assertEquals(2, lista.get(0).getId());
		assertEquals(3, lista.get(1).getId());
	}

	@Test
	@DisplayName("Teste remover país inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void removeUserNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> paisService.delete(10));
        assertEquals("O país 10 não existe", exception.getMessage());   
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void listAllPaisTest() {
		List<Pais> lista = paisService.listAll();
		assertEquals(3, lista.size());
	}

	@Test
	@DisplayName("Teste alterar país")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updatePaisTest() {
		var pais = paisService.findById(1);
		assertEquals("Brasil", pais.getNameCountry());
		var paisAltera = new Pais(1, "altera");
		paisService.update(paisAltera);
		pais = paisService.findById(1);
		assertEquals("altera", pais.getNameCountry());
	}

	@Test
	@DisplayName("Teste buscar por pais que inicia com")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByPaisStartsWithTest() {
		List <Pais> lista = paisService.findByNameCountryStartingWithIgnoreCase("brasil");
		assertEquals(1, lista.size());
		lista = paisService.findByNameCountryStartingWithIgnoreCase("n");
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> paisService.findByNameCountryStartingWithIgnoreCase("z"));
		assertEquals("Nenhum nome de país inicia com z", exception.getMessage());   
	}
}
