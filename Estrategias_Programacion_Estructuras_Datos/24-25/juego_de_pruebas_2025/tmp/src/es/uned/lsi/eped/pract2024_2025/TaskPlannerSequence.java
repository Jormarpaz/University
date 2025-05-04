package es.uned.lsi.eped.pract2024_2025;

import java.util.Stack;

import es.uned.lsi.eped.DataStructures.*;

public class TaskPlannerSequence implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */

	/* Estructura que almacena las tareas pasadas */
	protected StackIF<TaskIF> pastTasks; 
	/* La estructura que almacena las tareas futuras debe ser una secuencia */
	protected SequenceIF<TaskIF> futureTasks; 

	public TaskPlannerSequence(){
		this.futureTasks = new List<>();
		this.pastTasks = new Stack<>();
	}

	@Override
	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	public void add(String text,int date) {
		TaskIF nuevaTarea = new Task(text, date);
		ordenarListaFutura(nuevaTarea);
	}

	@Override
	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	public void delete(int date) {
		IteratorIF<TaskIF> iterador = iteratorFuture();
		int pos = 0;
		while (iterador.hasNext()) {	
			TaskIF tarea = iterador.getNext();
			if (tarea.getDate() == date){
				futureTasks.remove(pos);
				continue;
			}
			pos++;
			// Observar posible error que se elimine el elemento anterior al que queremos eliminar por creer que el getNext está en la misma posicion que el pos
		}
	}

	@Override
	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	public void move(int origDate,int newDate) {
		IteratorIF<TaskIF> iterador = iteratorFuture();
		int pos = 0;
		while(iterador.hasNext()){
			TaskIF tarea = iterador.getNext();
			if(tarea.getDate() == origDate){
				futureTasks.remove(pos);
				tarea.setDate(newDate);
				ordenarListaFutura(tarea);
				break;
			}
			pos++;
		}
	}

	@Override
	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	public void execute() {
		TaskIF tarea = futureTasks.get(0);
		tarea.setCompleted();
		futureTasks.remove(0);
		pastTasks.push(tarea);
	}

	@Override
	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	public void discard() {
		TaskIF tarea = futureTasks.get(0);
		futureTasks.remove(0);
		pastTasks.push(tarea);
	}

	@Override
	/* Devuelve un iterador de las tareas futuras */
	public IteratorIF<TaskIF> iteratorFuture() {
		return new IteratorIF<>(futureTasks);
	}

	@Override
	/* Devuelve un iterador del histórico de tareas pasadas */
	public IteratorIF<TaskIF> iteratorPast() {
		return new IteratorIF<>(pastTasks);
	}

	private void ordenarListaFutura(TaskIF tarea) {
		int size = futureTasks.size();
		int pos = 0;

		while (pos < size) {
			TaskIF tarea2 = futureTasks.get(pos); 
			if (tarea.compareTo(tarea2) < 0) {
				break;
			}
			pos++;
		}
		futureTasks.insert(pos, tarea);
	}
		
}
