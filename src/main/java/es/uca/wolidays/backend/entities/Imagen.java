package es.uca.wolidays.backend.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.vaadin.ui.Image;

@Entity
public class Imagen implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8707920022252584286L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		
	@Lob
	@Column(name="imagen")
	private byte[] imagen;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] i) {
		this.imagen = i;
	}

}
