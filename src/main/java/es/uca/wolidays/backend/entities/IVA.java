package es.uca.wolidays.backend.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IVA implements Serializable{

	private static final long serialVersionUID = -9071397397381602064L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	@Column(unique=true)
	private String pais;
	private Float porcentajeIVA;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public Float getPorcentajeIVA() {
		return porcentajeIVA;
	}
	public void setPorcentajeIVA(Float porcentajeIVA) {
		this.porcentajeIVA = porcentajeIVA;
	}
}
