package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.junit.Ignore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Oferta;

@Ignore
public interface OfertaRepository extends CrudRepository<Oferta, Integer>{

	@Query("SELECT o FROM Oferta o WHERE o.fechaInicio >= ?1 AND o.fechaFin <= ?2")
	List<Oferta> findByFechaInicioFechaFin(LocalDate fechaInicio, LocalDate fechaFin);
	List<Oferta> findByPrecioOfertaBetween(Double min, Double max);
}
