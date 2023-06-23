package br.com.trier.spring.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Championship;
import br.com.trier.spring.repositories.ChampionshipRepository;
import br.com.trier.spring.services.ChampionshipService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class ChampionshipServiceImpl implements ChampionshipService {

    @Autowired
    private ChampionshipRepository repository;

    private void validateYear(Championship championship) {
        if (championship.getYear() == null) {
            throw new IntegrityViolation("Year cannot be null");
        }
        int currentYear = LocalDateTime.now().getYear();
        if (championship.getYear() < 1990 || championship.getYear() > currentYear + 1) {
            throw new IntegrityViolation("Invalid year");
        }
    }

    @Override
    public Championship findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFound("Championship not found"));
    }

    @Override
    public Championship insert(Championship championship) {
        validateYear(championship);
        return repository.save(championship);
    }

    @Override
    public List<Championship> listAll() {
        List<Championship> championships = repository.findAll();
        if (championships.isEmpty()) {
            throw new ObjectNotFound ("No championships found");
        }
        return championships;
    }

    @Override
    public Championship update(Championship championship) {
        validateYear(championship);
        findById(championship.getId());
        return repository.save(championship);
    }

    @Override
    public void delete(Integer id) {
        Championship championship = findById(id);
        repository.delete(championship);
    }

    @Override
    public List<Championship> findByChampDescStartingWithIgnoreCase(String champDesc) {
        List<Championship> championships = repository.findByChampDescStartingWithIgnoreCase(champDesc);
        if (championships.isEmpty()) {
            throw new ObjectNotFound("No championships found");
        }
        return championships;
    }

    @Override
    public List<Championship> findByYearBetweenOrderByYearAsc(Integer startYear, Integer endYear) {
        List<Championship> championships = repository.findByYearBetweenOrderByYearAsc(startYear, endYear);
        if (championships.isEmpty()) {
            throw new ObjectNotFound("No championships found");
        }
        return championships;
    }

    @Override
    public List<Championship> findByYear(Integer year) {
        List<Championship> championships = repository.findByYear(year);
        if (championships.isEmpty()) {
            throw new ObjectNotFound("No championships found");
        }
        return championships;
    }
}
