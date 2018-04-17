package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
	Usuario findByUsername(String username);
	Usuario findByCorreo(String correo);
}
