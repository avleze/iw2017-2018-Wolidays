package es.uca.wolidays.backend.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity 
public class TransaccionPenalizacion extends Transaccion implements Serializable{

	private static final long serialVersionUID = 3246479772948814281L;
	
	@Column(name = "TP_CUENTAORIG",length = 20)
	private String cuentaOrigen;
	@Column(name = "TP_CUENTADEST", length = 20)
	private String cuentaDestino;
	@Column(name = "TP_CSTADICIONAL")
	private Double costeAdicional;
	
	@ManyToOne
	private Usuario usuarioAfectado;
	@ManyToOne
	private Usuario usuarioPenalizado;
	
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	public void setCuentaOrigen(String cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
	public String getCuentaDestino() {
		return cuentaDestino;
	}
	public void setCuentaDestino(String cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	public Double getCosteAdicional() {
		return costeAdicional;
	}
	public void setCosteAdicional(Double costeAdicional) {
		this.costeAdicional = costeAdicional;
	}
	public Usuario getUsuarioAfectado() {
		return usuarioAfectado;
	}
	public void setUsuarioAfectado(Usuario usuarioAfectado) {
		this.usuarioAfectado = usuarioAfectado;
	}
	public Usuario getUsuarioPenalizado() {
		return usuarioPenalizado;
	}
	public void setUsuarioPenalizado(Usuario usuarioPenalizado) {
		this.usuarioPenalizado = usuarioPenalizado;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
