package es.uca.wolidays.backend.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import es.uca.wolidays.backend.entities.Reserva.Estado;


@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name="Apartamento.apartamentoConOfertas", attributeNodes=@NamedAttributeNode("ofertas")),
	@NamedEntityGraph(name="Apartamento.apartamentoConReservas", attributeNodes=@NamedAttributeNode("reservas"))
})
public class Apartamento implements Serializable {

	private static final long serialVersionUID = -1392912192298652747L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 100)
	private String contacto;
	private String descripcion;
	private Integer numDormitorios;
	private Integer numCamas;
	private Boolean aireAcondicionado;
	private Double precioEstandar;
	
	@OneToMany(mappedBy="apartamento")
	private Set<Reserva> reservas;
	
	@ManyToOne
	@JoinColumn(name="USR_ID")
	private Usuario propietario;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="apartamento")
	private List<Oferta> ofertas;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Ubicacion ubicacion;
	
	public Ubicacion getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}
	public List<Oferta> getOfertas() {
		return ofertas;
	}
	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}
	public Set<Reserva> getReservas() {
		return reservas;
	}
	
	public Set<Reserva> getReservasPendientes() {
		return reservas.stream()
				.filter(r -> r.getEstado().equals(Estado.Pendiente))
				.collect(Collectors.toSet());
	}
	
	public void setReservas(Set<Reserva> reservas) {
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
	
	@Override
	public String toString() {
		return this.ubicacion.toString();
	}
	
}
