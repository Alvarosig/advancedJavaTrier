package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Team;
import br.com.trier.spring.models.Country;
import br.com.trier.spring.models.Driver;
import br.com.trier.spring.repositories.DriverRepository;
import br.com.trier.spring.services.DriverService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    private void findByNameEqualsExists(Driver driver) {
        Driver search = driverRepository.findByNameEqualsIgnoreCase(driver.getName());
        if (driver.getName() == null || driver.getCountry() == null) {
            throw new IntegrityViolation("Null driver");
        }
        if (search != null && search.getId() != driver.getId()) {
            throw new IntegrityViolation("Driver already registered");
        }
    }

    @Override
    public Driver insert(Driver driver) {
        findByNameEqualsExists(driver);
        return driverRepository.save(driver);
    }

    @Override
    public Driver update(Driver driver) {
        findByNameEqualsExists(driver);
        findById(driver.getId());
        return driverRepository.save(driver);
    }

    @Override
    public void delete(Integer id) {
        Driver driver = findById(id);
        driverRepository.delete(driver);
    }

    @Override
    public List<Driver> listAll() {
        List<Driver> list = driverRepository.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFound("No drivers registered");
        }
        return list;
    }

    @Override
    public Driver findById(Integer id) {
        return driverRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Driver %s does not exist".formatted(id)));
    }

    @Override
    public List<Driver> findByNameStartsWithIgnoreCase(String name) {
        List<Driver> list = driverRepository.findByNameStartsWithIgnoreCase(name);
        if (list.isEmpty()) {
            throw new ObjectNotFound("No drivers registered with this name");
        }
        return list;
    }

    @Override
    public List<Driver> findByCountryOrderByNameDesc(Country country) {
        List<Driver> list = driverRepository.findByCountryOrderByNameDesc(country);
        if (list.isEmpty()) {
            throw new ObjectNotFound("No drivers registered in the country: %s".formatted(country.getName()));
        }
        return list;
    }

    @Override
    public List<Driver> findByTeamOrderByNameDesc(Team team) {
        List<Driver> list = driverRepository.findByTeamOrderByNameDesc(team);
        if (list.isEmpty()) {
            throw new ObjectNotFound("No drivers registered in the team: %s".formatted(team.getTeamName()));
        }
        return list;
    }
}
