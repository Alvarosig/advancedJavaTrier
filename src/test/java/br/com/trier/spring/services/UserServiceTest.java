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
import br.com.trier.spring.models.User;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class UserServiceTest extends BaseTests {

	@Autowired
	UserService userService;

	@Test
	@DisplayName("Teste buscar usuário por ID")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByIdTest() {
		var usuario = userService.findById(2);
		assertNotNull(usuario);
		assertEquals(2, usuario.getId());
		assertEquals("User1", usuario.getName());
	}

	@Test
	@DisplayName("Teste buscar usuário por ID inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findById(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir usuário")
	void insertUserTest() {
		User usuario = new User(null, "teste", "teste@mail.com", "teste123", "ADMIN,USER");
		userService.insert(usuario);
		usuario = userService.findById(1);
		assertEquals(1, usuario.getId());
		assertEquals("teste", usuario.getName());
		assertEquals("teste@mail.com", usuario.getEmail());
		assertEquals("teste123", usuario.getPassword());
	}

	@Test
	@DisplayName("Teste Remover usuário")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void removeUserTest() {
		userService.delete(2);
		List<User> lista = userService.listAll();
		assertEquals(1, lista.size());
		assertEquals(3, lista.get(0).getId());
	}

	@Test
	@DisplayName("Teste remover usuário inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void removeUserNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> userService.delete(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void listAllUsersTest() {
		List<User> lista = userService.listAll();
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Teste listar todos sem usuários cadastrados")
	void listAllUsersEmptyTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> userService.listAll());
		assertEquals("Nenhum usuário cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar usuário")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void updateUsersTest() {
		var usuario = userService.findById(2);
		assertEquals("User1", usuario.getName());
		var usuarioAltera = new User(2, "altera", "altera@mail.com", "altera123", "ADMIN,USER");
		userService.update(usuarioAltera);
		usuario = userService.findById(2);
		assertEquals("altera", usuario.getName());
	}

	@Test
	@DisplayName("Teste alterar usuário sem cadastro")
	void updateUsersNonExistsTest() {
		var usuarioAltera = new User(2, "altera", "altera", "altera", "ADMIN,USER");
		var exception = assertThrows(ObjectNotFound.class, () -> userService.update(usuarioAltera));
		assertEquals("O usuário 2 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar por nome que inicia com")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByNameStartsWithTest() {
		List<User> lista = userService.findByNameStartingWithIgnoreCase("u");
		assertEquals(2, lista.size());
		lista = userService.findByNameStartingWithIgnoreCase("user1");
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findByNameStartingWithIgnoreCase("c"));
		assertEquals("Nenhum nome de usuário inicia com c", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir usuário com email duplicado")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void insertDuplicatedEmailTest() {
		User usuario2 = new User(null, "Alv", "user1@mail.com", "1234", "ADMIN,USER");
		var exception = assertThrows(IntegrityViolation.class, () -> userService.insert(usuario2));
		assertEquals("Email já cadastrado", exception.getMessage());
	}

	@Test
    @DisplayName ("Teste alterar usuário com email duplicado") 
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
    void updateDuplicatedEmailTest () {
		User usuario1 = new User(null, "User 2", "user1@mail.com", "123", "ADMIN,USER");
		var exception = assertThrows(IntegrityViolation.class, () -> userService.insert(usuario1));
		assertEquals("Email já cadastrado", exception.getMessage());
   }
	
}