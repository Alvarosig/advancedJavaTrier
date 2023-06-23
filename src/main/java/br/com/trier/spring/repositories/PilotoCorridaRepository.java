package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.models.PilotoCorrida;

public interface PilotoCorridaRepository extends JpaRepository<PilotoCorrida, Integer> {

	List<PilotoCorrida> findByPlacement (Integer placement);
	
	List<PilotoCorrida> findByCorridaOrderByPlacement(Corrida corrida);
	
	List<PilotoCorrida> findByPilotoOrderByPlacement(Piloto piloto);
}
