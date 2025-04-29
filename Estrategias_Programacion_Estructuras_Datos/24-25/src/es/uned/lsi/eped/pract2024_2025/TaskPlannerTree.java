package es.uned.lsi.eped.pract2024_2025;


import es.uned.lsi.eped.DataStructures.IteratorIF;

public class TaskPlannerTree implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */

	/* Estructura que almacena las tareas pasadas */
	protected ... pastTasks;
	/* La estructura que almacena las tareas futuras debe ser un BSTree */
	protected BSTreeIF<TaskIF> futureTasks;

	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	public void add(String text,int date) {}

	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	public void delete(int date) {}

	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	public void move(int origDate,int newDate) {}

	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	public void execute() {}

	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	public void discard() {}

	/* Devuelve un iterador de las tareas futuras */
	public IteratorIF<TaskIF> iteratorFuture() {}

	/* Devuelve un iterador del histórico de tareas pasadas */
	public IteratorIF<TaskIF> iteratorPast() {}
		
}
