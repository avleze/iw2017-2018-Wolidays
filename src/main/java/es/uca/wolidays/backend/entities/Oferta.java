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
public class Oferta implements Serializable{
	
	private static final long serialVersionUID = 3084327633186126314L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Double precioOferta;
	
	@ManyToOne
	@JoinColumn(name="APTO_ID")
	private Apartamento apartamento;
	
	public Apartamento getApartamento() {
		return apartamento;
	}
	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
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
	public Double getPrecioOferta() {
		return precioOferta;
	}
	public void setPrecioOferta(Double precioOferta) {
		this.precioOferta = precioOferta;
	}
	
}
