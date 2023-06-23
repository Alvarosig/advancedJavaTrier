package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.models.PilotoCorrida;
import br.com.trier.spring.repositories.PilotoCorridaRepository;
import br.com.trier.spring.services.PilotoCorridaService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class PilotoCorridaServiceImpl implements PilotoCorridaService {
	
	@Autowired
    private PilotoCorridaRepository repository;
	
	@Override
	public PilotoCorrida findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound ("Piloto/Corrida %s não existe".formatted(id)));
	}

	@Override
	public PilotoCorrida insert(PilotoCorrida pilotoCorrida) {
		return repository.save(pilotoCorrida);
	}

	@Override
	public List<PilotoCorrida> listAll() {
		List<PilotoCorrida> lista = repository.findAll(); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto/corrida cadastrado");
        }
        return lista;
	}

	@Override
	public PilotoCorrida update(PilotoCorrida pilotoCorrida) {
		 findById(pilotoCorrida.getId());
	     return repository.save(pilotoCorrida);
	}

	@Override
	public void delete(Integer id) {
		 PilotoCorrida pilotoCorrida = findById(id);
	     repository.delete(pilotoCorrida); 
	}

	@Override
	public List<PilotoCorrida> findByPlacement(Integer placement) {
		List<PilotoCorrida> lista = repository.findByPlacement(placement); 
        if (lista.isEmpty()) {
            throw new ObjectNotFound("Nenhum piloto cadastrado na colocação: %s".formatted(placement));
        }
        return lista;
	}

	@Override
	public List<PilotoCorrida> findByCorridaOrderByPlacement(Corrida corrida) {
		List<PilotoCorrida> lista = repository.findByCorridaOrderByPlacement(corrida);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto cadastrado na corrida com o id: %s".formatted(corrida.getId()));
		}
		return lista;
	}

	@Override
	public List<PilotoCorrida> findByPilotoOrderByPlacement(Piloto piloto) {
		List<PilotoCorrida> lista = repository.findByPilotoOrderByPlacement(piloto);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto cadastrado nesta posição com o id: %s".formatted(piloto.getId()));
		}
		return lista;
	}
	
}
