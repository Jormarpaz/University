package es.uned.lsi.eped.pract2024_2025;

import es.uned.lsi.eped.DataStructures.IteratorIF;

public class TaskPlannerSequence implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */

	/* Estructura que almacena las tareas pasadas */
	protected SequenceIF<TaskIF> pastTasks; // Vamos a probar una pila
	/* La estructura que almacena las tareas futuras debe ser una secuencia */
	protected SequenceIF<TaskIF> futureTasks; // Vamos a probar una cola

	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	public void add(String text,int date) {
		TaskIF nuevaTarea = new TaskIF(text, date);
		futureTasks.insert(size()+1,nuevaTarea); // Con esta funcion determino que es una lista
		Iterador<TaskIF> iterador = iteratorFuture();
		ordenarListaFutura(iterador,nuevaTarea);
	}

	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	public void delete(int date) {
		IteratorIF<TaskIF> iterador = iteratorFuture();
		int pos = 0;
		while (iterador.hasNext()) {	
			TaskIF tarea = iterador.getNext();
			if (tarea.date == date){
				futureTasks.remove(pos);
				continue;
			}
			pos++;
			// Observar posible error que se elimine el elemento anterior al que queremos eliminar por creer que el getNext está en la misma posicion que el pos
		}
	}

	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	public void move(int origDate,int newDate) {
		Iterator<TaskIF> iterador = iteratorFuture();
		int pos = 0;
		while(iterador.hasNext()){
			TaskIF tarea = iterador.getNext();
			if(tarea.date == origDate){
				tarea.date = newDate;
				// Tras cambiar la fecha habrá que ordenar la lista de elementos para que coincidan las fechas
				ordenarListaFutura(iterador, tarea);
				continue;
			}
			pos++;
		}
	}

	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	public void execute() {
		TaskIF tarea = futureTasks.get(0);
		tarea.completada = True;
		futureTasks.remove(0);
		pastTasks.push(tarea);
	}

	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	public void discard() {
		TaskIF tarea = futureTasks.get(0);
		tarea.completada = False;
		futureTasks.remove(0);
		pastTasks.push(tarea);
	}

	/* Devuelve un iterador de las tareas futuras */
	public IteratorIF<TaskIF> iteratorFuture() {
		return new ListIterator<>(futureTasks);
	}

	/* Devuelve un iterador del histórico de tareas pasadas */
	public IteratorIF<TaskIF> iteratorPast() {
		return new StackIterator<>(pastTasks);
	}

	private void ordenarListaFutura(IteratorIF<TaskIF> iterador, TaskIF tarea) {
		int pos = 0;
		while(iterador.hasNext()){
			TaskIF tarea2 = iterador.getNext();
			switch(tarea.compareTo(tarea2)) {
				case -1: break;
				case 1: futureTasks.remove(pos); futureTasks.insert(pos+1, tarea); break;
				case 0: if(tarea2.text != tarea.text ){futureTasks.remove(pos+1);} break;
			}
			pos++;
		}
	}
		
}
