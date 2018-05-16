package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.TransaccionReserva;

@Repository
public interface TransaccionReservaRepository extends CrudRepository<TransaccionReserva, Integer>{

}
