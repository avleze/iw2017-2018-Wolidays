package es.uca.wolidays.backend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Reserva implements Serializable {

	public enum Estado { 
		Pendiente, Validada, Anulada, Rechazada
	}
	
	private static final long serialVersionUID = 4788271250558192695L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Double precioFinal;
	@Column(length = 100)
	private String contacto;
	private String comentario;
	@Column(length = 16)
	private String tarjeta_huesped;
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	@ManyToOne
	@JoinColumn(name="APTO_ID")
	private Apartamento apartamento;
	
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private Usuario usuario;

	@OneToMany(mappedBy="reserva")
	private List<Incidencia> incidencias;
	
	@OneToMany(mappedBy="reservaAsociada")
	private List<TransaccionReserva> transaccionesReserva;
	
	public List<Incidencia> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(List<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Double getPrecioFinal() {
		return precioFinal;
	}

	public void setPrecioFinal(Double precioFinal) {
		this.precioFinal = precioFinal;
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

	public String getTarjeta() {
		return tarjeta_huesped;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta_huesped = tarjeta;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
