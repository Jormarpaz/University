import java.util.List;
import java.util.Scanner;

import java.time.LocalDate;

public class GUI {

	public SistemaGesRAE sistema;
	public int pantalla_actual;

	public static void main(String[] args) throws Exception {
		GUI gui = new GUI();
		gui.gestionarFlujoPantallas();
	}

	public GUI() {

		pantalla_actual = 0;
		sistema = new SistemaGesRAE();

	}

	public void gestionarFlujoPantallas() {

		switch (pantalla_actual) {
		case 0:
			showPantallaPrincipal();
			break;
		case 1:
			showPantallaE();
			break;
		case 2:
			showPantallaL();
			break;
		case 3:
			showPantallaA();
			break;
		case 4:
			showPantallaR();
			break;
		case 5:
			showPantallaM();
			break;
		case 6:
			sistema.salir();
			break;
		}
	}

	public void showPantallaPrincipal() {

		System.out.println("GesRAE: Gestión de Reservas Apartamentos-Edificios");
		System.out.println("");
		System.out.println("    Editar Edificio                     (Pulsar E)");
		System.out.println("    Listar Edificio                     (Pulsar L)");
		System.out.println("    Apartamentos Disponibles            (Pulsar A)");
		System.out.println("    Reservar Apartamento                (Pulsar R)");
		System.out.println("    Reservas Mensuales Apartamento      (Pulsar M)");
		System.out.println("    Salir                               (Pulsar S)");

		String letra = leerEntradaUsuario();
		System.out.println("Letra elegida: " + letra);
		switch (letra.toUpperCase()) {
		case "E":
			this.pantalla_actual = 1;
			break;

		case "L":
			this.pantalla_actual = 2;
			break;

		case "S":
			this.pantalla_actual = 6;
			break;

		case "A":
			this.pantalla_actual = 3;
			break;

		case "R":
			this.pantalla_actual = 4;
			break;

		case "M":
			this.pantalla_actual = 5;
			break;

		default:
			break;
		}

		gestionarFlujoPantallas();

	}

	public String leerEntradaUsuario() {
		return new Scanner(System.in).nextLine();
	}

	public void showPantallaE() {
		String letra;
		int idEdificio;
		String nombreEdificio;
		int nAptosBasicos;
		int nAptosNormales;
		int nAptosLujo;
		do {
			System.out.println("Editar Edificio:");
			System.out.println("");
			System.out.print("    Identificador(número entre 1 y 5)? ");
			idEdificio = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Nombre(entre 1 y 20 caracteres)? ");
			nombreEdificio = leerEntradaUsuario();
			System.out.print("    Número de Apartamentos Básicos? ");
			nAptosBasicos = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Número de Apartamentos Normales? ");
			nAptosNormales = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Número de Apartamentos de Lujo? ");
			nAptosLujo = Integer.parseInt(leerEntradaUsuario());
			System.out.println("IMPORTANTE: Esta opción borra los datos anteriores.");
			System.out.println("Son correctos los nuevos datos (S/N)? ");
			letra = leerEntradaUsuario().toLowerCase();
		} while ((letra.equals("n")) || !(letra.equals("s")));
		sistema.editarEdificios(idEdificio, nombreEdificio, nAptosBasicos, nAptosNormales, nAptosLujo);
		pantalla_actual = 0;
		gestionarFlujoPantallas();
	}

	public void showPantallaL() {
		char letra;
		do {
			System.out.printf("%-5s %-15s %-15s %-15s %-15s%n", "ID", "Nombre", "Aptos Básicos", "Aptos Normales",
					"Aptos de Lujo");
			sistema.listarEdificios();

			System.out.println("Para volver al menu principal introduzca la tecla S");
			letra = leerEntradaUsuario().charAt(0);
			letra = Character.toUpperCase(letra);
		} while (letra != 'S');
		pantalla_actual = 0;
		gestionarFlujoPantallas();
	}

	public void showPantallaA() {
		Fecha fecha;
		int idEdificio;
		int fechadia;
		int fechames;
		int fechaanno;
		int dias;

		System.out.println("Apartamentos Disponibles:");
		System.out.println("");
		System.out.print("    Identificador de Edificio? ");
		idEdificio = Integer.parseInt(leerEntradaUsuario());
		System.out.print("    Fecha Entrada: Día? ");
		fechadia = Integer.parseInt(leerEntradaUsuario());
		System.out.print("    Fecha Entrada: Mes? ");
		fechames = Integer.parseInt(leerEntradaUsuario());
		System.out.print("    Fecha Entrada: Año? ");
		fechaanno = Integer.parseInt(leerEntradaUsuario());
		System.out.print("    Dias de duracion de la estancia? ");
		dias = Integer.parseInt(leerEntradaUsuario());

		fecha = new Fecha(fechadia, fechames, fechaanno);
		sistema.validarFechas(fecha, idEdificio, dias);

		pantalla_actual = 0;
		gestionarFlujoPantallas();

	}

