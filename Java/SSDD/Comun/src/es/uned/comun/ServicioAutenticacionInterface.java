/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface ServicioAutenticacionInterface extends Remote {
	
	public boolean autenticarCliente(String nombre, int id) throws RemoteException;
	
	public int registrarCliente(String nombre) throws RemoteException;
	
	public boolean autenticarRepositorio(int idRepo) throws RemoteException;
	
	public int registrarRepositorio() throws RemoteException;

}