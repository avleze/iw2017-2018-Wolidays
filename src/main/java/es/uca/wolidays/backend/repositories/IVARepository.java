package es.uca.wolidays.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import es.uca.wolidays.backend.entities.IVA;

public interface IVARepository extends CrudRepository<IVA, Integer>{
	
	public IVA findByPais(String pais);

}
