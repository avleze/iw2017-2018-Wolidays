package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Reserva;

@Repository
public interface ReservaRepository extends CrudRepository<Reserva, Integer>{
	
	Optional<Reserva> findById(Integer pk);
	
	@EntityGraph(value = "Reserva.reservaConTransaccionesReserva", type = EntityGraphType.LOAD)
	@Query("SELECT r FROM Reserva r WHERE r.id = ?1")
	Optional<Reserva> findByIdWithTransaccionesReserva(Integer pk);
	
	@EntityGraph(value = "Reserva.reservaConIncidencias", type = EntityGraphType.LOAD)
	@Query("SELECT r FROM Reserva r WHERE r.id = ?1")
	Optional<Reserva> findByIdWithIncidencias(Integer pk);
	
	@Query("SELECT r FROM Reserva r, Apartamento apt WHERE r.apto_id = ?1 AND r.fecha_inicio >= ?2 AND r.fecha_fin <= ?3")
	List<Reserva> findByFechasBetween(Integer aptID, LocalDate fecha_inicio, LocalDate fecha_fin);
}
