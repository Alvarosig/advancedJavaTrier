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

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class CampeonatoServiceTest extends BaseTests {

	@Autowired
	CampeonatoService campeonatoService;

	@Test
	@DisplayName("Teste buscar campeonato por ID")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByIdTest() {
		var campeonato = campeonatoService.findById(1);
		assertNotNull(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals("Campeonato Veneza", campeonato.getChampDesc());
	}

	@Test
	@DisplayName("Teste buscar campeonato por ID inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByIdNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findById(10));
        assertEquals("O campeonato 10 não existe", exception.getMessage()); 
	}

	@Test
	@DisplayName("Teste inserir campeonato")
	void insertCampeonatoTest() {
		Campeonato campeonato = new Campeonato(null, "insert", 2005);
		campeonatoService.insert(campeonato);
		campeonato = campeonatoService.findById(1);
		assertEquals(1, campeonato.getId());
		assertEquals("insert", campeonato.getChampDesc());
		assertEquals(2005, campeonato.getYear());
	}

	@Test
    @DisplayName("Teste inserir campeonato já existente")
    void insertCampeonatoExistTest() {
	    Campeonato campeonato = new Campeonato (null, "insert", 1);
        Campeonato campeonato2 = new Campeonato (null, "insert", 1);
        campeonatoService.insert(campeonato);
        assertEquals(2, campeonato.getId());
        assertEquals("insert", campeonato.getChampDesc());
        var exception = assertThrows(IntegrityViolation.class, () -> campeonatoService.insert(campeonato2));
        assertEquals("Campeonato já cadastrado", exception.getMessage()); 
    }
	
	@Test
	@DisplayName("Teste Remover campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void removeCampeonatoTest() {
		campeonatoService.delete(1);
		List<Campeonato> lista = campeonatoService.listAll();
		assertEquals(2, lista.size());
		assertEquals(2, lista.get(0).getId());
		assertEquals(3, lista.get(1).getId());
	}

	@Test
	@DisplayName("Teste remover campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void removeCampeonatoNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.delete(10));
        assertEquals("O campeonato 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void listAllCampeonatoTest() {
		List<Campeonato> lista = campeonatoService.listAll();
		assertEquals(3, lista.size());
	}
	
	@Test
    @DisplayName("Teste listar todos sem nenhum cadastro")
    @Sql({ "classpath:/resources/sqls/campeonato.sql" })
    void listAllNoCampeonatoTest() {
        List<Campeonato> lista = campeonatoService.listAll();
        assertEquals(3, lista.size());
        campeonatoService.delete(1);
        campeonatoService.delete(2);
        campeonatoService.delete(3);
        var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.listAll());
        assertEquals("Nenhum campeonato cadastrado", exception.getMessage());
    }
	
	@Test
	@DisplayName("Teste alterar campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void updateCampeonatoTest() {
		var campeonato = campeonatoService.findById(1);
		assertEquals("Campeonato Veneza", campeonato.getChampDesc());
		var campeonatoAltera = new Campeonato(1, "altera", 2010);
		campeonatoService.update(campeonatoAltera);
		campeonato = campeonatoService.findById(1);
		assertEquals("altera", campeonato.getChampDesc());
	}

	@Test
	@DisplayName("Teste buscar por campeonato que inicia com")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByCampeonatoStartsWithTest() {
		List<Campeonato> lista = campeonatoService.findByChampDescStartingWithIgnoreCase("b");
		assertEquals(1, lista.size());
		lista = campeonatoService.findByChampDescStartingWithIgnoreCase("f1");
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByChampDescStartingWithIgnoreCase("z"));
        assertEquals("Nenhum nome de campeonato inicia com z", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar por campeonato entre anos")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByCampeonatoYearBetweenTest() {
		List<Campeonato> campeonatos = campeonatoService.findByYearBetweenOrderByYearAsc(2010, 2015);
		List<Campeonato> resultadoEsperado = new ArrayList<>();
		resultadoEsperado.add(new Campeonato(2, "F1 Racers", 2012));
		resultadoEsperado.add(new Campeonato(1, "Campeonato Veneza", 2015));
		assertEquals(resultadoEsperado.size(), campeonatos.size());
		assertArrayEquals(resultadoEsperado.toArray(), campeonatos.toArray());
	}
	
	@Test
    @DisplayName("Teste buscar por campeonato entre anos inexistentes")
    @Sql({ "classpath:/resources/sqls/campeonato.sql" })
    void findByCampeonatoYearBetweenNoExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByYearBetweenOrderByYearAsc(2000, 2002));
        assertEquals("Nenhum campeonato encontrado entre 2000 e 2002", exception.getMessage());
    }

}
