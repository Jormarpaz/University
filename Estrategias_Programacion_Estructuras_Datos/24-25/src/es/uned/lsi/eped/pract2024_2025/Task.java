package es.uned.lsi.eped.pract2024_2025;

public class Task implements TaskIF {

	private String text;
        private int date;
        private boolean completed;
	/* Declaración de atributos para almacenar la información de una tarea */

        public Task(String text, int date) {
            this.text = text;
            this.date = date;
            this.completed = false;
        }
        
	/* Marca la tarea como completada */
        @Override
	public void setCompleted() {
            this.completed = true;
        }
	
	/* Devuelve el texto de la tarea */
        @Override
	public String getText() {
            return this.text;
        }

	/* Devuelve la fecha de la tarea */
        @Override
	public int getDate() {
            return this.date;
        }

	/* Devuelve si la tarea ha sido completada o no */
        @Override
	public boolean getCompletion() {
            return this.completed;
        }

	/* Compara la tarea actual con una tarea llamante */
        @Override
	public int compareTo(TaskIF T) {
            return Integer.compare(this.date, T.getDate()); // Resto las fechas
        }

}
