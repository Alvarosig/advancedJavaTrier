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
import br.com.trier.spring.models.dto.EquipeDTO;
import br.com.trier.spring.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class EquipeResourceTest {
	
	@Autowired
    protected TestRestTemplate rest;
	
	private ResponseEntity<EquipeDTO> getTeam(String url) {
        return rest.getForEntity(url, EquipeDTO.class);
    }
    
    @SuppressWarnings("unused")
    private ResponseEntity<List<EquipeDTO>> getTeams(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<EquipeDTO>>() {
        });
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void findByIdTest() {
        ResponseEntity<EquipeDTO> response = getTeam("/equipes/2");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        EquipeDTO equipe = response.getBody(); // não é necessário
        assertEquals("Bmw", equipe.getTeamName());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void getNotFoundTest() {
        ResponseEntity<EquipeDTO> response = getTeam("/equipes/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    public void createCountryTest() {
    	EquipeDTO dto = new EquipeDTO(null, "equipe");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EquipeDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<EquipeDTO> responseEntity = rest.exchange("/equipes", HttpMethod.POST, requestEntity, EquipeDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        
        EquipeDTO team = responseEntity.getBody();
        assertEquals("equipe", team.getTeamName()); 
    }
    
    @Test
    @DisplayName("Excluir equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void deleteTeamTest() {
    	EquipeDTO dto = new EquipeDTO(1, "equipe");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EquipeDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<EquipeDTO> responseEntity = rest.exchange("/equipes/1", HttpMethod.DELETE, requestEntity, EquipeDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
	@DisplayName("Excluir equipe inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql" })
	public void deleteTeamInexistTest() {
		ResponseEntity<EquipeDTO> responseEntity = rest.exchange("/equipes/10", HttpMethod.DELETE, new HttpEntity<>(""),EquipeDTO.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Listar todas equipes")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void listAllTeamsTest() { 
        ResponseEntity<List<EquipeDTO>> responseEntity = rest.exchange("/equipes", HttpMethod.GET, null, new ParameterizedTypeReference<List<EquipeDTO>>(){});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<EquipeDTO> teamList = responseEntity.getBody();
        assertNotNull(teamList);
        assertEquals(3, teamList.size());
    }
    
    @Test
	@DisplayName("Listar todas equipes sem cadastro")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql"})
	public void listaAllTeamsNoExistTest () {
		ResponseEntity<StandardError> response = rest.getForEntity("/equipes", StandardError.class);
	    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Alterar equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void updateTeamTest() {
        Integer equipeId = 1;
        EquipeDTO updatedEquipeDTO = new EquipeDTO(equipeId, "nova equipe");
        ResponseEntity<EquipeDTO> responseEntity = rest.exchange("/equipes/" + equipeId, HttpMethod.PUT, new HttpEntity<>(updatedEquipeDTO), EquipeDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
        EquipeDTO updatedEquipe = responseEntity.getBody();
        assertNotNull(updatedEquipe);
        assertEquals(updatedEquipeDTO.getTeamName(), updatedEquipe.getTeamName());
    }
    
    @Test
	@DisplayName("Alterar equipe inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql" }) 
	public void updateTeamNoExist() {
		EquipeDTO dto = new EquipeDTO(1, "equipeNovo");
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<EquipeDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<EquipeDTO> responseEntity = rest.exchange("/equipes/10", HttpMethod.PUT, requestEntity, EquipeDTO.class);
	    assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Procurar por nome da Equipe")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/equipe.sql"})
    public void findByTeamNamePaisTest() {
    	 ResponseEntity<List<EquipeDTO>> response = getTeams("/equipes/nome/f");
         assertEquals(response.getStatusCode(), HttpStatus.OK);
         assertEquals(1, response.getBody().size());
    }
}
