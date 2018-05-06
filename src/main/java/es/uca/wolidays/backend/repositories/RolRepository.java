package es.uca.wolidays.backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Rol;

public interface RolRepository extends CrudRepository<Rol, Integer>{
	Optional<Rol> findByNombre(String nombre);
}
