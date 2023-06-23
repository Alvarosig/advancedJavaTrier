package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.dto.CountryDTO;
import br.com.trier.spring.services.CountryService;

@RestController
@RequestMapping(value = "/countries")
public class CountryResource {
    
    @Autowired
    private CountryService service;
    
    @PostMapping
    public ResponseEntity<CountryDTO> insert(@RequestBody CountryDTO countryDTO) {
        Country country = new Country(countryDTO);
        return ResponseEntity.ok(service.insert(country).toDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> findById(@PathVariable Integer id) {
        Country country = service.findById(id);
        return ResponseEntity.ok(country.toDTO());
    }
    
    @GetMapping
    public ResponseEntity<List<CountryDTO>> listAll() {
        return ResponseEntity.ok(service.listAll().stream().map(country -> country.toDTO()).toList());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> update(@PathVariable Integer id, @RequestBody CountryDTO countryDTO) {
        Country country = new Country(countryDTO);
        country.setId(id);
        return ResponseEntity.ok(service.update(country).toDTO());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/name/{countryName}")
    public ResponseEntity<List<CountryDTO>> findByNameStartingWithIgnoreCase(@PathVariable String countryName) {
        return ResponseEntity.ok(service.findByNameStartingWithIgnoreCase(countryName).stream().map(country -> country.toDTO()).toList());
    }
   
}
