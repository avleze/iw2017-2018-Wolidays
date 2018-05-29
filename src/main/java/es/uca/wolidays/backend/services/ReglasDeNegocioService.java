package es.uca.wolidays.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import es.uca.wolidays.backend.entities.IVA;
import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.entities.Penalizacion.Motivo;
import es.uca.wolidays.backend.entities.Penalizacion.TipoUsuario;
import es.uca.wolidays.backend.entities.Reserva;
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
	
	/**
	 * Método que calcula el coste adicional al modificar/cancelar
	 * una reserva por parte de un anfitrión o huésped.
	 * @param tipoUsr Tipo de usuario afectado (Anfitrión o Huésped).
	 * @param motivo Motivo de la penalizacion (Modificación o Cancelación).
	 * @param reserva Reserva modificada o cancelada.
	 * @return Coste adicional de modificación o cancelación.
	 */
	public Double calcularCosteAdicionalPorPenalizacion(TipoUsuario tipoUsr, Motivo motivo, Reserva reserva) {
		
		Integer nochesTotales = (int) ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
		Integer diasAntelacion = (int) ChronoUnit.DAYS.between(LocalDate.now(), reserva.getFechaFin());
		
		Optional<Penalizacion> penalizacion = penalizacionRepo.findPenalizacionForReserva(tipoUsr, motivo, nochesTotales, diasAntelacion);
		
		if(penalizacion.isPresent())
			return penalizacion.get().getPorcentajeCargo() * reserva.getPrecioFinal();
		
		return null;
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
