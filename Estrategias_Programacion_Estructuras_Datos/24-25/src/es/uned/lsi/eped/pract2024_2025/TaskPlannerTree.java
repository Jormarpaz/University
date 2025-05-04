package es.uned.lsi.eped.pract2024_2025;

import es.uned.lsi.eped.DataStructures.*;

public class TaskPlannerTree implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */

	/* Estructura que almacena las tareas pasadas */
	protected BSTree pastTasks;
	/* La estructura que almacena las tareas futuras debe ser un BSTree */
	protected BSTreeIF<TaskIF> futureTasks;

	public TaskPlannerTree() {
		this.futureTasks = new BSTree();
		this.pastTasks = new BSTree();
	}

	@Override
	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	public void add(String text,int date) {
		TaskIF tarea = new Task(text,date);
		futureTasks.add(tarea);
	}

	@Override
	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	public void delete(int date) {
		IteratorIF<TaskIF> iterador = iteratorFuture();
		while(iterador.hasNext()){
			TaskIF tarea = iterador.getNext();
			if (tarea.getDate() == date){
				futureTasks.remove(tarea);
			}
		}
	}

	@Override
	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	public void move(int origDate,int newDate) {
		IteradorIF<TaskIF> iterador = iteradorFuture();
		while(iterador.hasNext()){
			TaskIF tarea = iterador.getNext();
			if (tarea.getDate() == origDate){
				futureTasks.remove(tarea);
				tarea.setDate(newDate);
				futureTasks.add(tarea);
			}
		}
	}

	@Override
	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	public void execute() {
		TaskIF tarea = futureTasks.getRoot();
		tarea.setCompleted();
		futureTasks.remove(tarea);
		pastTasks.add(tarea);
	}

	@Override
	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	public void discard() {
		TaskIF tarea = futureTasks.getRoot();
		futureTasks.remove(tarea);
		pastTasks.add(tarea);
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
		
}
