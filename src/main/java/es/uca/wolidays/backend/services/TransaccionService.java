package es.uca.wolidays.backend.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.TransaccionPenalizacion;
import es.uca.wolidays.backend.entities.TransaccionReserva;
import es.uca.wolidays.backend.repositories.TransaccionPenalizacionRepository;
import es.uca.wolidays.backend.repositories.TransaccionReservaRepository;

@Service
public class TransaccionService {
	
	@Autowired
	private EntityManager em;
	@Autowired
	private TransaccionPenalizacionRepository repoTranPenalizacion;
	@Autowired
	private TransaccionReservaRepository repoTranReserva;
	
	public Optional<TransaccionPenalizacion> buscarTransaccionPenalizacionPorId(Integer pk) {
		return repoTranPenalizacion.findById(pk);
	}
	
	public Optional<TransaccionPenalizacion> buscarTransaccionReservaPorId(Integer pk) {
		return repoTranPenalizacion.findById(pk);
	}
	
	public List<?> obtenerBeneficiosTotales(){
		return em.createNamedQuery("TransaccionReserva.getTotalBenefits")
				.getResultList();
	}
	
	public TransaccionPenalizacion guardar(TransaccionPenalizacion tP) {
		return repoTranPenalizacion.save(tP);
	}
	
	public TransaccionReserva guardar(TransaccionReserva tR) {
		return repoTranReserva.save(tR);
	}
	
	public void eliminarTransaccionPenalizacion(TransaccionPenalizacion tP) {
		repoTranPenalizacion.delete(tP);
	}
	
	public void eliminarTransaccionReserva(TransaccionReserva tR) {
		repoTranReserva.delete(tR);
	}
	public Iterable<TransaccionPenalizacion> transaccionesPenalizaciones(){
		return repoTranPenalizacion.findAll();
	}
	
	public Iterable<TransaccionReserva> transaccionesReservas(){
		return repoTranReserva.findAll();
	}
	
}


