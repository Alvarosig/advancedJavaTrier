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
    
    Pista pista;
    Campeonato campeonato;
    
    @BeforeEach
    void setup () {
        pista = new Pista(1, "Interlagos", 5000, null);
        campeonato = new Campeonato (1, "Campeonato Veneza", 2015);
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
    
//    @Test
//    @DisplayName("Teste inserir piloto nulo")
//    void insertNullPilotoTest() {
//        Piloto piloto = new Piloto(1, null, equipe , null);
//        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(piloto));
//        assertEquals("Piloto nulo", exception.getMessage()); 
//    }
    
//    @Test
//    @DisplayName("Teste inserir piloto já cadastrado")
//    void insertSamePilotoTest() {
//        Piloto existingPiloto = new Piloto(2, "Alvaro", equipe, pais);
//        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(existingPiloto));
//        assertEquals("Piloto já cadastrado", exception.getMessage()); 
//    }
    
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
        String dataStr = "03/12/2018";
        ZonedDateTime data = DateUtils.strToZoneDateTime(dataStr);
        Pista pista1 = new Pista(1, "altera", 5000, null);
        var corrida = service.findById(1);
        assertEquals("Interlagos", corrida.getPista().getName());
        var corridaAltera = new Corrida(1, data, pista1, campeonato);
        service.update(corridaAltera);
        corrida = service.findById(1);
        assertEquals("altera", corrida.getPista().getName()); // falta arrumar
    }
}
