package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.Corrida;
import br.com.trier.spring.models.Piloto;
import br.com.trier.spring.models.PilotoCorrida;

public interface PilotoCorridaService {
	
	PilotoCorrida findById (Integer id);
    
	PilotoCorrida insert(PilotoCorrida pilotoCorrida);
    
    List<PilotoCorrida> listAll ();
    
    PilotoCorrida update (PilotoCorrida pilotoCorrida);
    
    void delete (Integer id);
    
    List<PilotoCorrida> findByPlacement (Integer placement);
	
	List<PilotoCorrida> findByCorridaOrderByPlacement(Corrida corrida);
	
	List<PilotoCorrida> findByPilotoOrderByPlacement(Piloto piloto);
}
