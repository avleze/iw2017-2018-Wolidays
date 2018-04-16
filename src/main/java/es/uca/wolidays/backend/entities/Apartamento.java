package es.uca.wolidays.backend.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Apartamento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String contacto;
	private String descripcion;
	private Integer num_dormitorios;
	private Integer num_camas;
	private Boolean aire_acondicionado;
	private Double precio_estandar;
	
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private Usuario propietario;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="apartamento")
	private List<Oferta> ofertas;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getNum_dormitorios() {
		return num_dormitorios;
	}
	public void setNum_dormitorios(Integer num_dormitorios) {
		this.num_dormitorios = num_dormitorios;
	}
	public Integer getNum_camas() {
		return num_camas;
	}
	public void setNum_camas(Integer num_camas) {
		this.num_camas = num_camas;
	}
	public Boolean getAire_acondicionado() {
		return aire_acondicionado;
	}
	public void setAire_acondicionado(Boolean aire_acondicionado) {
		this.aire_acondicionado = aire_acondicionado;
	}
	public Double getPrecio_estandar() {
		return precio_estandar;
	}
	public void setPrecio_estandar(Double precio_estandar) {
		this.precio_estandar = precio_estandar;
	}
	
}
