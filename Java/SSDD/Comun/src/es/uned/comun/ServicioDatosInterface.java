/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface ServicioDatosInterface extends Remote {
	
	public int obtenerRepositorioDeCliente(int idCliente) throws RemoteException;
	
	public void registrarArchivo(int idCliente, Fichero fichero) throws RemoteException;
	
	public void incrementarDescargas(String nombreArchivo) throws RemoteException;
	
	public int obtenerNumeroDescargas(String nombreArchivo) throws RemoteException;
	
	public String obtenerNombrePorId(int idCliente) throws RemoteException;
	
	public int obtenerIdPorNombre(String nombreCliente);
}