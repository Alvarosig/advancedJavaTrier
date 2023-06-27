package br.com.trier.spring.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.Application;
import br.com.trier.spring.config.jwt.LoginDTO;
import br.com.trier.spring.models.dto.UserDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
// @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
// @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/usuario.sql")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<UserDTO> getUser(String url, HttpHeaders headers) {
	    return rest.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), UserDTO.class);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<List<UserDTO>> getUsers(String url) {
		return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {
		});
	}
	
	private HttpHeaders headers;

	@BeforeEach
	public void setup() {
		LoginDTO loginDTO = new LoginDTO("user1@mail.com", "senha1");
	    HttpHeaders loginHeaders = new HttpHeaders();
	    loginHeaders.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, loginHeaders);
	    ResponseEntity<String> responseEntity = rest.exchange(
	            "/auth/token", 
	            HttpMethod.POST,  
	            requestEntity,    
	            String.class   
	    );
	    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	    String token = responseEntity.getBody();

	    headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);
	}
	
	@Test
    @DisplayName("Obter Token")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
    public void getToken() {
        LoginDTO loginDTO = new LoginDTO("user1@mail.com", "senha1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
        ResponseEntity<String> responseEntity = rest.exchange(
                "/auth/token", 
                HttpMethod.POST,  
                requestEntity,    
                String.class   
                );
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        String token = responseEntity.getBody();
        System.out.println("****************"+token);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        ResponseEntity<List<UserDTO>> response =  rest.exchange("/usuarios", HttpMethod.GET, new HttpEntity<>(null, headers) ,new ParameterizedTypeReference<List<UserDTO>>() {} , headers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
	
	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void findByIdTest() {
		ResponseEntity<UserDTO> response = getUser("/usuarios/1", headers);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		UserDTO user = response.getBody(); // não é necessário
		assertEquals("User1", user.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	public void getNotFoundTest() {
		ResponseEntity<UserDTO> responseEntity = getUser("/usuarios/100", headers);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Cadastrar usuário")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	public void createUserTest() {
	    // Criar o objeto de usuário a ser cadastrado
	    UserDTO dto = new UserDTO(2, "User1", "user1@mail.com", "senha1", "ADMIN,USER");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set(HttpHeaders.AUTHORIZATION, "Bearer <admin_token>");

	    HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios", HttpMethod.POST, requestEntity, UserDTO.class);

	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	    UserDTO createdUser = responseEntity.getBody();
	    assertNotNull(createdUser);
	    assertEquals("User1", createdUser.getName());
	    assertEquals("user1@mail.com", createdUser.getEmail());
	}

	@Test
	@DisplayName("Excluir usuário")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void deleteUserTest() {
		UserDTO dto = new UserDTO(1, "nome", "email", "senha", "ADMIN,USER");
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/1", HttpMethod.DELETE, requestEntity,UserDTO.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}

	@Test
	@DisplayName("Excluir usuário inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void deleteUserInexistTest() {
	    ResponseEntity<Void> responseEntity = rest.exchange("/usuarios/10", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Listar todos")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void listAllUserTest() {
		UserDTO dto = new UserDTO(1, "nome", "user1@mail.com", "senha1", "ADMIN,USER");
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<UserDTO>>() {});
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		List<UserDTO> userList = responseEntity.getBody();
		assertNotNull(userList);
		assertEquals(2, userList.size());
	}

	@Test
	@DisplayName("Listar todos sem cadastro") // Refazer
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql" })
	public void listaAllUserNoExistTest() { 
	    ResponseEntity<Void> responseEntity = rest.exchange("/usuarios", HttpMethod.GET, new HttpEntity<>(headers), Void.class);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
		
	@Test
	@DisplayName("Alterar usuário")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void updateUserTest() {
		UserDTO updatedUserDTO = new UserDTO(1, "novoNome", "novo@Email", "12a", "ADMIN,USER");
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(updatedUserDTO, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/1", HttpMethod.PUT, requestEntity, UserDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		UserDTO updatedUser = responseEntity.getBody();
		assertNotNull(updatedUser);
		assertEquals(updatedUserDTO.getName(), updatedUser.getName());
		assertEquals(updatedUserDTO.getEmail(), updatedUser.getEmail());
	}

	@Test
	@DisplayName("Alterar usuário inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" }) 
	public void updateUserNoExist() {
		UserDTO dto = new UserDTO(1, "nomeNovo", "emailNovo", "senhaNova", "ADMIN,USER");
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/10", HttpMethod.PUT, requestEntity, UserDTO.class);
	    assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Procurar por nome")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void findByNameUserTest() {
		String name = "User";
		UserDTO dto = new UserDTO(2, "nome", "email", "senha", "USER");
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios/nome/{name}", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<UserDTO>>() {}, name);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		List<UserDTO> users = responseEntity.getBody();
		assertNotNull(users);
		assertEquals(2, users.size());
		UserDTO user1 = users.get(0);
		assertEquals(1, user1.getId());
		assertEquals("User1", user1.getName());
		assertEquals("user1@mail.com", user1.getEmail());
	}
}