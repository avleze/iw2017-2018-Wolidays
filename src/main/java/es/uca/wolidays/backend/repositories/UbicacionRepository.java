package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Ubicacion;

@Repository
public interface UbicacionRepository extends CrudRepository<Ubicacion, Integer>{

}
