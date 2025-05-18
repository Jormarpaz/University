import java.util.List;
import java.util.ArrayList;

public class Edificio {

	public int id;
	public String nombre;
	public int cont_basico;
	public int cont_normal;
	public int cont_lujo;
	public List<Apartamento> apartamentos;

	public Edificio(int id, String nombre, int b, int n, int l) {

		this.id = id;
		this.nombre = nombre;
		this.cont_basico = b;
		this.cont_normal = n;
		this.cont_lujo = l;
		apartamentos = new ArrayList<Apartamento>();
		actualizarAptos();

	}

	public void actualizarAptos() {
		apartamentos.clear();

		for (Apartamento apto : apartamentos) {
			if (apto.reservas != null) {
				apto.reservas.clear();
			}
		}

		setApartamento('b');
		setApartamento('n');
		setApartamento('l');
	}

	public void setApartamento(char categoria) {
		int cont = 0;
		int i = 0;

		switch (categoria) {
		case 'b':
			cont = cont_basico;
			break;
		case 'n':
			cont = cont_normal;
			break;
		case 'l':
			cont = cont_lujo;
			break;
		}

		while (i < cont) {
			Apartamento apartamento = new Apartamento(categoria);
			apartamentos.add(apartamento);
			apartamento.setRef_apartamento("APT" + this.id + "N" + apartamentos.indexOf(apartamento));
			i++;
		}
	}

	public List<Fecha> searchApartamento(String ref, int mes, int anno) {
		for (Apartamento apto : apartamentos) {
			if (apto.getRef_apartamento().equals(ref)) {
				return apto.getDiasReservados(mes, anno);
			}
		}

		return null;
	}

	public Apartamento getApartamento(Fecha fechaInicio, int duracion, char categoria) {

		for (Apartamento ap : apartamentos) {
			if (ap.getCategoria() == ap.cambioChar(categoria)) {
				if (ap.isDisponible(fechaInicio, duracion)) {
					ap.setReserva(id, fechaInicio, duracion);
					return ap;
				}
			}
		}
		return null;
	}

	public List<Apartamento> getApartamentosDisponibles(Fecha fechaInicio, int duracion, char categoria) {
		List<Apartamento> disponibles = new ArrayList<>();

		for (Apartamento apto : apartamentos) {
			if (apto.isDisponible(fechaInicio, duracion)) {

				switch (categoria) {
				case 'b':
					if (apto.getCategoria() == Apartamento.TipoApartamento.BASICO) {
						disponibles.add(apto);
					}
					break;
				case 'n':
					if (apto.getCategoria() == Apartamento.TipoApartamento.NORMAL) {
						disponibles.add(apto);
					}
					break;
				case 'l':
					if (apto.getCategoria() == Apartamento.TipoApartamento.LUJO) {
						disponibles.add(apto);
					}
					break;
				}
			}
		}

		return disponibles;
	}

	// ===== Getters =====
	public int getContBasico() {
		return cont_basico;
	}

	public int getContNormal() {
		return cont_normal;
	}

	public int getContLujo() {
		return cont_lujo;
	}

	public int getID() {
		return this.id;
	}

	public String getNombre() {
		return nombre;
	}

	// ===== Setters =====
	public void setContBasico(int cont_basico) {
		this.cont_basico = cont_basico;
	}

	public void setContNormal(int cont_normal) {
		this.cont_normal = cont_normal;
	}

	public void setContLujo(int cont_lujo) {
		this.cont_lujo = cont_lujo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setId(int id) {
		this.id = id;
	}

}
