package es.uca.wolidays.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.entities.Penalizacion.Motivo;
import es.uca.wolidays.backend.entities.Penalizacion.TipoUsuario;

@Repository
public interface PenalizacionRepository extends CrudRepository<Penalizacion, Integer>{

	@Query("SELECT pen FROM Penalizacion pen WHERE pen.tipoUsr = ?1 AND pen.motivo = ?2 "
			+ "AND pen.minNoches <= ?3 AND pen.maxNoches >= ?3 "
				+ "AND pen.minDiasAntelacion <= ?4 AND pen.maxDiasAntelacion >= ?4")
	Optional<Penalizacion> findPenalizacionForReserva(TipoUsuario tipoUsr, Motivo motivo, Integer nochesTotales, Integer diasAntelacion);
}
