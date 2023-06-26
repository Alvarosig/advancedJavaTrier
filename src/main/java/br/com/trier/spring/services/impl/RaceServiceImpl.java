package br.com.trier.spring.services.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Championship;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.models.Track;
import br.com.trier.spring.repositories.RaceRepository;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class RaceServiceImpl implements RaceService {

	@Autowired
    private RaceRepository raceRepository;

	ZonedDateTime zonedDateTime = ZonedDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");
	String formattedString = formatter.format(zonedDateTime);
	
	@Override
	public Race findById(Integer id) {
		return raceRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Race with ID %s does not exist".formatted(id)));
	}

	@Override
	public Race insert(Race race) {
		return raceRepository.save(race);
	}

	@Override
	public List<Race> listAll() {
		List<Race> raceList = raceRepository.findAll();
		if (raceList.isEmpty()) {
			throw new ObjectNotFound("No races registered");
		}
		return raceList;
	}

	@Override
	public Race update(Race race) {
		findById(race.getId());
		return raceRepository.save(race);
	}

	@Override
	public void delete(Integer id) {
		Race race = findById(id);
		raceRepository.delete(race);
	}

	@Override
	public List<Race> findByDate(ZonedDateTime date) {
		List<Race> raceList = raceRepository.findByDate(date);
		if (raceList.isEmpty()) {
			throw new ObjectNotFound("No races found on the selected date");
		}
		return raceList;
	}

	@Override
	public List<Race> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2) {
		List<Race> raceList = raceRepository.findByDateBetween(date1, date2);
		if (raceList.isEmpty()) {
			throw new ObjectNotFound("No races found between the selected dates");
		}
		return raceList;
	}

	@Override
	public List<Race> findByTrackOrderByDate(Track track) {
		List<Race> raceList = raceRepository.findByTrackOrderByDate(track);
		if (raceList.isEmpty()) {
			throw new ObjectNotFound("No races registered on the track: %s".formatted(track.getName()));
		}
		return raceList;
	}

	@Override
	public List<Race> findByChampionshipOrderByDate(Championship championship) {
		List<Race> raceList = raceRepository.findByChampionshipOrderByDate(championship);
		if (raceList.isEmpty()) {
			throw new ObjectNotFound("No races registered in the championship: %s".formatted(championship.getChampDesc()));
		}
		return raceList;
	}

}
