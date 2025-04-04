/*
 * Nombre: Jorge
 * Apellidos: Martinez Pazos
 * Correo: jmartinez5741@alumno.uned.es
 * 
 */
package es.uned.comun;

public class Utiles {
	public static final String CODEBASE = "java.rmi.server.codebase";

	public static void setCodeBase(Class<?> c) {
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();

		String path = System.getProperty(CODEBASE);

		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;
		}

		System.setProperty(CODEBASE, ruta);
	}
}
