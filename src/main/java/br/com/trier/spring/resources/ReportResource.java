package br.com.trier.spring.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/reports")
public class ReportResource {
    
//    @Autowired
//    private CorridaService Corridaservice;
    
//    @Autowired
//    private PaissService Paisservice;
    
//    @Autowired
//    private PistaService pistaService;
    
//    @GetMapping("/corrida-por-pais-ano/{countryId}/{year}")
//    public ResponseEntity<CorridaPaisAnoDTO> findCorridaByPaisAndAno (@PathVariable )
//    
//    Pais pais = PaisService.findById (paisId);
    
   // List<CorridaDTO> corridaDTOS = pistaService.findByCountry
}
