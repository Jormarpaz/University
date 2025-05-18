package src.es.uned.lsi.eped.pract2024_2025;

import es.uned.lsi.eped.DataStructures.*;

public class TaskPlannerSequence implements TaskPlannerIF{
	/* Estructura que almacena las tareas pasadas */
	protected SequenceIF<TaskIF> pastTasks;
	/* La estructura que almacena las tareas futuras debe ser una secuencia */
	protected SequenceIF<TaskIF> futureTasks;
        
        /* Declaración de atributos para almacenar la información del planificador de tareas */
        public TaskPlannerSequence(){
            this.pastTasks = new Queue<>();
            this.futureTasks = new List<>();
        }
        
        // ((ListIF<TaskIF>)futureTasks).get(0); hay que hacerle cast a la lista
        

	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
        @Override
	public void add(String text,int date) {
            TaskIF newTask = new Task(text, date);
            int pos = 0;
            IteratorIF<TaskIF> it = futureTasks.iterator();
            while(it.hasNext()) {
                TaskIF task = it.getNext();
                if (task.getDate() >= date) break;
                pos++;
            }
            ((ListIF<TaskIF>)futureTasks).insert(pos, newTask);
        }

	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
        @Override
	public void delete(int date) {
            IteratorIF<TaskIF> it = futureTasks.iterator();
            int pos = 0;
            while(it.hasNext()) {
                TaskIF task = it.getNext();
                if(task.getDate() == date) {
                    ((ListIF<TaskIF>)futureTasks).remove(pos);
                    return;
                }
                pos++;
            }
        }

	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
        @Override
	public void move(int origDate,int newDate) {
            IteratorIF<TaskIF> it = futureTasks.iterator();
            int pos = 0;
            TaskIF taskToMove = null;
            while (it.hasNext()) {
                TaskIF task = it.getNext();
                if (task.getDate() == origDate) {
                    taskToMove = task;
                    ((ListIF<TaskIF>)futureTasks).remove(pos);
                    break;
                }
                pos++;
            }
            if (taskToMove != null) {
                add(taskToMove.getText(), newDate);
            }
        }

	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
        @Override
	public void execute() {
            if (!futureTasks.isEmpty()) {
                TaskIF task = ((ListIF<TaskIF>)futureTasks).get(0);
                task.setCompleted();
                ((ListIF<TaskIF>)pastTasks).insert(pastTasks.size(), task);
                ((ListIF<TaskIF>)futureTasks).remove(0);
            }
        }

	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
        @Override
	public void discard() {
            if (!futureTasks.isEmpty()) {
                TaskIF task = ((ListIF<TaskIF>)futureTasks).get(0);
                ((ListIF<TaskIF>)pastTasks).insert(pastTasks.size(), task);
                ((ListIF<TaskIF>)futureTasks).remove(0);
            }
        }

	/* Devuelve un iterador de las tareas futuras */
        @Override
	public IteratorIF<TaskIF> iteratorFuture() {
            return futureTasks.iterator();
        }

	/* Devuelve un iterador del histórico de tareas pasadas */
        @Override
	public IteratorIF<TaskIF> iteratorPast() {
            return pastTasks.iterator();
        }
		
}
