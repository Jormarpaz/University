/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.servidor;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.uned.comun.*;

public class ServicioGestorImpl implements ServicioGestorInterface {
	
	private ServicioDatosImpl datos;

    public ServicioGestorImpl(ServicioDatosImpl datos,ServicioClOperadorInterface servicioClOperador, ServicioSrOperadorInterface servicioSrOperador) {
        this.datos = datos;
    }

    @Override
    public boolean subirArchivo(int idCliente, String nombreArchivo, String rutaOrigen) throws RemoteException {
        int idRepositorio = datos.obtenerRepositorioDeCliente(idCliente);
        if (idRepositorio == -1) {
        	System.out.println("No se encontró repositorio asignado para el cliente.");
            return false;  // Retorna false si no se encuentra el repositorio
        }

        // URL del ServicioClOperador en el repositorio
        String urlClOperador = "rmi://localhost:1099/ServicioClOperador";
        ServicioClOperadorInterface servicioClOperador = null; // Definir fuera del try

        try {
            servicioClOperador = (ServicioClOperadorInterface) Naming.lookup(urlClOperador);
        } catch (Exception e) {
            System.out.println("Error al buscar el servicio: " + e.getMessage());
            return false;  // Retorna false si no se encuentra el servicio
        }

        // Construir la ruta en el servidor con el idRepositorio
        String rutaDestino = "../Repositorio/ficheros" + File.separator + idRepositorio + File.separator + idCliente + File.separator + nombreArchivo;
        String propietario = datos.obtenerNombrePorId(idCliente);
        
        // Delegar la subida al ServicioClOperador, pasando la ruta del repositorio
        boolean exito = servicioClOperador.subirArchivo(rutaOrigen, rutaDestino, propietario, nombreArchivo);
        
        if (exito) {
            // Registrar el archivo en ServicioDatos
            Fichero fichero = new Fichero(rutaOrigen, nombreArchivo, String.valueOf(idCliente));
            datos.registrarArchivo(idCliente, fichero);
        }
        return exito;
    }


    @Override
    public boolean descargarArchivo(int idCliente, String nombreArchivo) throws RemoteException {
        int idRepositorio = datos.obtenerRepositorioDeCliente(idCliente);
        if (idRepositorio == -1) {
        	System.out.println("No se encontró repositorio asignado para el cliente.");
            return false;  // Retorna false si no se encuentra el repositorio
        }

        // URL del ServicioSrOperador en el repositorio
        String urlSrOperador = "rmi://localhost:1099/ServicioSrOperador";
        ServicioSrOperadorInterface servicioSrOperador = null; // Definir fuera del try

        try {
            servicioSrOperador = (ServicioSrOperadorInterface) Naming.lookup(urlSrOperador);
        } catch (Exception e) {
        	System.out.println("Error al buscar el servicio: " + e.getMessage());
            return false;  // Retorna false si no se encuentra el servicio
        }

        // Construir la ruta del archivo en el servidor con el idRepositorio
        String rutaArchivo = "../Repositorio/ficheros/" + idRepositorio + "/" + idCliente + "/" + nombreArchivo;

        String propietario = datos.obtenerNombrePorId(idCliente);
        
        // Delegar la descarga al ServicioSrOperador, pasando la ruta del archivo
        boolean exito = servicioSrOperador.descargarArchivo(rutaArchivo, nombreArchivo, propietario);
        
        if (exito) {
            // Incrementar el contador de descargas
            datos.incrementarDescargas(nombreArchivo);
        }

        return exito;
    }


    @Override
    public boolean borrarArchivo(int idCliente, String nombreArchivo) throws RemoteException {
        int idRepositorio = datos.obtenerRepositorioDeCliente(idCliente);
        if (idRepositorio == -1) {
        	 System.out.println("No se encontró repositorio asignado para el cliente.");
             return false;  // Retorna false si no se encuentra el repositorio
         }

        // Suponiendo que ya tienes el idRepositorio correcto
        String urlClOperador = "rmi://localhost:1099/ServicioClOperador";
        ServicioClOperadorInterface servicioClOperador = null; // Definir fuera del try

        try {
            servicioClOperador = (ServicioClOperadorInterface) Naming.lookup(urlClOperador);
        } catch (Exception e) {
        	System.out.println("Error al buscar el servicio: " + e.getMessage());
            return false;  // Retorna false si no se encuentra el servicio
        }

        // Construir la ruta del archivo en el servidor con el idRepositorio
        String rutaArchivo = "../Repositorio/ficheros/" + idRepositorio + "/" + idCliente + "/" + nombreArchivo;

        // Delegar el borrado al ServicioClOperador, pasando la ruta del archivo
        return servicioClOperador.borrarArchivo(rutaArchivo);
    }

    
    @Override
    public List<String> listarClientesRepo(int idRepositorio) throws RemoteException {
    	if (datos.repositorio_clientes.containsKey(idRepositorio)) {
            return datos.repositorio_clientes.get(idRepositorio);
        } else {
            System.out.println("Repositorio no encontrado.");
            return new ArrayList<>();  // Devuelve una lista vacía en caso de error
        }
    }


