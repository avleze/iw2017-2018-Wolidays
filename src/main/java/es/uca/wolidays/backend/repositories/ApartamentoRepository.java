package es.uca.wolidays.backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.Apartamento;

public interface ApartamentoRepository extends CrudRepository<Apartamento, Integer>{
	
	List<Apartamento> findByUbicacion(String ubicacion);
	List<Apartamento> findByNumDormitorios(Integer numDormitorios);
	List<Apartamento> findByNumCamas(Integer numCamas);
	List<Apartamento> findByPrecioEstandarBetween(Double min, Double max);
}
