package es.uca.wolidays.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.IVA;
import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.repositories.IVARepository;
import es.uca.wolidays.backend.repositories.PenalizacionRepository;

@Service
public class ReglasDeNegocioService {
	
	@Autowired
	PenalizacionRepository penalizacionRepo;
	
	@Autowired
	IVARepository ivaRepo;
	
	public Penalizacion guardar(Penalizacion penalizacion) {
		return penalizacionRepo.save(penalizacion);
	}
	
	public Optional<Penalizacion> buscarPenalizacionPorId(Integer pk) {
		return penalizacionRepo.findById(pk);
	}
	
	public void eliminar(Penalizacion penalizacion) {
		penalizacionRepo.delete(penalizacion);
	}
	
	public Iterable<Penalizacion> penalizaciones() {
		return penalizacionRepo.findAll();
	}
	
	public IVA guardar(IVA iva) {
		return ivaRepo.save(iva);
	}
	
	public Optional<IVA> buscarIVAPorId(Integer pk) {
		return ivaRepo.findById(pk);
	}
	
	public IVA buscarIVAPorPais(String pais) {
		return ivaRepo.findByPais(pais);
	}
	
	public void eliminar(IVA iva) {
		ivaRepo.delete(iva);
	}
	
	public Iterable<IVA> ivas() {
		return ivaRepo.findAll();
	}

}
