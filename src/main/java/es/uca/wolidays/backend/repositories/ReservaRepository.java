package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Apartamento;
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
	
	@Query("SELECT r FROM Reserva r WHERE r.apartamento = ?1 AND (((?2 BETWEEN r.fechaInicio AND r.fechaFin) OR (?3 BETWEEN r.fechaInicio AND r.fechaFin)) AND r.estado = 'Validada')")
	List<Reserva> findByValidadasAndFechasBetween(Apartamento apto, LocalDate fecha_inicio, LocalDate fecha_fin);
}
