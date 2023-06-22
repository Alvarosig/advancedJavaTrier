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
import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
class CampeonatoServiceTest extends BaseTests {

	@Autowired
	CampeonatoService campeonatoService;

	@Test
	@DisplayName("Teste buscar campeonato por ID")
	
	void findByIdTest() {
		var campeonato = campeonatoService.findById(1);
		assertNotNull(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals("Campeonato Veneza", campeonato.getChampDesc());
	}

	@Test
	@DisplayName("Teste buscar campeonato por ID inexistente")
	void findByIdNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findById(10));
        assertEquals("O campeonato 10 não existe", exception.getMessage()); 
	}

	@Test
	@DisplayName("Teste inserir campeonato")
	@Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
	void insertCampeonatoTest() {
		Campeonato campeonato = new Campeonato(null, "insert", 2005);
		campeonatoService.insert(campeonato);
		assertEquals(1, campeonatoService.listAll().size());
		assertEquals(1, campeonato.getId());
		assertEquals("insert", campeonato.getChampDesc());
		assertEquals(2005, campeonato.getYear());
	}
	
	@Test
    @DisplayName("Teste inserir campeonato com ano menor que 1990")
    void insertCampeonatoLessThanTest() {
        Campeonato campeonato = new Campeonato (null, "insert", 1989);
        var exception = assertThrows(IntegrityViolation.class, () -> campeonatoService.insert(campeonato));
        assertEquals("Ano inválido", exception.getMessage()); 
    }
	
	@Test
	@DisplayName("Teste Remover campeonato")
	void removeCampeonatoTest() {
		campeonatoService.delete(1);
		List<Campeonato> lista = campeonatoService.listAll();
		assertEquals(2, lista.size());
		assertEquals(2, lista.get(0).getId());
		assertEquals(3, lista.get(1).getId());
	}

	@Test
	@DisplayName("Teste remover campeonato inexistente")
	void removeCampeonatoNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.delete(10));
        assertEquals("O campeonato 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	void listAllCampeonatoTest() {
		List<Campeonato> lista = campeonatoService.listAll();
		assertEquals(3, lista.size());
	}
	
	@Test
    @DisplayName("Teste listar todos sem nenhum cadastro")
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
	void findByCampeonatoStartsWithTest() {
		List<Campeonato> lista = campeonatoService.findByChampDescStartingWithIgnoreCase("b");
		assertEquals(1, lista.size());
		lista = campeonatoService.findByChampDescStartingWithIgnoreCase("f1");
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByChampDescStartingWithIgnoreCase("z"));
        assertEquals("Nenhum nome de campeonato inicia com z", exception.getMessage());
	}
	
	@Test
    @DisplayName("Teste buscar por ano de campeonato")
    void findByCampeonatoYearTest() {
        assertEquals(1, campeonatoService.findByYear(2012).size());
    }
	
	@Test
    @DisplayName("Teste buscar por ano de campeonato nulo")
    void findByCampeonatoYearNullTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByYear(1500));
        assertEquals("Nenhum campeonato encontrado em 1500", exception.getMessage());
    }
	
	@Test
	@DisplayName("Teste buscar por campeonato entre anos")
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
    void findByCampeonatoYearBetweenNoExistTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByYearBetweenOrderByYearAsc(2000, 2002));
        assertEquals("Nenhum campeonato encontrado entre 2000 e 2002", exception.getMessage());
    }

	@Test
	@DisplayName("Insert novo campeonato com ano nulo")
	void insertNullYearTest() {	
	    Campeonato campeonato = new Campeonato(null, "Campeonato", null);
	    var exception = assertThrows(IntegrityViolation.class, () -> campeonatoService.insert(campeonato));
        assertEquals("Ano não pode ser nulo", exception.getMessage());
	}
}
