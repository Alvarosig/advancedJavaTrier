package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.DriverRace;
import br.com.trier.spring.models.Race;

public interface DriverRaceService {
	
	DriverRace findById(Integer id);
    
	DriverRace insert(DriverRace driverRace);
    
    List<DriverRace> listAll();
    
    DriverRace update(DriverRace driverRace);
    
    void delete(Integer id);
    
    List<DriverRace> findByPlacement(Integer placement);
	
	List<DriverRace> findByRaceOrderByPlacement(Race race);
	
	List<DriverRace> findByDriverOrderByPlacement(Driver driver);
}
