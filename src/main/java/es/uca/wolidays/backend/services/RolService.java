package es.uca.wolidays.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Rol;
import es.uca.wolidays.backend.repositories.RolRepository;

@Service
public class RolService {

	@Autowired
	private RolRepository repo;
	
	
	public Rol guardar(Rol rol) {
		return repo.save(rol);
	}
	
	public Optional<Rol> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public void eliminar(Rol rol) {
		repo.delete(rol);
	}
	
	public Iterable<Rol> roles() {
		return repo.findAll();
	}
}
