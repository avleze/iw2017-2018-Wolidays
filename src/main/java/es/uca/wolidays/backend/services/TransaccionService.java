package es.uca.wolidays.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
	
	public Float obtenerPctBeneficioActual() {
    	
		String filename = "src/main/resources/beneficio";
    	try (Stream<String> stream = Files.lines(Paths.get(filename))) {
    		
    		Optional<String> beneficioStr = stream.findFirst();
        	
    		if(beneficioStr.isPresent())
    			return Float.parseFloat(beneficioStr.get());
 
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
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


