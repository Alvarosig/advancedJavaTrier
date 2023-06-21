package br.com.trier.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import br.com.trier.spring.services.CampeonatoService;
import br.com.trier.spring.services.EquipeService;
import br.com.trier.spring.services.PaisService;
import br.com.trier.spring.services.PistaService;
import br.com.trier.spring.services.UserService;
import br.com.trier.spring.services.impl.CampeonatoServiceImpl;
import br.com.trier.spring.services.impl.EquipeServiceImpl;
import br.com.trier.spring.services.impl.PaisServiceImpl;
import br.com.trier.spring.services.impl.PistaServiceImpl;
import br.com.trier.spring.services.impl.UserServiceImpl;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class BaseTests {

    @Bean
    public UserService userService () {
        return new UserServiceImpl();  
    }
    
    @Bean
    public PaisService paisService () {
        return new PaisServiceImpl();
    }

    @Bean
    public EquipeService equipeService () {
        return new EquipeServiceImpl ();
    }
    
    @Bean
    public CampeonatoService campeonatoService () {
        return new CampeonatoServiceImpl ();
    }
    
    @Bean
    public PistaService pistaService () {
        return new PistaServiceImpl ();
    }
}
