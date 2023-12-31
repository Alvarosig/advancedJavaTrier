package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.dto.DriverDTO;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.DriverService;
import br.com.trier.spring.services.TeamService;

@RestController
@RequestMapping("/pilotos")
public class DriverResource {

	@Autowired
	private DriverService service;

	@Autowired
	private CountryService countryService;

	@Autowired
	private TeamService teamService;
	
	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<DriverDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<DriverDTO> insert(@RequestBody Driver driver) {
		countryService.findById(driver.getCountry().getId());
		teamService.findById(driver.getTeam().getId());
		return ResponseEntity.ok(service.insert(driver).toDTO());
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<DriverDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(driver -> driver.toDTO()).toList());
	}
	
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<DriverDTO> update(@PathVariable Integer id, @RequestBody Driver driver) {
		countryService.findById(driver.getCountry().getId());
		teamService.findById(driver.getTeam().getId());
		driver.setId(id);
		return ResponseEntity.ok(service.update(driver).toDTO());
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/nome/{name}")
	public ResponseEntity<List<DriverDTO>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name).stream().map(driver -> driver.toDTO()).toList());
	}
	
	@Secured({"ROLE_USER"})
	@GetMapping("/pais/{id}")
	public ResponseEntity<List<DriverDTO>> findByCountryOrderByName(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByCountryOrderByNameDesc(countryService.findById(id)).stream().map(driver -> driver.toDTO()).toList());
	}
	
	@Secured({"ROLE_USER"})
	@GetMapping("/equipe/{id}")
	public ResponseEntity<List<DriverDTO>> findByTeamOrderByNameDesc(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByTeamOrderByNameDesc(teamService.findById(id)).stream().map(driver -> driver.toDTO()).toList());
	}
}
