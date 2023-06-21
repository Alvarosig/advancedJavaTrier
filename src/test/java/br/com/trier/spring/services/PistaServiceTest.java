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
import br.com.trier.spring.models.Pista;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class PistaServiceTest extends BaseTests{
    
    @Autowired
    PistaService service;
    
    @Autowired
    PaisService paisService;
    
    @Test
    @DisplayName("Teste buscar pista por ID")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void findByIdTest() {
        var pista = service.findById(1);
        assertNotNull(pista);
        assertEquals(1, pista.getId());
        assertEquals("Interlagos", pista.getName());
    }
    
    @Test
    @DisplayName("Teste buscar pista por ID inexistente")
    @Sql({ "classpath:/resources/sqls/pais.sql" })
    void findByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Pista 10 não existe", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Teste inserir pista")
    void insertPistaTest() {
        Pista pista = new Pista(null, "Interlagos", 5000, null);
        service.insert(pista);
        pista = service.findById(1);
        assertEquals(1, pista.getId());
        assertEquals("Interlagos", pista.getName());
        assertEquals(5000, pista.getSize());
    }
    
    @Test
    @DisplayName("Teste inserir pista nula ou menor que zero")
    void insertPistaNullTest() {
        Pista pista = new Pista(null, "Caixa", 0, null);
        var exception = assertThrows(IntegrityViolation.class, () -> service.insert(pista));
        assertEquals("Tamanho da pista inválido", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Teste Remover pista")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void removePistaTest() {
        service.delete(1);
        List<Pista> lista = service.listAll();
        assertEquals(2, lista.size());
        assertEquals(2, lista.get(0).getId());
        assertEquals(3, lista.get(1).getId());
    }
    
    @Test
    @DisplayName("Teste remover pista inexistente")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void removePistaNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
        assertEquals("Pista 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todas pistas com cadastro")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void listAllPistaTest() {
        List<Pista> lista = service.listAll();
        assertEquals(3, lista.size());  
    }
    
    @Test
    @DisplayName("Teste listar todas sem nenhum cadastro")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void listAllNoPistaTest() {
        List<Pista> lista = service.listAll();
        assertEquals(3, lista.size());
        service.delete(1);
        service.delete(2);
        service.delete(3);
        var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhuma pista cadastrada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar pista")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void updatePistaTest() {
        var pista = service.findById(1);
        assertEquals("Interlagos", pista.getName());
        var pistaAltera = new Pista(1, "altera", 500, null);
        service.update(pistaAltera);
        pista = service.findById(1);
        assertEquals("altera", pista.getName());
    }
    
    @Test
    @DisplayName("Teste buscar por pista que inicia com")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void findByPistaStartsWithTest() {
        List<Pista> lista = service.findByNameStartsWithIgnoreCase("I");
        assertEquals(1, lista.size());
        lista = service.findByNameStartsWithIgnoreCase("Junipero");
        assertEquals(1, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("z"));
        assertEquals("Nenhuma pista cadastrada com esse nome", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar por tamanho de pista por pais em ordem")
    @Sql({ "classpath:/resources/sqls/pista.sql", "classpath:/resources/sqls/pais.sql" })
    void findByPaisOrderTest() {
        
    }
    
    @Test
    @DisplayName("Teste buscar por tamanho de pista por pais em ordem (sem pista)")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void findByPaisOrderEmptyTest() {

    }
    
    @Test
    @DisplayName("Teste buscar por pista com tamanhos entre")
    @Sql({ "classpath:/resources/sqls/pista.sql" })
    void findBySizeBetweenTest() {
        List<Pista> pistas = service.findBySizeBetween(2000, 8000);
        assertEquals(2, pistas.size());
        
        assertEquals("Interlagos", pistas.get(0).getName());
        assertEquals(5000, pistas.get(0).getSize());
        assertEquals("Madero", pistas.get(1).getName());
        assertEquals(7500, pistas.get(1).getSize());
    }
    
    @Test
    @DisplayName("Teste buscar por pista com tamanhos inválidos entre")
    void findBySizeBetweenInvalidTest() {
        Integer sizeIni = 10000;
        Integer sizeFin = 12000;
        assertThrows(ObjectNotFound.class, () -> service.findBySizeBetween(sizeIni, sizeFin));
    }
}
