package es.uca.wolidays.backend.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;


@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name="Apartamento.apartamentoConOfertas", attributeNodes=@NamedAttributeNode("ofertas")),
	@NamedEntityGraph(name="Apartamento.apartamentoConReservas", attributeNodes=@NamedAttributeNode("reservas"))
})
public class Apartamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 100)
	private String contacto;
	private String descripcion;
	@Column(length = 80)
	private String ubicacion;
	private Integer numDormitorios;
	private Integer numCamas;
	private Boolean aireAcondicionado;
	private Double precioEstandar;
	
	@OneToMany(mappedBy="apartamento")
	private List<Reserva> reservas;
	
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private Usuario propietario;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="apartamento")
	private List<Oferta> ofertas;
	
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public List<Oferta> getOfertas() {
		return ofertas;
	}
	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}
	public List<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	public Usuario getPropietario() {
		return propietario;
	}
	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}
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
	public Integer getNumDormitorios() {
		return numDormitorios;
	}
	public void setNumDormitorios(Integer numDormitorios) {
		this.numDormitorios = numDormitorios;
	}
	public Integer getNumCamas() {
		return numCamas;
	}
	public void setNumCamas(Integer numCamas) {
		this.numCamas = numCamas;
	}
	public Boolean getAireAcondicionado() {
		return aireAcondicionado;
	}
	public void setAireAcondicionado(Boolean aireAcondicionado) {
		this.aireAcondicionado = aireAcondicionado;
	}
	public Double getPrecioEstandar() {
		return precioEstandar;
	}
	public void setPrecioEstandar(Double precioEstandar) {
		this.precioEstandar = precioEstandar;		
	}
	
}
