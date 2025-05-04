package es.uned.lsi.eped.pract2024_2025;

public class Task implements TaskIF {

	public boolean completado;
	public String texto;
	public int date;

	/* Declaración de atributos para almacenar la información de una tarea */
	/* Constructor de la clase Task */
	public Task(String texto, int date) {
		this.texto = texto;
		this.date = date;
		this.completado = false; // Inicialmente no completado
	}

	@Override
	/* Marca la tarea como completada */
	public void setCompleted() {
		this.completado = true;
	}
	
	@Override
	/* Devuelve el texto de la tarea */
	public String getText() {
		return this.texto;
	}

	@Override
	/* Devuelve la fecha de la tarea */
	public int getDate() {
		return this.date;
	}

	public void setDate(int date){
		this.date = date;
	}

	@Override
	/* Devuelve si la tarea ha sido completada o no */
	public boolean getCompletion() {
		return this.completado;
	}

	/* Compara la tarea actual con una tarea llamante */
	public int compareTo(TaskIF T) {
		if (this.date > T.date) {
			return 1; // num > 0 si la tarea llamante es posterior a T
		} else if (this.date < T.date) {
			return -1; // num < 0 si la tarea llamante es anterior a T
		} else {
			return 0; // num = 0 si tienen la misma fecha
		}
	}

}
