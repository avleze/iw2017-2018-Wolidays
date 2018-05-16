package es.uca.wolidays.backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Rol;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer>{
	Optional<Rol> findByNombre(String nombre);
}
