package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Apartamento;

public interface ApartamentoRepository extends CrudRepository<Apartamento, Integer>{
	
	List<Apartamento> findByUbicacion(String ubicacion);
	List<Apartamento> findByNumDormitorios(Integer numDormitorios);
	List<Apartamento> findByNumCamas(Integer numCamas);
	List<Apartamento> findByPrecioEstandarBetween(Double min, Double max);
	@Query("SELECT apt FROM Apartamento apt WHERE apt.ubicacion = ?1 AND apt.precioEstandar BETWEEN ?2 AND ?3")
	List<Apartamento> filterByUbicacionAndPrecio(String ubicacion, Double min, Double max);
	@Query("SELECT apt FROM Apartamento apt, Reserva res WHERE (apt.id = res.apartamento AND apt.ubicacion = ?1 AND res.fechaInicio < ?2 AND res.fechaFin > ?3) OR apt.id NOT IN (SELECT res.apartamento FROM Reserva res)")
	List<Apartamento> filterByUbicacionAndFecha(String ubicacion, LocalDate fechainicio, LocalDate fechafin);
}
