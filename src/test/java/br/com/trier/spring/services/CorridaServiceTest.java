package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Pista;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import br.com.trier.spring.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
public class CorridaServiceTest extends BaseTests {
    
    @Autowired
    CorridaService service;
    
    @Autowired
    CampeonatoService campeonatoService;
    
    @Autowired
    PistaService pistaService;
    
    Pista pista;
    Campeonato campeonato;
    
    @BeforeEach
    void setup () {
      //  pista = new Pista(1, "Interlagos", 5000, null);
      //  campeonato = new Campeonato (1, "Campeonato Veneza", 2015);
    }
    
    @Test
    @DisplayName("Teste buscar corrida por ID")
    void findByIdTest() {
        var corrida = service.findById(1);
        assertNotNull(corrida);
        assertEquals(1, corrida.getId());
    }
    
    @Test
    @DisplayName("Teste buscar piloto por ID inexistente")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Corrida 10 não existe", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Teste inserir corrida")
    void insertPilotoTest() {
        String dataStr = "03/12/2018";
        ZonedDateTime data = DateUtils.strToZoneDateTime(dataStr);
        Corrida corrida = new Corrida(1, data, pista, campeonato);
        service.insert(corrida);
        assertEquals(2, service.listAll().size());
        assertEquals(1, corrida.getId());
        assertEquals(data, corrida.getDate());
    }
    
    @Test
    @DisplayName("Teste Remover corrida")
    void removeCorridaTest() {
        service.delete(1);
        List<Corrida> lista = service.listAll();
        assertEquals(1, lista.size());
        assertEquals(2, lista.get(0).getId());
    }
    
    @Test
    @DisplayName("Teste remover corrida inexistente")
    void removeCorridaNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Corrida 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todas corridas cadastradas")
    void listAllCorridaTest() {
        List<Corrida> lista = service.listAll();
        assertEquals(2, lista.size());  
    }
    
    @Test
    @DisplayName("Teste listar todos sem nenhum cadastro")
    void listAllNoCorridaTest() {
        List<Corrida> lista = service.listAll();
        assertEquals(2, lista.size());
        service.delete(1);
        service.delete(2);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhuma corrida cadastrada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar corrida")
    void updateCorridaTest() {
    	Corrida corrida = new Corrida(1, DateUtils.strToZoneDateTime("03/12/2018"), pistaService.findById(2), campeonatoService.findById(1));
		service.update(corrida);
		assertEquals(2, service.listAll().size());
		assertEquals(1, corrida.getId());
		assertEquals(2018, corrida.getDate().getYear());
    }
    
    @Test
    @DisplayName("Teste buscar por ano da corrida")
    void findByDateCorridaTest() {
    	List<Corrida> lista = service.findByDate(DateUtils.strToZoneDateTime("03/12/2018"));
    	assertEquals(1, lista.size());
    }
    
    @Test
    @DisplayName("Teste buscar por ano inexistente da corrida")
    void findByDateCorridaNoExistTest() {
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByDate(DateUtils.strToZoneDateTime("03/10/2010")));
        assertEquals("Nenhuma corrida foi encontrada na data selecionada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar entre anos da corrida")
    void findByDateBetweenCorridaTest() {
    	List<Corrida> lista = service.findByDateBetween(DateUtils.strToZoneDateTime("03/12/2010"), DateUtils.strToZoneDateTime("03/12/2020"));
    	assertEquals(2, lista.size());
    }
    
    @Test
    @DisplayName("Teste buscar entre anos inexistentes da corrida")
    void findByDateBetweenCorridaNoExistTest() {
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByDateBetween(DateUtils.strToZoneDateTime("03/10/2000"), DateUtils.strToZoneDateTime("03/12/2005")));
        assertEquals("Nenhuma corrida foi encontrada entre a data selecionada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar pistas ordenadas da corrida")
    void findByPistaOrderByDateTest () {
    	Pista pista1 = new Pista(1, "Pista 1", 2000, null);
    	pistaService.insert(pista1);   	
    	List<Corrida> lista = service.findByPistaOrderByDate(pista1);
    	assertEquals(1, lista.size());
    }
    
    @Test
    @DisplayName("Teste buscar pistas ordenadas da corrida inexistente")
    void findByPistaOrderByDateInexistTest () {
    	Pista pistaInexistente = new Pista(999, "Pista Inexistente", 5000, null);
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByPistaOrderByDate(pistaInexistente));
        assertEquals("Nenhuma corrida cadastrada na pista: Pista Inexistente", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar campeonatos ordenados da corrida")
    void findByCampeonatoOrderByDateTest () {
    	Campeonato campeonato = new Campeonato(1, "Nevile", 2018);
    	campeonatoService.insert(campeonato);   	
    	List<Corrida> lista = service.findByCampeonatoOrderByDate(campeonato);
    	assertEquals(1, lista.size());
    }
    
    @Test
    @DisplayName("Teste buscar pistas ordenadas da corrida inexistente")
    void findByCampeonatoOrderByDateNoExistTest () {
    	Campeonato campeonatoInexistente = new Campeonato(999, "Campeonato Inexistente", 3000);
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByCampeonatoOrderByDate(campeonatoInexistente));
        assertEquals("Nenhuma corrida cadastrada no campeonato: Campeonato Inexistente", exception.getMessage());
    }
}
