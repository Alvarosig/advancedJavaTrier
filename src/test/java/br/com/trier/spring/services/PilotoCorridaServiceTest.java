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
import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Equipe;
import br.com.trier.spring.models.Pais;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.models.PilotoCorrida;
import br.com.trier.spring.models.Pista;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto-corrida.sql")
public class PilotoCorridaServiceTest extends BaseTests{
	
	@Autowired
	PilotoCorridaService service;
	
	@Autowired
	PilotoService pilotoService;
	
	@Autowired
	CorridaService corridaService;
	
	@Autowired
	PaisService paisService;
	
	Piloto piloto;
	Corrida corrida;
	Equipe equipe;
	Pais pais;
	Pista pista;
	Campeonato campeonato;
	

	@Test
    @DisplayName("Teste buscar piloto/corrida por ID")
    void findByIdTest() {
        var pistaCorrida = service.findById(1);
        assertNotNull(pistaCorrida);
        assertEquals(1, pistaCorrida.getId());
        assertEquals(1, pistaCorrida.getPlacement()); 
    }
	
	@Test
    @DisplayName("Teste buscar piloto/corrida por ID inexistente")
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Piloto/Corrida 10 não existe", exception.getMessage()); 
    }
	
	@Test
    @DisplayName("Teste inserir piloto/corrida")
    void insertPilotoCorridaTest() {
        PilotoCorrida pilotoCorrida4 = new PilotoCorrida(1, 1, piloto, corrida);
        service.insert(pilotoCorrida4);
        pilotoCorrida4 = service.findById(1);
        assertEquals(1, pilotoCorrida4.getId());
        assertEquals(1, pilotoCorrida4.getPlacement());
    }
	
	@Test
    @DisplayName("Teste Remover piloto/corrida")
    void removePilotoCorridaTest() {
        service.delete(1);
        List<PilotoCorrida> lista = service.listAll();
        assertEquals(2, lista.size());
        assertEquals(2, lista.get(0).getId());
    }
	
	 @Test
	 @DisplayName("Teste remover piloto/corrida inexistente")
	 void removePilotoCorridaNonExistsTest() {
	    var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
	    assertEquals("Piloto/Corrida 10 não existe", exception.getMessage());
	}
	
	 @Test
	 @DisplayName("Teste listar todos pilotos/corrida com cadastro")
	 void listAllPilotosCorridasTest() {
	    List<PilotoCorrida> lista = service.listAll();
	    assertEquals(3, lista.size());  
	 }
	    
	 @Test
	 @DisplayName("Teste listar todos sem nenhum cadastro")
	 void listAllNoPilotoTest() {
	    List<PilotoCorrida> lista = service.listAll();
	    assertEquals(3, lista.size());
	    service.delete(1);
	    service.delete(2);
	    service.delete(3);
	    var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
	    assertEquals("Nenhum piloto/corrida cadastrado", exception.getMessage());
	 }
	    
	 @Test
	 @DisplayName("Teste alterar piloto")
	 void updatePilotoCorridaTest() {
	    var pilotoCorrida = service.findById(1);
	    assertEquals(1, pilotoCorrida.getPlacement());
	    var pilotoCorridaAltera = new PilotoCorrida(1, 2, piloto, corrida);
	    service.update(pilotoCorridaAltera);
	    pilotoCorrida = service.findById(1);
	    assertEquals(2, pilotoCorrida.getPlacement());
	 }
	 
	 @Test
     @DisplayName("Teste procurar por colocação")
	 void findByPlacementTest () {
	     List<PilotoCorrida> lista = service.findByPlacement(1);
	     assertEquals(1, lista.size());
	     lista = service.findByPlacement(2);
	     assertEquals(1, lista.size());
	     var exception = assertThrows(ObjectNotFound.class, () -> service.findByPlacement(5));
	     assertEquals("Nenhum piloto cadastrado na colocação: 5", exception.getMessage());
	 }
	 
	 @Test
	 @DisplayName("Teste procurar por corrida e ordernar por colocação")
	 @Sql ({"classpath:/resources/sqls/limpa_tabela.sql"})
	 void findByCorridaOrderByPlacement () {
	     Corrida corridaT = new Corrida(1, null, null, null);
	     Corrida corridaT2 = new Corrida(3, null, null, null);	     
	     corridaService.insert(corridaT);
	     PilotoCorrida pt = new PilotoCorrida(null, 1, piloto, corridaT);
	     service.insert(pt);
	     List<PilotoCorrida> lista = service.findByCorridaOrderByPlacement(corridaT);
	     assertEquals(1, lista.size());
	     var exception = assertThrows(ObjectNotFound.class, () -> service.findByCorridaOrderByPlacement(corridaT2));
	     assertEquals("Nenhum piloto cadastrado na corrida com o id: 3", exception.getMessage());
	 }
	 
	 @Test
	 @DisplayName("Teste procurar por piloto e ordernar por colocação")
	 @Sql({"classpath:/resources/sqls/pais.sql"})
	 @Sql({"classpath:/resources/sqls/equipe.sql"})
	 @Sql({"classpath:/resources/sqls/piloto.sql"})
	 void findByPilotoOrderByPlacement () {
	     Pais pais = new Pais(1,"Brasil");
	     paisService.insert(pais);
	     assertEquals("Brasil", pais.getNameCountry());
	     Piloto pilotoT1 = new Piloto(1, "Alvaro", equipe, pais);
	     Piloto pilotoT2 = new Piloto(3, "B", equipe, pais);
	     pilotoService.insert(pilotoT1);
	     PilotoCorrida pt = new PilotoCorrida(null, 1, pilotoT1, corrida);
	     service.insert(pt);
         List<PilotoCorrida> lista = service.findByPilotoOrderByPlacement(pilotoT1);
         assertEquals(1, lista.size());
         var exception = assertThrows(ObjectNotFound.class, () -> service.findByPilotoOrderByPlacement(pilotoT2));
         assertEquals("Nenhum piloto cadastrado nesta posição com o id: 3", exception.getMessage());
	 }
	 
}
