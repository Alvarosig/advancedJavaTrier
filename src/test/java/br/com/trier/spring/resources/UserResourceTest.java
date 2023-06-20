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
import br.com.trier.spring.models.dto.UserDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
// @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/usuario.sql")
// @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/resources/sqls/limpa_tabelas.sql")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    @Autowired
    protected TestRestTemplate rest;

    private ResponseEntity<UserDTO> getUser(String url) {
        return rest.getForEntity(url, UserDTO.class);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<List<UserDTO>> getUsers(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {
        });
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql"})
    public void findByIdTest() {
        ResponseEntity<UserDTO> response = getUser("/usuarios/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        UserDTO user = response.getBody(); // não é necessário
        assertEquals("User 1", user.getName());
    }

    @Test
    @DisplayName("Buscar por id inexistente")
    public void getNotFoundTest() {
        ResponseEntity<UserDTO> response = getUser("/usuarios/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    public void createUserTest() {
        UserDTO dto = new UserDTO(null, "nome", "email", "senha");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios", HttpMethod.POST, requestEntity, UserDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        
        UserDTO user = responseEntity.getBody();
        assertEquals("nome", user.getName());
    }
    
    @Test
    @DisplayName("Excluir usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql"})
    public void deleteUserTest() {
        UserDTO dto = new UserDTO(1, "nome", "email", "senha");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/1", HttpMethod.DELETE, requestEntity, UserDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    @DisplayName("Listar todos")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql"})
    public void listAllUserTest() { 
        ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios", HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>(){});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserDTO> userList = responseEntity.getBody();
        assertNotNull(userList);
        assertEquals(2, userList.size());
    }
    
    @Test
    @DisplayName("Alterar usuário")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql"})
    public void updateUserTest() {
        Integer userId = 1;
        UserDTO updatedUserDTO = new UserDTO(userId, "novoNome", "novoEmail", null);
        ResponseEntity<UserDTO> responseEntity = rest.exchange("/usuarios/" + userId, HttpMethod.PUT, new HttpEntity<>(updatedUserDTO), UserDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
        UserDTO updatedUser = responseEntity.getBody();
        assertNotNull(updatedUser);
        assertEquals(updatedUserDTO.getName(), updatedUser.getName());
        assertEquals(updatedUserDTO.getEmail(), updatedUser.getEmail());
        assertEquals(updatedUserDTO.getPassword(), updatedUser.getPassword());
    }
    
    @Test
    @DisplayName("Procurar por nome")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/usuario.sql"})
    public void findByNameUserTest() {
        String name = "User";
        ResponseEntity<List<UserDTO>> responseEntity = rest.exchange("/usuarios/name/{name}", HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>(){}, name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserDTO> users = responseEntity.getBody();
        assertNotNull(users);
        assertEquals(2, users.size());
        UserDTO user1 = users.get(0);
        assertEquals(1, user1.getId());
        assertEquals("User 1", user1.getName());
        assertEquals("email1", user1.getEmail());

        UserDTO user2 = users.get(1);
        assertEquals(2, user2.getId());
        assertEquals("User 2", user2.getName());
        assertEquals("email2", user2.getEmail());
    }
}