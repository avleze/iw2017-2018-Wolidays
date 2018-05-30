package es.uca.wolidays.backend.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Penalizacion implements Serializable{

	private static final long serialVersionUID = 3606673148089012789L;
	
	public enum Motivo {
		Modificacion, Cancelacion
	}
	
	public enum TipoUsuario {
		Huesped, Anfitrion
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer minNoches;
	private Integer maxNoches;
	private Integer minDiasAntelacion;
	private Integer maxDiasAntelacion;
	private Float porcentajeCargo;
	
	@Enumerated(EnumType.STRING)
	private Motivo motivo;
	
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipoUsr;
	
	public Motivo getMotivo() {
		return motivo;
	}
	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	public TipoUsuario getTipoUsuario() {
		return tipoUsr;
	}
	public void setTipoUsuario(TipoUsuario tipoUsr) {
		this.tipoUsr = tipoUsr;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
