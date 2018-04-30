package es.uca.wolidays.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.repositories.ReservaRepository;

@Service
public class ReservaService {
	@Autowired
	ReservaRepository repo;
	
	public void eliminar(Reserva reserva) {
		repo.delete(reserva);
	}
	
	public void eliminarTodos(Iterable<Reserva> reservas){
		repo.deleteAll(reservas);
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
}
