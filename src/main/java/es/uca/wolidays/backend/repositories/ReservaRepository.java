package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Integer>{
	
}
