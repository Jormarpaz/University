/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioSrOperadorInterface extends Remote {

	public boolean descargarArchivo(String rutaArchivo, String nombreArchivo, String propietario) throws RemoteException;
	
}