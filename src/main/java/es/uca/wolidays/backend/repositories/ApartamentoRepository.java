package es.uca.wolidays.backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uca.wolidays.backend.entities.Apartamento;

@Repository
public interface ApartamentoRepository extends CrudRepository<Apartamento, Integer> {
	
	@EntityGraph(value = "Apartamento.apartamentoConImagenes", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE apt.ubicacion.direccion LIKE %:ubicacion% OR apt.ubicacion.pais LIKE %:ubicacion% OR apt.ubicacion.ciudad LIKE %:ubicacion%")
	List<Apartamento> findByUbicacion(String ubicacion);
	@EntityGraph(value = "Apartamento.apartamentoConImagenes", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE apt.ubicacion.ciudad LIKE %:ciudad%")
	List<Apartamento> findByCiudad(String ciudad);
	List<Apartamento> findByNumDormitorios(Integer numDormitorios);
	List<Apartamento> findByNumCamas(Integer numCamas);
	List<Apartamento> findByPrecioEstandarBetween(Double min, Double max);
	
	@EntityGraph(value = "Apartamento.apartamentoConImagenes", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE (apt.ubicacion.direccion LIKE %:ubicacion% OR apt.ubicacion.pais LIKE %:ubicacion% OR apt.ubicacion.ciudad LIKE %:ubicacion%) AND apt.precioEstandar BETWEEN :min AND :max")
	List<Apartamento> filterByUbicacionAndPrecio(String ubicacion, Double min, Double max);
	@EntityGraph(value = "Apartamento.apartamentoConImagenes", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt, Reserva res WHERE (apt.id = res.apartamento AND (apt.ubicacion.direccion LIKE %:ubicacion% OR apt.ubicacion.pais LIKE %:ubicacion% OR apt.ubicacion.ciudad LIKE %:ubicacion%) AND res.fechaInicio < :fechainicio AND res.fechaFin > :fechafin) OR apt.id NOT IN (SELECT res.apartamento FROM Reserva res)")
	List<Apartamento> filterByUbicacionAndFecha(String ubicacion, LocalDate fechainicio, LocalDate fechafin);
	
	@EntityGraph(value = "Apartamento.apartamentoConOfertas", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE apt.id = ?1")
	Optional<Apartamento> findByIdWithOfertas(Integer pk);
	
	@EntityGraph(value = "Apartamento.apartamentoConReservas", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE apt.id = ?1")
	Optional<Apartamento> findByIdWithReservas(Integer pk);
	
	@EntityGraph(value = "Apartamento.apartamentoConImagenes", type = EntityGraphType.LOAD)
	@Query("SELECT apt FROM Apartamento apt WHERE apt.id = ?1")
	Optional<Apartamento> findByIdWithImages(Integer pk);
}
