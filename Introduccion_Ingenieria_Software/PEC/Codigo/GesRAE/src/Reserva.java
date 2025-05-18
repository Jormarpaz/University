import java.util.ArrayList;
import java.util.List;

public class Reserva {

	public String n_reserva;
	public Fecha fechaInicio;
	public Fecha fechaFin;
	public int duracion;

	public Reserva(String n_reserva, Fecha fechaInicio, Fecha fechaFin, int duracion) {

		this.n_reserva = n_reserva;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.duracion = duracion;

	}

	public List<Fecha> getDiasReservados(int mes, int anno, List<Reserva> reservas) {
		List<Fecha> diasReservados = new ArrayList<>();

		for (Reserva reserva : reservas) {
			Fecha inicioReserva = reserva.getFechaInicio();
			Fecha finReserva = inicioReserva.sumarDias(reserva.getDuracion() - 1);

			if (inicioReserva.solapaConMes(inicioReserva, finReserva, mes, anno)) {
				inicioReserva.agregarDiasReservados(diasReservados, inicioReserva, finReserva, mes, anno);
			}
		}

		return diasReservados;
	}

	public Fecha calcSalida(Fecha fechaInicio, int duracion) {
		return fechaInicio.sumarDias(duracion);
	}

	// ===== Getters =====
	public String getNReserva() {
		return n_reserva;
	}

	public Fecha getFechaInicio() {
		return fechaInicio;
	}

	public Fecha getFechaFinal() {
		return fechaFin;
	}

	public int getDuracion() {
		return duracion;
	}

	// ===== Setters =====
	public void setNReserva(String n_reserva) {
		this.n_reserva = n_reserva;
	}

	public void setFechaInicio(Fecha fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFinal(Fecha fechaFinal) {
		this.fechaFin = fechaFinal;
	}

	public void setDuracion(int duracion) {
		if (duracion < 0) {
			throw new IllegalArgumentException("La duración no puede ser negativa");
		}
		this.duracion = duracion;
	}

}
