/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDiscoClienteInterface extends Remote {
    
	public boolean subirArchivo(String rutaDestino, Fichero fichero) throws RemoteException;
	
	public Fichero descargarArchivo(String rutaArchivo, String propietario) throws RemoteException;
	
	public boolean borrarArchivo(String rutaArchivo) throws RemoteException;
}

