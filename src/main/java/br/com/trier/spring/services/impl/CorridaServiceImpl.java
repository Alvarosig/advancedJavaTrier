package br.com.trier.spring.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Campeonato;
import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Pista;
import br.com.trier.spring.repositories.CorridaRepository;
import br.com.trier.spring.services.CorridaService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class CorridaServiceImpl implements CorridaService{
	
	@Autowired
    private CorridaRepository corridaRepository;
	
	@Override
	public Corrida findById(Integer id) {
		return corridaRepository.findById(id).orElseThrow(() -> new ObjectNotFound ("Corrida %s não existe".formatted(id)));
	}

	@Override
	public Corrida insert(Corrida corrida) {
		return corridaRepository.save(corrida);
	}

	@Override
	public List<Corrida> listAll() {
		List<Corrida> lista = corridaRepository.findAll(); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida cadastrada");
        }
        return lista;
	}

	@Override
	public Corrida update(Corrida corrida) {
		findById(corrida.getId());
        return corridaRepository.save(corrida);
	}

	@Override
	public void delete(Integer id) {
		Corrida corrida = findById(id);
        corridaRepository.delete(corrida); 
	}

	@Override
	public List<Corrida> findByDate(ZonedDateTime date) {
		List<Corrida> lista = corridaRepository.findByDate(date);
	    if (lista.isEmpty()) {
	        throw new ObjectNotFound("Nem uma corrida foi encontrada na data selecionada");
	    }
	    return lista;
	}

	@Override
	public List<Corrida> findByDateBetween(ZonedDateTime date1, ZonedDateTime date2) {
		List<Corrida> lista = corridaRepository.findByDateBetween(date1, date2);
	    if (lista.isEmpty()) {
	        throw new ObjectNotFound("Nem uma corrida foi encontrada entre a data selecionada");
	    }
	    return lista;
	}

	@Override
	public List<Corrida> findByPistaOrderByName(Pista pista) {
		List<Corrida> lista = corridaRepository.findByPistaOrderByName(pista); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida cadastrada na pista: %s".formatted(pista.getName()));
        }
        return lista;
	}

	@Override
	public List<Corrida> findByCampeonatoOrderByName(Campeonato campeonato) {
		List<Corrida> lista = corridaRepository.findByCampeonatoOrderByName(campeonato); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhuma corrida cadastrada no campeonato: %s".formatted(campeonato.getChampDesc()));
        }
        return lista;
	}

}
