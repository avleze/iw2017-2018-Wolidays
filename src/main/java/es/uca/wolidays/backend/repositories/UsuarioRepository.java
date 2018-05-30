package es.uca.wolidays.backend.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
	Usuario findByUsername(String username);
	Usuario findByCorreo(String correo);
	
	@EntityGraph(value = "Usuario.usuarioConReservas", type = EntityGraphType.LOAD)
	@Query("SELECT u FROM Usuario u WHERE username = ?1")
	Usuario findByUsernameWithReservas(String username);
	
	@EntityGraph(value = "Usuario.usuarioConApartamentos", type = EntityGraphType.LOAD)
	@Query("SELECT u FROM Usuario u WHERE username = ?1")
	Usuario findByUsernameWithApartamentos(String username);
	
	@EntityGraph(value = "Usuario.usuarioConApartamentosYReservas", type = EntityGraphType.LOAD)
	@Query("SELECT u FROM Usuario u WHERE username = ?1")
	Usuario findByUsernameWithApartamentosAndReservas(String username);
}
