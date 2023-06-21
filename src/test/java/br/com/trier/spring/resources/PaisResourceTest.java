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
import br.com.trier.spring.models.dto.PaisDTO;
import br.com.trier.spring.resources.exceptions.StandardError;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PaisResourceTest {
    
    @Autowired
    protected TestRestTemplate rest;
    
    private ResponseEntity<PaisDTO> getCountry(String url) {
        return rest.getForEntity(url, PaisDTO.class);
    }
    
    @SuppressWarnings("unused")
    private ResponseEntity<List<PaisDTO>> getCountries(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<PaisDTO>>() {
        });
    }
    
    @Test
    @DisplayName("Buscar por id")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void findByIdTest() {
        ResponseEntity<PaisDTO> response = getCountry("/paises/1");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        PaisDTO pais = response.getBody(); // não é necessário
        assertEquals("Brasil", pais.getNameCountry());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void getNotFoundTest() {
        ResponseEntity<PaisDTO> response = getCountry("/paises/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    @DisplayName("Cadastrar país")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql"})
    public void createCountryTest() {
        PaisDTO dto = new PaisDTO(null, "nome");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaisDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<PaisDTO> responseEntity = rest.exchange("/paises", HttpMethod.POST, requestEntity, PaisDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        
        PaisDTO user = responseEntity.getBody();
        assertEquals("nome", user.getNameCountry()); 
    }
    
    @Test
    @DisplayName("Excluir país")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void deleteCountryTest() {
        PaisDTO dto = new PaisDTO(1, "nome");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaisDTO> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<PaisDTO> responseEntity = rest.exchange("/paises/1", HttpMethod.DELETE, requestEntity, PaisDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
	@DisplayName("Excluir país inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql" })
	public void deleteCountryInexistTest() {
		ResponseEntity<PaisDTO> responseEntity = rest.exchange("/paises/10", HttpMethod.DELETE, new HttpEntity<>(""),PaisDTO.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Listar todos países")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void listAllCountryTest() { 
        ResponseEntity<List<PaisDTO>> responseEntity = rest.exchange("/paises", HttpMethod.GET, null, new ParameterizedTypeReference<List<PaisDTO>>(){});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<PaisDTO> countryList = responseEntity.getBody();
        assertNotNull(countryList);
        assertEquals(3, countryList.size());
    }
    
    @Test
	@DisplayName("Listar todos paises sem cadastro")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql"})
	public void listaAllCountryNoExistTest () {
		ResponseEntity<StandardError> response = rest.getForEntity("/paises", StandardError.class);
	    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Alterar pais")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void updateCountryTest() {
        Integer paisId = 1;
        PaisDTO updatedPaisDTO = new PaisDTO(paisId, "novo Pais");
        ResponseEntity<PaisDTO> responseEntity = rest.exchange("/paises/" + paisId, HttpMethod.PUT, new HttpEntity<>(updatedPaisDTO), PaisDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        
        PaisDTO updatedPais = responseEntity.getBody();
        assertNotNull(updatedPais);
        assertEquals(updatedPaisDTO.getNameCountry(), updatedPais.getNameCountry());
    }
    
    @Test
	@DisplayName("Alterar país inexistente")
	@Sql({ "classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql" }) 
	public void updateCountryNoExist() {
		PaisDTO dto = new PaisDTO(1, "paisNovo");
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<PaisDTO> requestEntity = new HttpEntity<>(dto, headers);
	    ResponseEntity<PaisDTO> responseEntity = rest.exchange("/pais/10", HttpMethod.PUT, requestEntity, PaisDTO.class);
	    assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
    
    @Test
    @DisplayName("Procurar por nome do país")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/pais.sql"})
    public void findByCountryNamePaisTest() {
    	 ResponseEntity<List<PaisDTO>> response = getCountries("/paises/nome/br");
         assertEquals(response.getStatusCode(), HttpStatus.OK);
         assertEquals(1, response.getBody().size());
    }
}
