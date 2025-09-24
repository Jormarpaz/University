package es.uned.lsi.eped.pract2024_2025;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import es.uned.lsi.eped.DataStructures.IteratorIF;

public class Main {

	private static final StringBuilder sb = new StringBuilder();
	
	private static void printUsage() {
		System.err.println("Error en los parámetros de entrada:");
		System.err.println("-Primer parámetro: estructura a utilizar (SEQUENCE/TREE)");
		System.err.println("-Segundo parámetro: fichero con las operaciones a realizar sobre el planificador");
		System.err.println("-Tercer parámetro: fichero donde volcar la salida de la ejecución");
	}
	
	/* Comprueba que el fichero de entrada exista y puede ser leido */
	public static Boolean checkInput(String file) {
		File f = new File(file);
		// Se comprueba que el fichero existe y es, realmente, un fichero
		if ( !f.exists() || !f.isFile() ) {
			System.out.println("ERROR: no existe el fichero de entrada "+file+".");
			return false;
		}
		// Se comprueba que el fichero puede ser leido
		if ( !f.canRead() ) {
			System.out.println("ERROR: el fichero de entrada "+file+" no puede leerse.");
			return false;
		}
		return true;
	}
	
	/* Comprueba que se puede crear el fichero de salida */
	public static Boolean checkOutput(String file) {
		File f = new File(file);
		f=f.getAbsoluteFile();
		// Se comprueba que la carpeta para escribir el fichero de salida existe
		if ( !f.getParentFile().exists() ) {
			System.out.println("ERROR: no existe la carpeta del fichero de salida "+f.getParent()+".");
			return false;
		}
		// Se comprueba que la carpeta para escribir el fichero de salida tenga permisos de escritura
		if ( !f.getParentFile().canWrite() ) {
			System.out.println("ERROR: no se puede escribir en la carpeta del fichero de salida "+f.getParent()+".");
			return false;
		}
		// Si el fichero de salida existe...
		if ( f.exists() ) {
			// Se comprueba que sea un fichero
			if ( !f.isFile() ) {
				System.out.println("ERROR: la salida "+file+" no es un fichero.");
				return false;
			}
			// Se comprueba que pueda sobreescribirse
			if ( !f.canWrite() ) {
				System.out.println("ERROR: el fichero de salida "+file+" no puede sobreescribirse");
				return false;
			}
		}
		return true;
	}
	
	/* Convierte un iterador de tareas en cadena de caracteres
	 * @param it: iterador
	 */
	public static String toString(IteratorIF<TaskIF> it){
		StringBuilder result = new StringBuilder();
		while ( it.hasNext() ){
			TaskIF T = it.getNext();
			result.append("<"+T.getText()+","+T.getDate()+","+T.getCompletion()+">");
			if( it.hasNext() ){
				result.append(", ");
			}
		}
		return result.toString();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		if ( args.length != 3 ) {
			printUsage();
			return;
		}
 
		/* Implementacion gestor de tareas. Posibles valores: 
		 *   - SEQUENCE 
		 *   - BTREE
		 */
		String typeTaskPlanner = args[0]; 
		TaskPlannerIF taskPlanner;
		switch (typeTaskPlanner) {
		case "SEQUENCE":
			// Gestor de tareas basado en secuencias
			taskPlanner = new TaskPlannerSequence();
			break;
		case "TREE":
			// Gestor de tareas basado en arboles
			taskPlanner = new TaskPlannerTree();
			break;
		default:
			System.out.println("ERROR: el primer argumento debe ser SEQUENCE o TREE.");
			return;
		}

		// Fichero de entrada
		String input = args[1]; 
		if ( !checkInput(input) ) { return; }	
		
		// Fichero de salida
		String output = args[2];
		if ( !checkOutput(output) ) { return; }

		// Salto de linea
		String lineFeed = System.getProperty("line.separator");
		// Se abre la lectura del fichero de entrada
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "utf-8"));		
		// Se abre la escritura en el fichero de salida
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "utf-8"));

		
		// Calculo porcentajes
        Path path = Paths.get(input);
        long max = Files.lines(path).count();
        double percentage = 0;
		double old_percentage = -2;
		long count = 1;

		String line;
		String result = "";
		long t0 = System.currentTimeMillis();
		while ((line = br.readLine())!=null) {
			// Cada linea del fichero de entrada es una operacion sobre el planificador de tareas
			// Separa la linea en tokens por tabuladores
			StringTokenizer st = new StringTokenizer(line, "\t");
			// El primer token contiene la operacion 
			String op = st.nextToken();
			// Posibles parámetros de operaciones
			String task;
			int date, newDate;
			// Ejecucion de operaciones
			switch(op){
				// Operacion de añadir tarea, requiere dos parámetros
				case "add":
					// Primer parámetro: descripción de la tarea, presente en el segundo token
					task = st.nextToken();
					// Segundo parámetro: fecha de la tarea, presente en el tercer token
					date = Integer.parseInt(st.nextToken());
					taskPlanner.add(task, date);
					result = "Tarea incluida";
					break;
				// Operacion de eliminar tarea, requiere un parámetro
				case "delete":
					// Primer parámetro: fecha de la tarea, presente en el segundo token
					date = Integer.parseInt(st.nextToken());
					taskPlanner.delete(date);
					result = "Tarea eliminada";
					break;
				// Operacion de mover tarea, requiere dos parámetros
				case "move":
					// Primer parámetro: fecha original de la tarea, presente en el segundo token 
					date = Integer.parseInt(st.nextToken());
					// Segundo parámetro: nueva fecha de la tarea, presente en el tercer token
					newDate = Integer.parseInt(st.nextToken());
					taskPlanner.move(date, newDate);
					result = "Tarea movida de fecha";
					break;
				// Operacion de ejecutar tarea, sin parámetros
				case "execute":
					taskPlanner.execute();
					result = "Tarea realizada";
					break;
				// Operacion de descartar tarea, sin parámetros
				case "discard":
					taskPlanner.discard();
					result = "Tarea descartada";
					break;
				// Iterador de tareas futuras, sin parámetros
				case "iteratorFuture":
					result = toString(taskPlanner.iteratorFuture());
					break;
				// Iterador de histórico de tareas pasadas, sin parámetros
				case "iteratorPast":
					result = toString(taskPlanner.iteratorPast());
					break;
			}
			// Se escribe la salida en el fichero de salida 
			bw.write(line+": ");
			bw.write(result);
			bw.write(lineFeed);

			// Impresion porcentaje trabajo
			percentage = (100*count)/max;
			if(percentage - old_percentage >= 2) {
				sb.setLength(0);
				for (int j = 0 ; j < percentage/2; j++) {
					sb.append("#");
				}
				System.out.print("[" + String.format("%-50s", sb.toString()) + "] " +  percentage + "%");
				/* Se imprime un retorno de carro para sobreescribir la barra de progreso de porcentaje */
				System.out.print("\r");
				old_percentage = percentage;
			}
			count++;

			
		} 
		System.out.println();
		long t1 = System.currentTimeMillis() - t0;
		// Muestra por consola el tiempo de ejecucion en milisegundos
		System.out.println(t1+" ms");
		// Se cierra el buffer de lectura
		br.close();
		// Se cierra el buffer de escritura
		bw.close();
	}
}
