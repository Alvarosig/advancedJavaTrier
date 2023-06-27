package br.com.trier.spring.resources;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.dto.RaceCountryYearDTO;
import br.com.trier.spring.models.dto.RaceDTO;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.TrackService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@RestController
@RequestMapping("/reports")
public class ReportResource {

	@Autowired
	private CountryService countryService;

	@Autowired
	private TrackService trackService;

	@Autowired
	private RaceService raceService;

	@Secured({"ROLE_USER"})
	@GetMapping("/corridas_por_pais_e_ano/{countryId}/{year}")
	public ResponseEntity<RaceCountryYearDTO> findRaceByCountryAndYear(@PathVariable Integer countryId, @PathVariable Integer year) {

		Country country = countryService.findById(countryId);

		List<RaceDTO> raceDTOs = trackService.findByCountryOrderBySizeDesc(country).stream()
		.flatMap(speedway -> {
			try {
				return raceService.findByTrackOrderByDate(speedway).stream();
			} catch (ObjectNotFound e) {
				return Stream.empty();
			}
		})
		.filter(race -> race.getDate().getYear() == year)
		.map(Race::toDTO)
		.toList();

		return ResponseEntity.ok(new RaceCountryYearDTO(year, country.getName(), raceDTOs.size(), raceDTOs));
	}

}
