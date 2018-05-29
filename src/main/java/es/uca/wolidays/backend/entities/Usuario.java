package es.uca.wolidays.backend.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name="Usuario.usuarioConApartamentos", attributeNodes =  {
		@NamedAttributeNode(value="apartamentos")
	}),
	@NamedEntityGraph(name="Usuario.usuarioConReservas", attributeNodes =  {
		@NamedAttributeNode(value="reservas")
	}), 
	@NamedEntityGraph(name="Usuario.usuarioConApartamentosYReservas", 
		attributeNodes =  @NamedAttributeNode(value="apartamentos", subgraph = "apartamentos"),
		subgraphs = @NamedSubgraph(name = "apartamentos", attributeNodes = @NamedAttributeNode(value="reservas"))),
	@NamedEntityGraph(name="Usuario.usuarioConApartamentosReservasEImagenes", 
	attributeNodes =  @NamedAttributeNode(value="apartamentos", subgraph = "apartamentos"),
	subgraphs = @NamedSubgraph(name = "apartamentos", attributeNodes = {@NamedAttributeNode(value="reservas"), @NamedAttributeNode(value="imagenes")}))
})
@Table(indexes= {@Index(columnList = "username")})
public class Usuario implements UserDetails, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	private String apellidos;
	
	@Column(unique = true)
	private String correo;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String cuentaBancaria;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usuario_rol",
	joinColumns = @JoinColumn(name = "usr_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	private List<Rol> roles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="propietario")
	private List<Apartamento> apartamentos;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="usuario")
	private List<Reserva> reservas;
	
	@OneToMany(mappedBy="afectado")
	private List<Incidencia> incidencias;
	
	@OneToMany(mappedBy="usuarioAfectado")
	private List<TransaccionPenalizacion> penalizacionesAfectado;
	
	@OneToMany(mappedBy="usuarioPenalizado")
	private List<TransaccionPenalizacion> penalizacionesPenalizado;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCuentaBancaria() {
		return cuentaBancaria;
	}
	public void setCuentaBancaria(String cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}
	public List<Rol> getRoles() {
		return roles;
	}
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	public List<Apartamento> getApartamentos() {
		return apartamentos;
	}
	public void setApartamentos(List<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}
	public List<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	public List<Incidencia> getIncidencias() {
		return incidencias;
	}
	public void setIncidencias(List<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}
	public List<TransaccionPenalizacion> getPenalizacionesAfectado() {
		return penalizacionesAfectado;
	}
	public void setPenalizacionesAfectado(List<TransaccionPenalizacion> penalizacionesAfectado) {
		this.penalizacionesAfectado = penalizacionesAfectado;
	}
	public List<TransaccionPenalizacion> getPenalizacionesPenalizado() {
		return penalizacionesPenalizado;
	}
	public void setPenalizacionesPenalizado(List<TransaccionPenalizacion> penalizacionesPenalizado) {
		this.penalizacionesPenalizado = penalizacionesPenalizado;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();
		for(Rol rol : this.getRoles())
			list.add(new SimpleGrantedAuthority(rol.getNombre()));
		return list;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}
}
