package br.com.trier.spring.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.Application;
import br.com.trier.spring.models.dto.CampeonatoDTO;

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
    public void findByIdTest() {
        ResponseEntity<CampeonatoDTO> response = getTeam("/campeonatos/2");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        CampeonatoDTO campeonato = response.getBody(); // não é necessário
        assertEquals("F1 Racers", campeonato.getChampDesc());
    }
    
    @Test
    @DisplayName("Buscar por id inexistente")
    @Sql({"classpath:/resources/sqls/limpa_tabela.sql", "classpath:/resources/sqls/campeonato.sql"})
    public void getNotFoundTest() {
        ResponseEntity<CampeonatoDTO> response = getTeam("/campeonatos/100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
