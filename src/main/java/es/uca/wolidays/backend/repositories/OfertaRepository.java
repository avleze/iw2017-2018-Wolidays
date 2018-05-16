package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Oferta;

@Repository
public interface OfertaRepository extends CrudRepository<Oferta, Integer>{

	@Query("SELECT o FROM Oferta o WHERE o.fechaInicio >= ?1 AND o.fechaFin <= ?2")
	List<Oferta> findByFechaInicioFechaFin(LocalDate fechaInicio, LocalDate fechaFin);
	List<Oferta> findByPrecioOfertaBetween(Double min, Double max);
	@Query("SELECT o FROM Oferta o WHERE o.apartamento = ?1 AND o.fechaInicio >= ?2 AND o.fechaFin <= ?3")
	List<Oferta> findByApartamentoAndFechaInicioFechaFin(Integer idApartamento ,LocalDate fechaInicio, LocalDate fechaFin);
}
