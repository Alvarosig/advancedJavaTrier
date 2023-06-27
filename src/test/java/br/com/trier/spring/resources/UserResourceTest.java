package br.com.trier.spring.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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
	
	private HttpHeaders getHeaders(String email, String password){
        LoginDTO loginDTO = new LoginDTO(email, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
        ResponseEntity<String> responseEntity = rest.exchange(
                "/auth/token", 
                HttpMethod.POST,  
                requestEntity,    
                String.class   
                );
        String token = responseEntity.getBody();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
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
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    ResponseEntity<UserDTO> response = getUser("/usuarios/2", headers);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    UserDTO user = response.getBody();
	    assertEquals("User1", user.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void getNotFoundTest() {
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    ResponseEntity<UserDTO> responseEntity = getUser("/usuarios/100", headers);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Cadastrar usuário")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void createUserTest() {
	    UserDTO dto = new UserDTO(null, "User3", "user3@mail.com", "senha3", "ADMIN,USER");
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios", HttpMethod.POST, requestEntity, UserDTO.class);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    UserDTO createdUser = responseEntity.getBody();
	    assertEquals("User3", createdUser.getName());
	}

	@Test
	@DisplayName("Excluir usuário")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void deleteUserTest() {
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
	    ResponseEntity<Void> responseEntity = rest.exchange("/usuarios/2", HttpMethod.DELETE, requestEntity, Void.class);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Excluir usuário inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void deleteUserInexistTest() {
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    ResponseEntity<Void> responseEntity = rest.exchange("/usuarios/10", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Listar todos")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void listAllUserTest() {
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios", HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<UserDTO>>() {});
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    List<UserDTO> userList = responseEntity.getBody();
	    assertNotNull(userList);
	    assertEquals(2, userList.size());
	}
	
	@Test
	@DisplayName("Alterar usuário")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void updateUserTest() {
	    UserDTO updatedUserDTO = new UserDTO(1, "novoNome", "novo@Email", "12a", "ADMIN,USER");
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<UserDTO> requestEntity = new HttpEntity<>(updatedUserDTO, headers);
	    ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/2", HttpMethod.PUT, requestEntity, UserDTO.class);
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
	    UserDTO dto = new UserDTO(10, "nomeNovo", "emailNovo", "senhaNova", "ADMIN,USER");
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/10", HttpMethod.PUT, requestEntity, UserDTO.class);
	    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@DisplayName("Procurar por nome")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql" })
	public void findByNameUserTest() {
	    String name = "User1";
	    HttpHeaders headers = getHeaders("user1@mail.com", "senha1");
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	    ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios/nome/{name}", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<UserDTO>>() {}, name);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    List<UserDTO> users = responseEntity.getBody();
	    assertNotNull(users);
	    assertEquals(1, users.size());
	    UserDTO user1 = users.get(0);
	    assertEquals(2, user1.getId());
	    assertEquals("User1", user1.getName());
	    assertEquals("user1@mail.com", user1.getEmail());
	}

}