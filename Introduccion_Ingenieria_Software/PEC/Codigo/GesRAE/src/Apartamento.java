import java.util.ArrayList;
import java.util.List;

public class Apartamento {
	public enum TipoApartamento {
		BASICO, NORMAL, LUJO
	}

	public String ref_apartamento;
	public TipoApartamento categoria;
	public List<Reserva> reservas;

	public Apartamento(char categoria) {

		switch (Character.toUpperCase(categoria)) {
		case 'B':
			this.categoria = TipoApartamento.BASICO;
			break;
		case 'N':
			this.categoria = TipoApartamento.NORMAL;
			break;
		case 'L':
			this.categoria = TipoApartamento.LUJO;
			break;
		default:
			throw new IllegalArgumentException("Categoría no válida: " + categoria);
		}
		this.reservas = new ArrayList<Reserva>();
		this.ref_apartamento = null;

	}

	public List<Fecha> getDiasReservados(int mes, int anno) {
		List<Fecha> diasReservados = new ArrayList<>();
		for (Reserva r : reservas) {
			diasReservados.addAll(r.getDiasReservados(mes, anno, reservas));
		}
		return diasReservados;
	}

	public TipoApartamento cambioChar(char categoria) {
		switch (categoria) {
		case 'B':
			return TipoApartamento.BASICO;
		case 'N':
			return TipoApartamento.NORMAL;
		case 'L':
			return TipoApartamento.LUJO;
		default:
			throw new IllegalArgumentException("Categoría no válida: " + categoria);
		}

	}

	public boolean isDisponible(Fecha fechainicio, int duracion) {
		Fecha fechaTotal = fechainicio.sumarDias(duracion);

		for (Fecha fecha = fechainicio; !fecha.isAfter(fechaTotal); fecha = fecha.sumarDias(1)) {
			for (Reserva r : reservas) {
				if (r.getFechaInicio() == fecha) {
					return false;
				}
			}
		}

		return true;

	}

	// Getter
	public String getRef_apartamento() {
		return ref_apartamento;
	}

	public Reserva getReserva(Fecha fechaInicio, int duracion) {
		for (Reserva r : reservas) {
			if ((r.getFechaInicio() == fechaInicio) && (r.getDuracion() == duracion)) {
				return r;
			}
		}

		return null;

	}

	public TipoApartamento getCategoria() {
		return categoria;
	}

	// Setter
	public void setRef_apartamento(String ref_apartamento) {
		this.ref_apartamento = ref_apartamento;
	}

	public void setReserva(int idEdificio, Fecha fechaInicio, int duracion) {
		Fecha fechaFin = fechaInicio.sumarDias(duracion);
		String n_reserva = ref_apartamento.replace("APT", "").replace("N", "").replace("0", "") + "/"
				+ fechaInicio.getAño();
		Reserva reservaNueva = new Reserva(n_reserva, fechaInicio, fechaFin, duracion);
		reservas.add(reservaNueva);
	}

	public void setCategoria(TipoApartamento categoria) {
		this.categoria = categoria;
	}

}
