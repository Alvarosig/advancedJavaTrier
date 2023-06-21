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
import br.com.trier.spring.models.dto.CampeonatoDTO;
import br.com.trier.spring.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampeonatoResourceTest {
	
	@Autowired
    protected TestRestTemplate rest;
	
	private ResponseEntity<CampeonatoDTO> getTeam(String url) {
        return rest.getForEntity(url, CampeonatoDTO.class);
    }
    
    @SuppressWarnings("unused")
    private ResponseEntity<List<CampeonatoDTO>> getTeams(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CampeonatoDTO>>() {
        });
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void findByIdTest() {
        ResponseEntity<CampeonatoDTO> response = getTeam("/campeonatos/2");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        CampeonatoDTO campeonato = response.getBody(); // não é necessário
        assertEquals("F1 Racers", campeonato.getChampDesc());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void getNotFoundTest() {
        ResponseEntity<CampeonatoDTO> response = getTeam("/campeonatos/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar campeonato")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    void createChampionshipTest() {
        CampeonatoDTO dto = new CampeonatoDTO(null, "campeonato", 2010);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CampeonatoDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<CampeonatoDTO> responseEntity = rest.exchange("/campeonatos", HttpMethod.POST, requestEntity, CampeonatoDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        
        CampeonatoDTO championship = responseEntity.getBody();
        assertEquals("campeonato", championship.getChampDesc()); 
    }
    
    @Test
    @DisplayName("Excluir campeonato")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void deleteChampionshipTest() {
        CampeonatoDTO dto = new CampeonatoDTO(1, "campeonato", 2010);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CampeonatoDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<CampeonatoDTO> responseEntity = rest.exchange("/campeonatos/1", HttpMethod.DELETE, requestEntity, CampeonatoDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    @DisplayName("Excluir campeonato inexistente")
    @Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql" })
    void deleteChampionshipInexistTest() {
        ResponseEntity<CampeonatoDTO> responseEntity = rest.exchange("/campeonato/10", HttpMethod.DELETE, new HttpEntity<>(""), CampeonatoDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Listar todos campeonatos")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void listAllChampionshipsTest() { 
        ResponseEntity<List<CampeonatoDTO>> responseEntity = rest.exchange("/campeonatos", HttpMethod.GET, null, new ParameterizedTypeReference<List<CampeonatoDTO>>(){});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CampeonatoDTO> championshipList = responseEntity.getBody();
        assertNotNull(championshipList);
        assertEquals(3, championshipList.size());
    }
    
    @Test
    @DisplayName("Listar todos campeonatos sem cadastro")
    @Sql({ "classpath:/resources/sqls/limpa_tabela.sql"})
    void listaAllChampionshipsNoExistTest () {
        ResponseEntity<StandardError> response = rest.getForEntity("/campeonatos", StandardError.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Alterar campeonato")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void updateChampionshipTest() {
        Integer championshipId = 1;
        CampeonatoDTO updatedChampionshipDTO = new CampeonatoDTO(championshipId, "novo campeonato", 2015);
        ResponseEntity<CampeonatoDTO> responseEntity = rest.exchange("/campeonatos/" + championshipId, HttpMethod.PUT, new HttpEntity<>(updatedChampionshipDTO), CampeonatoDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
        CampeonatoDTO updatedChampionship = responseEntity.getBody();
        assertNotNull(updatedChampionship);
        assertEquals(updatedChampionshipDTO.getChampDesc(), updatedChampionship.getChampDesc());
    }
    
    @Test
    @DisplayName("Alterar campeonato inexistente")
    @Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql" }) 
    void updateChampionshipNoExist() {
        CampeonatoDTO dto = new CampeonatoDTO(1, "campeonato Novo", 2010);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CampeonatoDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<CampeonatoDTO> responseEntity = rest.exchange("/campeonatos/10", HttpMethod.PUT, requestEntity, CampeonatoDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Procurar por nome do Campeonato")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void findByChampionshipNamePaisTest() {
        ResponseEntity<List<CampeonatoDTO>> response = getTeams("/campeonatos/nome/f1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, response.getBody().size());
    }
    
    @Test
    @DisplayName("Procurar por Campeonato entre anos")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    void findByChampionshipBetweenYearsTest () {
        ResponseEntity<List<CampeonatoDTO>> response = getTeams("/campeonatos/entre/2010/2017");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(2, response.getBody().size());
    }
}
