package es.uned.lsi.eped.pract2024_2025;

public interface TaskIF extends Comparable<TaskIF>{
	
	/* Marca la tarea como completada */
	public void setCompleted();
	
	/* Devuelve el texto de la tarea */
	public String getText();

	/* Devuelve la fecha de la tarea */
	public int getDate();

	/* Devuelve si la tarea ha sido completada o no */
	public boolean getCompletion();

}