/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.io.Serializable;

public class Mensaje implements Serializable {


	private static final long serialVersionUID = -3189496939089104087L;
	private String cuerpo, remitente;

	public Mensaje(String cuerpo, String remitente) {
		this.cuerpo = cuerpo;
		this.remitente = remitente;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getRemitente() {
		return remitente;
	}
}
