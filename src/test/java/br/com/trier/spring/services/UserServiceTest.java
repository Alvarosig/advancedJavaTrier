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
import br.com.trier.spring.models.User;
import jakarta.transaction.Transactional;

@Transactional
class UserServiceTest extends BaseTests {

    @Autowired
    UserService userService;
    
    @Test
    @DisplayName("Teste buscar usuário por ID")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void findByIdTest() {
        var usuario = userService.findById(1);
        assertNotNull(usuario);
        assertEquals(1, usuario.getId());
        assertEquals("User 1", usuario.getName());
        assertEquals("email1", usuario.getEmail());
        assertEquals("senha1", usuario.getPassword());
        
    }

    @Test
    @DisplayName("Teste buscar usuário por ID inexistente")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void findByIdNonExistsTest() {
        var usuario = userService.findById(10);
        assertNull(usuario);      
    }
    
    @Test
    @DisplayName("Teste inserir usuário")
    void insertUserTest () {
        User usuario = new User (null, "insert", "insert", "insert");
        userService.insert(usuario);
        usuario = userService.findById(1);
        assertEquals(1, usuario.getId());
        assertEquals("insert", usuario.getName());
        assertEquals("insert", usuario.getEmail());
        assertEquals("insert", usuario.getPassword());
    }
    
    @Test
    @DisplayName("Teste Remover usuário")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void removeUserTest () {
        userService.delete(1);
        List<User> lista = userService.listAll();
        assertEquals(1, lista.size());
        assertEquals(2, lista.get(0).getId());
    }
    
    @Test
    @DisplayName ("Teste remover usuário inexistente")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void removeUserNonExistsTest () {
        userService.delete(10);
        List<User> lista = userService.listAll();
        assertEquals(2, lista.size());
        assertEquals(1, lista.get(0).getId());
    }
    
    @Test
    @DisplayName ("Teste listar todos")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void listAllUsersTest () {
        List<User> lista = userService.listAll();
        assertEquals(2, lista.size());
    }
    
    @Test
    @DisplayName ("Teste alterar usuário")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void udateUsersTest () {
        var usuario = userService.findById(1);
        assertEquals("User 1", usuario.getName());
        var usuarioAltera = new User(1, "altera", "altera", "altera");
        userService.update(usuarioAltera);
        usuario = userService.findById(1);
        assertEquals("altera", usuario.getName());
    }
    
    @Test
    @DisplayName ("Teste buscar por nome que inicia com")
    @Sql ( {"classpath:/resources/sqls/usuario.sql"})
    void findByNameStartsWithTest () {
        List<User> lista = userService.findByNameStartingWithIgnoreCase("u");
        assertEquals(2, lista.size());
        lista = userService.findByNameStartingWithIgnoreCase("user 1");
        assertEquals(1, lista.size());
        lista = userService.findByNameStartingWithIgnoreCase("c");
        assertEquals(0, lista.size());
    }
}