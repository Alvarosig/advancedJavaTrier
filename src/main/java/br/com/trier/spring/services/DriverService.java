package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Team;
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Driver;

public interface DriverService {

    Driver findById(Integer id);

    Driver insert(Driver driver);

    List<Driver> listAll();

    Driver update(Driver driver);

    void delete(Integer id);

    List<Driver> findByNameStartsWithIgnoreCase(String name);

    List<Driver> findByCountryOrderByNameDesc(Country country);

    List<Driver> findByTeamOrderByNameDesc(Team team);
}
