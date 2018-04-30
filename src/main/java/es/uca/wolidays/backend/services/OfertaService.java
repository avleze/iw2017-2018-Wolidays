package es.uca.wolidays.backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.repositories.OfertaRepository;

@Service
public class OfertaService {

	@Autowired
	private OfertaRepository repo;
	
	public List<Oferta> buscarPorFechaInicioFechaFin(LocalDate fechaInicio, LocalDate fechaFin) {
		return repo.findByFechaInicioFechaFin(fechaInicio, fechaFin);
	}
	
	public List<Oferta> buscarPorPrecio(Double min, Double max){
		return repo.findByPrecioOfertaBetween(min, max);
	}
	
	public Oferta guardar(Oferta oferta) {
		return repo.save(oferta);
	}
	
	public Optional<Oferta> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public void eliminar(Oferta oferta) {
		repo.delete(oferta);
	}
	
	public Iterable<Oferta> ofertas() {
		return repo.findAll();
	}
}
