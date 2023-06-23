package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Team;
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    List<Driver> findByNameStartsWithIgnoreCase(String name);

    Driver findByNameEqualsIgnoreCase(String name);

    List<Driver> findByCountryOrderByNameDesc(Country country);

    List<Driver> findByTeamOrderByNameDesc(Team team);
}
