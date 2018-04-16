package es.uca.wolidays.backend.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Oferta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date fechaInicio;
	private Date fechaFin;
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
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Double getPrecioOferta() {
		return precioOferta;
	}
	public void setPrecioOferta(Double precioOferta) {
		this.precioOferta = precioOferta;
	}
	
}