    @Override
    public List<String> listarFicheros(int idCliente, String nombreCliente) throws RemoteException {
    	// Si no se proporciona el nombre del cliente, se resuelve a partir del idCliente    	
        if (nombreCliente == null) {
        	nombreCliente = datos.obtenerNombrePorId(idCliente);
            if (nombreCliente == null) {
            	System.out.println("ID de cliente inválido.");
                return new ArrayList<>();  // Retorna lista vacía si no se encuentra el cliente
            }
        }
        int idRepositorio = datos.obtenerRepositorioDeCliente(idCliente);
        // Ruta donde están almacenados los ficheros del cliente
        File carpetaCliente = new File("../Repositorio/ficheros/" + idRepositorio + "/" + idCliente + "/");

        // Comprueba si la carpeta existe y lista los archivos
        if (carpetaCliente.exists() && carpetaCliente.isDirectory()) {
            // Lista de archivos en la carpeta
            String[] archivos = carpetaCliente.list();
            if (archivos != null) {
                // Convertimos el array a una lista
                return Arrays.asList(archivos);
            }
        }
        // Si no se encuentran ficheros o la carpeta no existe, devuelve una lista vacía
        return new ArrayList<>();
    }
    
    @Override
    public List<String> listarFicherosRepo(String nombreCliente) throws RemoteException {
    	// Obtener el ID del cliente a partir del nombre
        int idCliente = datos.obtenerIdPorNombre(nombreCliente);
        if (idCliente == -1) {  // Si el ID es -1, el cliente no existe
            System.out.println("No se encontró el cliente con el nombre: " + nombreCliente);
            return new ArrayList<>();  // Retorna lista vacía si no se encuentra el cliente
        }

        // Obtener el ID del repositorio asignado al cliente
        int idRepositorio = datos.obtenerRepositorioDeCliente(idCliente);
        if (idRepositorio == -1) {
            System.out.println("No se encontró repositorio asignado para el cliente: " + nombreCliente);
            return new ArrayList<>();  // Retorna lista vacía si no se encuentra el repositorio
        }

        // Ruta donde están almacenados los ficheros del cliente
        File carpetaCliente = new File("../Repositorio/ficheros/" + idRepositorio + "/" + idCliente + "/");

        // Comprueba si la carpeta existe y lista los archivos
        if (carpetaCliente.exists() && carpetaCliente.isDirectory()) {
            // Lista de archivos en la carpeta
            String[] archivos = carpetaCliente.list();
            if (archivos != null) {
                // Convertimos el array a una lista
                return Arrays.asList(archivos);
            }
        }

        // Si no se encuentran ficheros o la carpeta no existe, devuelve una lista vacía
        return new ArrayList<>();
    }


    @Override
    public List<String> listarClientesServer() throws RemoteException {
    	// Devuelve todos los nombres de clientes
        List<String> clientes = new ArrayList<>(datos.nombre_id.keySet());
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
        	System.out.println("Clientes registrados: " + clientes);
        }
        return clientes;
    }
    
    @Override
    public List<String> listarClientesCliente() throws RemoteException {
    	// Devuelve todos los nombres de clientes
        List<String> clientes = new ArrayList<>(datos.nombre_id.keySet());
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        }
        return clientes;
    }


    @Override
    public List<Integer> listarRepositorios() throws RemoteException {
    	// Devuelve todas las claves del mapa repositorio_clientes (IDs de repositorios)
        List<Integer> repositorios = new ArrayList<>(datos.repositorio_clientes.keySet());
        if (repositorios.isEmpty()) {
            System.out.println("No hay repositorios registrados.");
        } else {
        	System.out.println("Repositorios registrados: " + repositorios);
        }
        
        return repositorios;
    }


    @Override
    public Map<Integer, List<String>> listarRelaciones() throws RemoteException {
    	// Devuelve el mapa completo de relaciones repositorio-clientes
        Map<Integer, List<String>> relaciones = datos.repositorio_clientes;
        if (relaciones.isEmpty()) {
            System.out.println("No hay relaciones repositorio-cliente registradas.");
        } else {
        	System.out.println("Relaciones registradas: " + relaciones);
        }
        return relaciones;
    }
    
    @Override
    public int obtenerNumeroDescargas(String nombreArchivo) throws RemoteException {
        return datos.obtenerNumeroDescargas(nombreArchivo);
    }
}