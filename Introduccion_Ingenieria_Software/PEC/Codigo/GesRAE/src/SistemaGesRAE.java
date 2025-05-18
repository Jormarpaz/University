import java.util.List;
import java.util.ArrayList;

public class SistemaGesRAE {

	public List<Edificio> edificios;

	public SistemaGesRAE() {

		edificios = new ArrayList<>();

	}

	public void editarEdificios(int id, String nombre, int nAB, int nAN, int nAL) {
		Edificio edificioExistente = null;

		if ((id <= 5) && (id > 0)) {
			if((nAB + nAN + nAL) < 20) {
				if (nombre.length() < 20) {
					for (Edificio e : edificios) {
						if (e.getID() == id) {
							edificioExistente = e;
							break;
						}
					}

					if (edificioExistente == null) {
						Edificio nuevoEdificio = new Edificio(id, nombre, nAB, nAN, nAL);
						edificios.add(nuevoEdificio);
					} else {
						edificioExistente.setNombre(nombre);
						edificioExistente.setContBasico(nAB);
						edificioExistente.setContNormal(nAN);
						edificioExistente.setContLujo(nAL);
						edificioExistente.actualizarAptos();
					}
				} else {
					System.out.println("Error, el nombre del edificio debe tener menos de 20 caracteres.");
				}
				
			} else {
				System.out.println("Error, la cantidad de apartamentos no debe superar las 20 estancias.");
				
				
			}
			
		} else {
			System.out.println("Error, el id debe estar entre 1 y 5.");
		}
		
	}

	public void listarEdificios() {

		if (!edificios.isEmpty()) {
			for (Edificio edificio : edificios) {

				System.out.printf("%-5d %-20s %-15d %-15d %-15d%n", edificio.id, edificio.nombre, edificio.cont_basico,
						edificio.cont_normal, edificio.cont_lujo);

			}
		} else {
			System.out.println("No hay edificios gestionados.");
		}

	}

	public void validarFechas(Fecha fechaInicio, int id, int dias) {
		Edificio edificio = null;
		String nombre;

		if (fechaInicio.isValida(fechaInicio.getDia(), fechaInicio.getMes(), fechaInicio.getAño())) {
			if (!edificios.isEmpty()) {
				for (Edificio ed : edificios) {
					if (ed.getID() == id) {
						edificio = ed;
					}
				}
				
				if (edificio == null) {
					System.out.println("Error: No se encontró el edificio con ID " + id);
	                return;
				}
				
				nombre = edificio.getNombre();

				System.out.println("El edificio " + nombre + " desde el " + fechaInicio + " y " + dias
						+ " dias de estancia, tendría disponibles: ");
				System.out.println("");

				List<Apartamento> aptosb = edificio.getApartamentosDisponibles(fechaInicio, dias, 'b');
				List<Apartamento> aptosn = edificio.getApartamentosDisponibles(fechaInicio, dias, 'n');
				List<Apartamento> aptosl = edificio.getApartamentosDisponibles(fechaInicio, dias, 'l');

				System.out.println(aptosb.size() + " apartamentos tipo Básico");
				System.out.println(aptosn.size() + " apartamentos tipo Normales");
				System.out.println(aptosl.size() + " apartamentos tipo Lujos");
			} else {
				System.out.println("Todavía no hay edificios registrados.");
			}

		} else {
			System.out.println("Error con la fecha, cerrando...");
		}
	}

	public void realizarReserva(int id, char tipoApto, Fecha fechaEntrada, int dias) {

		String nReserva = null;
		String nombreEdificio = null;
		String nApto = null;
		Fecha fechaSalida = null;

		if (fechaEntrada.isValida(fechaEntrada.getDia(), fechaEntrada.getMes(), fechaEntrada.getAño())) {
			for (Edificio ed : edificios) {
				if (ed.getID() == id) {
					nombreEdificio = ed.getNombre();
					Apartamento apto = ed.getApartamento(fechaEntrada, dias, tipoApto);

					if (apto == null) {
						System.out.println("No hay apartamentos disponibles para las fechas seleccionadas.");
						return;
					}

					nApto = apto.getRef_apartamento();
					Reserva res = apto.getReserva(fechaEntrada, dias);
					nReserva = res.getNReserva();
					fechaSalida = res.calcSalida(fechaEntrada, dias);
				}
			}
		}

		System.out.println("");
		System.out.println("        Datos de la Reserva:        ");
		System.out.println("");
		System.out.println("    Número de la Reserva:" + nReserva);
		System.out.println("    Edificio:" + nombreEdificio + "(Id = " + id + ")");
		System.out.println("    Referencia Apartamento:" + nApto);
		System.out.println("    Fecha Entrada:" + fechaEntrada);
		System.out.println("    Duración estancia:" + dias + "dias");
		System.out.println("    Fecha Salida:" + fechaSalida);
		System.out.println("");

	}

	public List<Fecha> consultarReservaMes(String ref_apto, int mes, int anno) {

		for (Edificio ed : edificios) {
			List<Fecha> diasReservados = ed.searchApartamento(ref_apto, mes, anno);
			if (diasReservados != null) {
				return diasReservados;
			}
		}

		return null;

	}

	public void salir() {
		System.out.println("Saliendo del programa...");
		System.exit(0);
	}
}
