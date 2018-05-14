package es.uca.wolidays.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Incidencia;
import es.uca.wolidays.backend.repositories.IncidenciaRepository;

@Service
public class IncidenciaService {

	@Autowired
	private IncidenciaRepository repo;
	
	public Incidencia guardar(Incidencia incidencia) {
		return repo.save(incidencia);
	}
	
	public Optional<Incidencia> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public void eliminar(Incidencia incidencia) {
		repo.delete(incidencia);
	}
	
	public Iterable<Incidencia> incidencias() {
		return repo.findAll();
	}
}
