package es.uca.wolidays.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.repositories.ReservaRepository;

@Service
public class ReservaService {
	
	@Autowired
	ReservaRepository repo;
	@Autowired
	OfertaService ofertaService;
	
	public void eliminar(Reserva reserva) {
		repo.delete(reserva);
	}
	
	public void eliminarTodos(Iterable<Reserva> reservas){
		repo.deleteAll(reservas);
	}
	
	public Optional<Reserva> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}
	
	public Optional<Reserva> buscarReservaPorIdConTransaccionesReserva(Integer pk) {
		return repo.findByIdWithTransaccionesReserva(pk);
	}
	
	public void eliminarPorId(Integer pk){
		repo.deleteById(pk);
	}
	
	public Iterable<Reserva> reservas() {
		return repo.findAll();
	}
	
	public Iterable<Reserva> reservasPorId(Iterable<Integer> pks) {
		return repo.findAllById(pks);
	}
	
	public Reserva guardar(Reserva reserva) {
		return repo.save(reserva);
	}
	
	/**
	 * Método que calcula el precio final de una reserva,
	 * teniendo en cuenta el precio estándar del apartamento,
	 * y todas las posibles ofertas que puede tener definidas.
	 * @param apto Apartamento que se está reservando
	 * @param inicioReserva Fecha de inicio de la reserva que se está haciendo
	 * @param finReserva Fecha de fin de la reserva que se está haciendo
	 * @return Precio final de una reserva
	 */
	public Double calcularPrecioFinal(Apartamento apto, LocalDate inicioReserva, LocalDate finReserva) {
		Double precioFinal = 0.0;
		
		long numNoches = ChronoUnit.DAYS.between(inicioReserva, finReserva);
		int i = 0;
		
		List<Oferta> ofertasIntermedias = ofertaService.buscarPorFechaInicioFechaFin(inicioReserva, finReserva);
		
		while(i < numNoches) {
			precioFinal += precioAptoDia(ofertasIntermedias, apto.getPrecioEstandar(), inicioReserva.plusDays(i));
			i++;
		}		
		
		return precioFinal;
	}
	
	/**
	 * Método que devuelve el precio de un apartamento en un día concreto
	 * @param ofertas Ofertas disponibles para el apartamento.
	 * @param precioEstandar Precio estándar por dia del apartamento.
	 * @param dia Día del que se quiere saber el precio
	 * @return Precio del apartamento en el día indicado
	 */
	public Double precioAptoDia(List<Oferta> ofertas, Double precioEstandar, LocalDate dia) {
		Double precio = 0.0;
		Boolean diaConOferta = false;
		
		if(!ofertas.isEmpty()) {
			for(Oferta oferta : ofertas) {
				if(!dia.isBefore(oferta.getFechaInicio()) && dia.isBefore(oferta.getFechaFin())) {
					precio = oferta.getPrecioOferta();
					diaConOferta = true;
				}
			}
		}
		
		if(ofertas.isEmpty() || !diaConOferta) {
			precio = precioEstandar;
		}		
		
		return precio;
	}
	
}
