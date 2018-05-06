package es.uca.wolidays.backend.repositories;

import org.junit.Ignore;
import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Reserva;

@Ignore
public interface ReservaRepository extends CrudRepository<Reserva, Integer>{
	
}
