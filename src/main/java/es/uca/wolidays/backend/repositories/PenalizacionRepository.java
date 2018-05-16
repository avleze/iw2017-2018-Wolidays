package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Penalizacion;

@Repository
public interface PenalizacionRepository extends CrudRepository<Penalizacion, Integer>{

}