	public void showPantallaR() {

		String letra;
		int idEdificio;
		char tipoApto;
		int fechadia;
		int fechames;
		int fechaanno;
		int dias;

		Fecha fechaEntrada;

		do {
			System.out.println("Reservar Apartamento:");
			System.out.println("");
			System.out.print("    Identificador de Edificio? ");
			idEdificio = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Tipo de Apartamento(B-Básico/N-Normal/L-Lujo)? ");
			tipoApto = Character.toUpperCase(leerEntradaUsuario().charAt(0));
			System.out.print("    Fecha Entrada: Día? ");
			fechadia = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Fecha Entrada: Mes? ");
			fechames = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Fecha Entrada: Año? ");
			fechaanno = Integer.parseInt(leerEntradaUsuario());
			System.out.print("    Dias de duracion de la estancia? ");
			dias = Integer.parseInt(leerEntradaUsuario());

			fechaEntrada = new Fecha(fechadia, fechames, fechaanno);
			sistema.realizarReserva(idEdificio, tipoApto, fechaEntrada, dias);
			System.out.println("Es correcta la operación(S/N)");
			letra = leerEntradaUsuario().toLowerCase();
		} while ((letra.equals("n")) || !(letra.equals("s")));

		pantalla_actual = 0;
		gestionarFlujoPantallas();

	}

	public void showPantallaM() {
		String referenciaApto;
		String nombreEdificio = null;
		String letra;
		int mes;
		int anno;

		do {
			System.out.println("Reservas Mensuales Apartamento: ");
			System.out.print("Referencia Apartamento? ");
			referenciaApto = leerEntradaUsuario();
			System.out.print("Selección Mes? ");
			mes = Integer.parseInt(leerEntradaUsuario());
			System.out.print("Selección Año? ");
			anno = Integer.parseInt(leerEntradaUsuario());

			boolean aptoEncontrado = false;
			for (Edificio e : sistema.edificios) {
				for (Apartamento a : e.apartamentos) {
					if (a.getRef_apartamento().equals(referenciaApto)) {
						nombreEdificio = e.getNombre();
						aptoEncontrado = true;
						break;
					}
				}
				if (aptoEncontrado)
					break;
			}

			if (!aptoEncontrado) {
				System.out.println("No se encontró el apartamento con referencia: " + referenciaApto);
				System.out.print("\n\nMostrar otro mes (S/N)? ");
				letra = leerEntradaUsuario().toLowerCase();
				continue;
			}

			List<Fecha> diasReservados = sistema.consultarReservaMes(referenciaApto, mes, anno);
			boolean hayReservas = diasReservados != null && !diasReservados.isEmpty();

			for (Edificio e : sistema.edificios) {
				for (Apartamento a : e.apartamentos) {
					if (a.getRef_apartamento().equals(referenciaApto)) {
						nombreEdificio = e.getNombre();
					}
				}
			}
			LocalDate primerDia = LocalDate.of(anno, mes, 1);
			int diasEnMes = primerDia.lengthOfMonth();
			int diaSemana = primerDia.getDayOfWeek().getValue() - 1;

			System.out.println("Estado Mensual Apartamento: " + referenciaApto);
			System.out.println("Edificio: " + nombreEdificio);
			System.out.printf("%n%s %d%n", primerDia.getMonth(), anno);

			
			System.out.println("L  M  X  J  V  S  D");

			for (int i = 0; i < diaSemana; i++) {
				System.out.print("   ");
			}

			for (int dia = 1; dia <= diasEnMes; dia++) {
				Fecha fechaActual = new Fecha(dia, mes, anno);
				boolean reservado = hayReservas && diasReservados.stream().anyMatch(f -> f.equals(fechaActual));

				if (reservado) {
			        System.out.print("R  ");
			    } else if(dia < 10) {
			        System.out.printf("%d  ", dia);
			    } else {
			    	System.out.printf("%d ", dia);
			    }

				if ((dia + diaSemana) % 7 == 0) {
					System.out.println();
				}
			}

			System.out.print("\n\nMostrar otro mes (S/N)? ");
			letra = leerEntradaUsuario().toLowerCase();
		} while (letra.equals("s"));

		pantalla_actual = 0;
		gestionarFlujoPantallas();
	}
	
}
