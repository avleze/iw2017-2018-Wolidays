package es.uca.wolidays.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.repositories.ApartamentoRepository;

@Service
public class ApartamentoService {

	@Autowired
	private ApartamentoRepository repo;
	
	public List<Apartamento> buscarPorUbicacion(String ubicacion) {
		return repo.findByUbicacion(ubicacion);
	}
	
	public List<Apartamento> buscarPorNumDormitorios(Integer numDormitorios) {
		return repo.findByNumDormitorios(numDormitorios);
	}
	
	public List<Apartamento> buscarPorNumCamas(Integer numCamas) {
		return repo.findByNumCamas(numCamas);
	}
	
	public List<Apartamento> buscarPorPrecioEstandar(Double min, Double max) {
		return repo.findByPrecioEstandarBetween(min, max);
	}
	
	public Apartamento guardar(Apartamento apartamento) {
		return repo.save(apartamento);
	}
	
	public Optional<Apartamento> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public void eliminar(Apartamento apartamento) {
		repo.delete(apartamento);
	}
	
	public Iterable<Apartamento> apartamentos() {
		return repo.findAll();
	}
}
