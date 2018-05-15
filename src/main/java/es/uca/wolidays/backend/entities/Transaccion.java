package es.uca.wolidays.backend.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Transaccion {
	@Id
	@Column(name = "T_ID")
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(name="T_FECHA")
	private LocalDate fecha;
	
	public Transaccion() {
		super();
	}
	

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}



	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


}