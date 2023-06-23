package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Driver;
import br.com.trier.spring.models.DriverRace;
import br.com.trier.spring.models.Race;

public interface DriverRaceRepository extends JpaRepository<DriverRace, Integer> {

    List<DriverRace> findByPlacement(Integer placement);

    List<DriverRace> findByRaceOrderByPlacement(Race race);

    List<DriverRace> findByDriverOrderByPlacement(Driver driver);
}
