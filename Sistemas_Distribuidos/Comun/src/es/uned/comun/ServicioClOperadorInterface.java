/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */

package es.uned.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioClOperadorInterface extends Remote{

	public boolean subirArchivo(String rutaOrigen, String rutaDestino, String propietario, String nombreArchivo) throws RemoteException;

	public boolean borrarArchivo(String nombreArchivo) throws RemoteException;
	
}
