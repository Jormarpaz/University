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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import es.uned.comun.*;

public class Repositorio {
	
	private int idRepo;
	private ServidorInterface servidor;
	
	// Constructor
	public Repositorio() throws RemoteException {
	    try {
	        // Conexión al servidor
	        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	        servidor = (ServidorInterface) registry.lookup("Servidor");
	        System.out.println("Conexión al servidor establecida correctamente.");
	        
	        // Inicialización del servicio de operador de repositorio y disco
	        ServicioClOperadorInterface servicioClOperador = new ServicioClOperadorImpl();
	        ServicioSrOperadorInterface servicioSrOperador = new ServicioSrOperadorImpl();
	        
	        // Exportar objetos remotos (RMI)
	        ServicioClOperadorInterface remoteClOperador = (ServicioClOperadorInterface) UnicastRemoteObject.exportObject(servicioClOperador, 0);
	        ServicioSrOperadorInterface remoteSrOperador = (ServicioSrOperadorInterface) UnicastRemoteObject.exportObject(servicioSrOperador, 0);

	        // Registrar los servicios en el RMI Registry
	        registry.rebind("ServicioClOperador", remoteClOperador);
	        registry.rebind("ServicioSrOperador", remoteSrOperador);
	        
	        System.out.println("Servicios del repositorio registrados correctamente.");
	    } catch (Exception e) {
	        System.out.println("Error al conectar con el servidor: " + e.getMessage());
	        throw new RemoteException("No se pudo establecer conexión con el servidor.");
	    }
	}


    public static void main(String[] args) throws Exception {
    	try {
    		// Crear la carpeta principal de repositorios
            crearCarpetaRepositorios();
            Repositorio repositorio = new Repositorio(); // Crea una instancia del repositorio
            repositorio.login(); // Llama al método login
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void login() throws RemoteException {
		int opt = 0;
		
		do {
			opt = Gui.menu("Bienvenid@", new String[] {  "Registrar un nuevo repositorio.",
														 "Autenticarse en el repositorio (hacer login).",
														 "Salir"});
					
			switch(opt) {
				case 0 : registrar(); break;
				case 1 : autenticar(); 
		        		 if (idRepo != 0) {	 
		        			 break;  
		        		 }
		        		 break;
				case 2 : System.out.println("Saliendo del sistema...");break;	
			}
			
			
		}
		while (opt != 2);
		
	}
    
    private void gui() throws RemoteException {
		int opt = 0;
		
		if (idRepo == -1) {
		    System.out.println("Debe autenticarse antes de realizar esta acción.");
		    login();  // Redirigir al login si no está autenticado
		    return;
		}
		
		do {
			opt = Gui.menu("Menu Principal",new String[] {	"Listar Clientes.",
															"Listar ficheros del Cliente.",
															"Salir"});
			
			switch (opt) {
				case 0: listarclientes(); break;
				case 1: listarficheros(); break;
				case 2: System.out.println("Saliendo del sistema...");
						login();
						break;
			}
		}
		while (opt != 2);
	}
    
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

    private void registrar() throws RemoteException {
        idRepo = servidor.registrarRepositorio(); // Registra el repositorio en el servidor
        if (idRepo != 0) {
            System.out.println("Registro exitoso. Su ID único es: " + idRepo);
        } else {
            System.out.println("El registro ha fallado.");
        }
    }
	
    private void autenticar() throws RemoteException {
        String input = Gui.input("Autenticarse", "Introduzca su ID único: ");
        try {
            int idRepositorio = Integer.parseInt(input);
            boolean autenticado = servidor.autenticarRepositorio(idRepositorio);
            if (autenticado) {
                idRepo = idRepositorio;  // Guarda el ID autenticado
                System.out.println("Autenticación exitosa. ID activo: " + idRepo);
                gui();
            } else {
                System.out.println("Error: ID de repositorio no válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, introduzca un número válido.");
        }
    }
    
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void listarclientes() throws RemoteException {
        // Llama al servidor para obtener los clientes asociados a este repositorio
        List<String> clientes = servidor.listarClientesRepo(idRepo); // Reemplazamos miSesion por idRepo

        // Muestra los clientes en la consola
        System.out.println("Clientes asociados al repositorio " + idRepo + ":");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes asociados a este repositorio.");
        } else {
            for (String cliente : clientes) {
                System.out.println("- " + cliente);
            }
        }
    }


    private void listarficheros() throws RemoteException {
        String nombre = Gui.input("Listar Ficheros", "Introduzca el nombre del usuario: ");
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre del usuario no puede estar vacío.");
            return;
        }

        // Llama al servidor para obtener los ficheros del cliente
        List<String> ficheros = servidor.listarFicherosRepo(nombre); 

        // Muestra los ficheros en la consola
        System.out.println("Ficheros del cliente " + nombre + ":");
        if (ficheros.isEmpty()) {
            System.out.println("No hay ficheros para este cliente.");
        } else {
            for (String fichero : ficheros) {
                System.out.println("- " + fichero);
            }
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean recibirArchivo(String cliente, String nombreArchivo, long tamaño) throws RemoteException {
	    // Crea la carpeta del cliente si no existe
	    File carpetaCliente = new File("ficheros/" + cliente);
	    if (!carpetaCliente.exists()) {
	        carpetaCliente.mkdirs();
	    }

	    // Simula recibir y guardar el archivo (en este caso, crea un archivo vacío)
	    try {
	        File archivo = new File(carpetaCliente, nombreArchivo);
	        if (archivo.exists()) {
	            System.out.println("El archivo ya existe. Sobrescribiendo...");
	        }
	        boolean creado = archivo.createNewFile();
	        return creado || archivo.exists(); // Éxito si el archivo se crea o ya existía
	    } catch (IOException e) {
	        System.out.println("Error al guardar el archivo: " + e.getMessage());
	        return false;
	    }
	}
	
	// Método que crea la carpeta principal para los repositorios si no existe
	 	private static void crearCarpetaRepositorios() {
	 	    File carpetaRepositorios = new File("ficheros");
	 	    if (!carpetaRepositorios.exists()) {
	 	        boolean creada = carpetaRepositorios.mkdirs();  // Crea la carpeta 'ficheros'
	 	        if (creada) {
	 	            System.out.println("Carpeta 'ficheros' creada con éxito.");
	 	        } else {
	 	            System.out.println("No se pudo crear la carpeta 'ficheros'.");
	 	        }
	 	    }
	 	}
	
}
