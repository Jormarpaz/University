package src.es.uned.lsi.eped.pract2024_2025;


import es.uned.lsi.eped.DataStructures.*;

public class TaskPlannerTree implements TaskPlannerIF{
	/* Estructura que almacena las tareas pasadas */
	protected BSTreeIF<TaskIF> pastTasks;
	/* La estructura que almacena las tareas futuras debe ser un BSTree */
	protected BSTreeIF<TaskIF> futureTasks;
        
        /* Declaración de atributos para almacenar la información del planificador de tareas */
        public TaskPlannerTree() {
            this.pastTasks = new BSTree<>();
            this.futureTasks = new BSTree<>();
        }
        
        
	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
        @Override
	public void add(String text,int date) {
            TaskIF newTask = new Task(text, date);
            futureTasks.add(newTask);
        }

	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
        @Override
	public void delete(int date) {
            TaskIF tarea = new Task("", date);
            futureTasks.remove(tarea);
        }

	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
        @Override
	public void move(int origDate,int newDate) {
            IteratorIF<TaskIF> iterador = iteratorFuture();
            
            while(iterador.hasNext()){
                TaskIF fecha_a_mover = iterador.getNext();
                if (fecha_a_mover.getDate() == origDate){
                    futureTasks.remove(fecha_a_mover);
                    TaskIF nuevaFecha = new Task(fecha_a_mover.getText(),newDate);
                    futureTasks.add(nuevaFecha);
                    break;
                }
            }
        }

	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
        @Override
	public void execute() {
            if (!futureTasks.isEmpty()) {
                TaskIF task = futureTasks.getRoot();
                task.setCompleted();
                futureTasks.remove(task);
                pastTasks.add(task);
            }
        }

	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
        @Override
	public void discard() {
            if (!futureTasks.isEmpty()) {
                TaskIF task = futureTasks.getRoot();
                futureTasks.remove(task);
                pastTasks.add(task);
            }
        }

	/* Devuelve un iterador de las tareas futuras */
        @Override
	public IteratorIF<TaskIF> iteratorFuture() {
            return futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
        }

	/* Devuelve un iterador del histórico de tareas pasadas */
        @Override
	public IteratorIF<TaskIF> iteratorPast() {
            return pastTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
        }
		
}
