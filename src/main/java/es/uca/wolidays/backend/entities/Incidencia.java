package es.uca.wolidays.backend.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Incidencia implements Serializable{

	private static final long serialVersionUID = 8336800537136393316L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	private LocalDate fechaIncidencia;
	private String contacto;
	private String comentario;
	
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private Usuario afectado;
	
	@ManyToOne
	@JoinColumn(name="RSV_ID")
	private Reserva reserva;
	
	public Usuario getAfectado() {
		return afectado;
	}
	public void setAfectado(Usuario afectado) {
		this.afectado = afectado;
	}
	public Reserva getReserva() {
		return reserva;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public LocalDate getFechaIncidencia() {
		return fechaIncidencia;
	}
	public void setFechaIncidencia(LocalDate fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
