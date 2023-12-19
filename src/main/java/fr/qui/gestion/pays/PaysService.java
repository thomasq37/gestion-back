package fr.qui.gestion.pays;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaysService {
	
	@Autowired
	private PaysRepository paysRepository;
	
	public List<Pays> obtenirTousLesPays(){
		return paysRepository.findAll();
	}
}
