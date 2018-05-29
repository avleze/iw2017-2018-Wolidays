package es.uca.wolidays.backend.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "TransaccionReserva.getTotalBenefits",
		query="select year(t_fecha), sum(tr_beneficio) from transaccion_reserva group by year(t_fecha)")
public class TransaccionReserva extends Transaccion implements Serializable{

	private static final long serialVersionUID = 3983378816379306082L;


	
	@Column(name = "TR_TARJH", length = 16)
	private String tarjetaHuesped;
	
	@Column(name = "TR_CUENTAANF", length = 20)
	private String cuentaAnfitrion;
	
	@Column(name = "TR_PRECIO")
	private Double precio;
	
	@Column(name = "TR_BENEFICIO")
	private Double beneficioEmpresa;
	
	@ManyToOne
	private Reserva reservaAsociada;

	public String getTarjetaHuesped() {
		return tarjetaHuesped;
	}

	public void setTarjetaHuesped(String tarjetaHuesped) {
		this.tarjetaHuesped = tarjetaHuesped;
	}

	public String getCuentaAnfitrion() {
		return cuentaAnfitrion;
	}

	public void setCuentaAnfitrion(String cuentaAnfitrion) {
		this.cuentaAnfitrion = cuentaAnfitrion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getBeneficioEmpresa() {
		return beneficioEmpresa;
	}

	public void setBeneficioEmpresa(Double beneficioEmpresa) {
		this.beneficioEmpresa = beneficioEmpresa;
	}

	public Reserva getReservaAsociada() {
		return reservaAsociada;
	}

	public void setReservaAsociada(Reserva reservaAsociada) {
		this.reservaAsociada = reservaAsociada;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
