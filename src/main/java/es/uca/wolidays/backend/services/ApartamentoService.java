package es.uca.wolidays.backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.repositories.ApartamentoRepository;
import es.uca.wolidays.backend.repositories.OfertaRepository;

@Service
public class ApartamentoService {

	@Autowired
	private ApartamentoRepository repo;
	@Autowired
	private OfertaRepository ofertasRepo;
	
	public List<Apartamento> buscarPorUbicacion(String ubicacion) {
		return repo.findByUbicacion(ubicacion);
	}
	
	public List<Apartamento> buscarPorCiudad(String ciudad) {
		return repo.findByCiudad(ciudad);
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
	
	public List<Apartamento> filtrarPorUbicacionyPrecioEstandar(String ubicacion, Double min, Double max) {
		return repo.filterByUbicacionAndPrecio(ubicacion, min, max);
	}
	
	public List<Apartamento> filtrarPorUbicacionyFecha(String ubicacion, LocalDate fechaini, LocalDate fechafin) {
		return repo.filterByUbicacionAndFecha(ubicacion, fechaini, fechafin);
	}
	
	public List<Oferta> buscarOfertasEntreFechas(Apartamento apto, LocalDate fechaini, LocalDate fechafin) {
		return ofertasRepo.findByApartamentoAndFechaInicioFechaFin(apto.getId(), fechaini, fechafin);
	}
	
	public Boolean existeOfertasEntreFechas(Apartamento apto, LocalDate fechaini, LocalDate fechafin) {
		return !buscarOfertasEntreFechas(apto, fechaini, fechafin).isEmpty();
	}
	
	public Apartamento guardar(Apartamento apartamento) {
		return repo.save(apartamento);
	}
	
	public Optional<Apartamento> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public Optional<Apartamento> buscarPorIdConOfertas(Integer pk){
		return repo.findByIdWithOfertas(pk);
	}
	
	public Optional<Apartamento> buscarPorIdConReservas(Integer pk){
		return repo.findByIdWithReservas(pk);
	}
	
	public void eliminar(Apartamento apartamento) {
		repo.delete(apartamento);
	}
	
	public Iterable<Apartamento> apartamentos() {
		return repo.findAll();
	}
}
