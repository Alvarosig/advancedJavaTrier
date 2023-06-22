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
import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabela.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
public class PilotoServiceTest extends BaseTests{

	@Autowired
	PilotoService service;
	
	Pais pais;
	Equipe equipe;
	
	@BeforeEach
	void init() {
		pais = new Pais(1, "Brasil");
		equipe = new Equipe(1, "Hotwheels");
	}
	
	@Test
    @DisplayName("Teste buscar piloto por ID")
    void findByIdTest() {
        var piloto = service.findById(1);
        assertNotNull(piloto);
        assertEquals(1, piloto.getId());
        assertEquals("Alvaro", piloto.getName());

    }
    
    @Test
    @DisplayName("Teste buscar piloto por ID inexistente")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Piloto 10 não existe", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Teste inserir piloto")
    void insertPilotoTest() {
        Piloto piloto = new Piloto(1, "Piloto insert", equipe, pais);
        service.insert(piloto);
        assertEquals(2, service.listAll().size());
		assertEquals(1, piloto.getId());
        assertEquals("Piloto insert", piloto.getName());
    }
    
//    @Test
//    @DisplayName("Teste inserir piloto nulo ou menor que zero")
//    void insertPistaNullTest() {
//        Piloto piloto = new Piloto(null, "Piloto", null , null);
//        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(piloto));
//        assertEquals("Cadastro de piloto inválido", exception.getMessage()); 
//    }
    
    @Test
    @DisplayName("Teste Remover piloto")
    void removePilotoTest() {
        service.delete(1);
        List<Piloto> lista = service.listAll();
        assertEquals(1, lista.size());
        assertEquals(2, lista.get(0).getId());
    }
    
    @Test
    @DisplayName("Teste remover piloto inexistente")
    void removePilotoNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Piloto 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todos pilotos com cadastro")
    void listAllPilotosTest() {
        List<Piloto> lista = service.listAll();
        assertEquals(2, lista.size());  
    }
    
    @Test
    @DisplayName("Teste listar todos sem nenhum cadastro")
    void listAllNoPilotoTest() {
        List<Piloto> lista = service.listAll();
        assertEquals(2, lista.size());
        service.delete(1);
        service.delete(2);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar piloto")
    void updatePilotoTest() {
        var piloto = service.findById(1);
        assertEquals("Alvaro", piloto.getName());
        var pilotoAltera = new Piloto(1, "altera", equipe, pais);
        service.update(pilotoAltera);
        piloto = service.findById(1);
        assertEquals("altera", piloto.getName());
    }
    
    @Test
    @DisplayName("Teste buscar por piloto que inicia com")
    void findByPilotoStartsWithTest() {
        List<Piloto> lista = service.findByNameStartsWithIgnoreCase("A");
        assertEquals(1, lista.size());
        lista = service.findByNameStartsWithIgnoreCase("Piloto");
        assertEquals(1, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("z"));
        assertEquals("Nenhum piloto cadastrado com esse nome", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar por país do piloto")
    void findByPaisOrderByNameDesc () {
    	Pais paisT = new Pais (4, "Testes");
    	List<Piloto> lista = service.findByPaisOrderByNameDesc(pais);
    	assertEquals(1, lista.size());
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByPaisOrderByNameDesc(paisT));
        assertEquals("Nenhum piloto cadastrado no país: Testes", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar por equipe do piloto")
    void findByEquipeOrderByNameDesc () {
    	Equipe equipeT = new Equipe (4, "Testes");
    	List<Piloto> lista = service.findByEquipeOrderByNameDesc(equipe);
    	assertEquals(1, lista.size());
    	var exception = assertThrows(ObjectNotFound.class, () -> service.findByEquipeOrderByNameDesc(equipeT));
        assertEquals("Nenhum piloto cadastrado na equipe: Testes", exception.getMessage());
    }
}
