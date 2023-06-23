package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.DriverRace;
import br.com.trier.spring.models.Race;
import br.com.trier.spring.repositories.DriverRaceRepository;
import br.com.trier.spring.services.DriverRaceService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class DriverRaceServiceImpl implements DriverRaceService {
	
	@Autowired
    private DriverRaceRepository repository;
	
	@Override
	public DriverRace findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound ("Race/Driver %s does not exist".formatted(id)));
	}

	@Override
	public DriverRace insert(DriverRace driverRace) {
		return repository.save(driverRace);
	}

	@Override
	public List<DriverRace> listAll() {
		List<DriverRace> lista = repository.findAll(); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("No race/driver registered");
        }
        return lista;
	}

	@Override
	public DriverRace update(DriverRace driverRace) {
		 findById(driverRace.getId());
	     return repository.save(driverRace);
	}

	@Override
	public void delete(Integer id) {
		 DriverRace driverRace = findById(id);
	     repository.delete(driverRace); 
	}

	@Override
	public List<DriverRace> findByPlacement(Integer placement) {
		List<DriverRace> lista = repository.findByPlacement(placement); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("No driver registered with placement: %s".formatted(placement));
        }
        return lista;
	}

	@Override
	public List<DriverRace> findByRaceOrderByPlacement(Race race) {
		List<DriverRace> lista = repository.findByRaceOrderByPlacement(race);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("No driver registered in the race with id: %s".formatted(race.getId()));
		}
		return lista;
	}

	@Override
	public List<DriverRace> findByDriverOrderByPlacement(Driver driver) {
		List<DriverRace> lista = repository.findByDriverOrderByPlacement(driver);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("No driver registered in this position with id: %s".formatted(driver.getId()));
		}
		return lista;
	}
}
