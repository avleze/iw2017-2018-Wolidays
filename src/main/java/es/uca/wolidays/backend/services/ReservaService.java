package es.uca.wolidays.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.repositories.ReservaRepository;

@Service
public class ReservaService {
	@Autowired
	ReservaRepository repo;
	
	void eliminar(Reserva reserva) {
		repo.delete(reserva);
	}
	
	void eliminarTodos(Iterable<Reserva> reservas){
		repo.deleteAll(reservas);
	}
	
	void eliminarPorId(Integer pk){
		repo.deleteById(pk);
	}
	
	Iterable<Reserva> reservas() {
		return repo.findAll();
	}
	
	Iterable<Reserva> reservasPorId(Iterable<Integer> pks) {
		return repo.findAllById(pks);
	}
	
	Reserva guardar(Reserva reserva) {
		return repo.save(reserva);
	}
}
