/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.repositorio;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.Naming;

import es.uned.comun.*;

public class ServicioClOperadorImpl implements ServicioClOperadorInterface {

	@Override
	public boolean subirArchivo(String rutaOrigen, String rutaDestino, String propietario, String nombreArchivo) throws RemoteException {
	    // URL del ServicioDiscoCliente
	    String urlDiscoCliente = "rmi://localhost:1099/ServicioDiscoCliente";
	    ServicioDiscoClienteInterface servicioDiscoCliente;

	    try {
	        servicioDiscoCliente = (ServicioDiscoClienteInterface) Naming.lookup(urlDiscoCliente);
	    } catch (Exception e) {
	    	System.out.println("Error al buscar el servicio de DiscoCliente: " + e.getMessage());
	        return false;  // Retorna false si no se encuentra el servicio
	    }

	    try {
	        // Leer el contenido del archivo desde la ruta origen
	        File archivoOrigen = new File(rutaOrigen);
	        if (!archivoOrigen.exists()) {
	        	System.out.println("El archivo no existe en la ruta especificada.");
	            return false;  // Retorna false si el archivo no existe
	        }
	        
	        Fichero fichero = new Fichero(rutaOrigen, nombreArchivo, propietario);
            
	        // Llamar al ServicioDiscoCliente para guardar el archivo en la ruta destino
	        return servicioDiscoCliente.subirArchivo(rutaDestino,  fichero);
	    } catch (IOException e) {
	    	System.out.println("Error al leer el archivo: " + e.getMessage());
	        return false;  // Retorna false si ocurre un error de I/O
	    }
	}


	@Override
	public boolean borrarArchivo(String rutaArchivo) throws RemoteException {
	    // URL del ServicioDiscoCliente
	    String urlDiscoCliente = "rmi://localhost:1099/ServicioDiscoCliente";
	    ServicioDiscoClienteInterface servicioDiscoCliente = null;

	    try {
	        // Obtener el servicio remoto de DiscoCliente
	        servicioDiscoCliente = (ServicioDiscoClienteInterface) Naming.lookup(urlDiscoCliente);
	    } catch (Exception e) {
	    	System.out.println("Error al buscar el servicio de DiscoCliente: " + e.getMessage());
	        return false;  // Retorna false si no se puede obtener el servicio
	    }

	    // Delegar el borrado al DiscoCliente con la ruta completa del archivo
	    return servicioDiscoCliente.borrarArchivo(rutaArchivo);
	}

}

