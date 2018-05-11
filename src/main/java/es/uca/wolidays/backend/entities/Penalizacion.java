package es.uca.wolidays.backend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Penalizacion implements Serializable{

	private static final long serialVersionUID = 3606673148089012789L;
	
	public enum Motivo {
		Modificacion, Cancelacion
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	private Integer minNoches;
	private Integer maxNoches;
	private Integer minDiasAntelacion;
	private Integer maxDiasAntelacion;
	private Float porcentajeCargo;
	
	@Enumerated(EnumType.STRING)
	private Motivo motivo;
	
	@ManyToOne
	@JoinColumn(name="TIPO_USR")
	private Rol tipo_usuario;
	
	public Motivo getMotivo() {
		return motivo;
	}
	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	public Rol getTipo_usuario() {
		return tipo_usuario;
	}
	public void setTipo_usuario(Rol tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Integer getMinNoches() {
		return minNoches;
	}
	public void setMinNoches(Integer minNoches) {
		this.minNoches = minNoches;
	}
	public Integer getMaxNoches() {
		return maxNoches;
	}
	public void setMaxNoches(Integer maxNoches) {
		this.maxNoches = maxNoches;
	}
	public Integer getMinDiasAntelacion() {
		return minDiasAntelacion;
	}
	public void setMinDiasAntelacion(Integer minDiasAntelacion) {
		this.minDiasAntelacion = minDiasAntelacion;
	}
	public Integer getMaxDiasAntelacion() {
		return maxDiasAntelacion;
	}
	public void setMaxDiasAntelacion(Integer maxDiasAntelacion) {
		this.maxDiasAntelacion = maxDiasAntelacion;
	}
	public Float getPorcentajeCargo() {
		return porcentajeCargo;
	}
	public void setPorcentajeCargo(Float porcentajeCargo) {
		this.porcentajeCargo = porcentajeCargo;
	}
}
