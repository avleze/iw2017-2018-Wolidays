package es.uca.wolidays.backend.repositories;

import java.util.Optional;

import org.junit.Ignore;
import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Rol;

@Ignore
public interface RolRepository extends CrudRepository<Rol, Integer>{
	Optional<Rol> findByNombre(String nombre);
}
