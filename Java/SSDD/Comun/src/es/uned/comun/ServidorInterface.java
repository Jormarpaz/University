/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServidorInterface extends Remote {
	
	public int registrarCliente(String nombre) throws RemoteException;
	
	public boolean autenticarCliente(String nombre, int id) throws RemoteException;
	
	public int registrarRepositorio() throws RemoteException;
	
	public boolean autenticarRepositorio(int idRepositorio) throws RemoteException;
	
	public boolean subirArchivo(int idCliente, String nombreArchivo, String ruta) throws RemoteException;
	
	public boolean descargarArchivo(int idCliente, String nombreArchivo) throws RemoteException;
	
	public int obtenerNumeroDescargas(String nombreArchivo) throws RemoteException;
	
	public boolean borrarArchivo(int idCliente, String nombreArchivo) throws RemoteException;
	
	public List<String> listarFicheros(int idCliente, String nombreCliente) throws RemoteException;
	
	public List<String> listarFicherosRepo(String nombreCliente) throws RemoteException;
	
	public List<String> listarClientesRepo(int idRepositorio) throws RemoteException;
	
	public List<String> listarClientesServer() throws RemoteException;
	
	public List<String> listarClientesCliente() throws RemoteException;
	
	public List<Integer> listarRepositorios() throws RemoteException;
	
	public Map<Integer, List<String>> listarRelaciones() throws RemoteException;
	
}