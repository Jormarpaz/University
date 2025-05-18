import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Fecha {

	public int dia;
	public int mes;
	public int ano;

	public Fecha(int dia, int mes, int anno) {

		this.dia = dia;
		this.mes = mes;
		this.ano = anno;

	}

	public Fecha sumarDias(int dias) {
		int nuevoDia = this.dia;
		int nuevoMes = this.mes;
		int nuevoAno = this.ano;

		while (dias > 0) {
			int diasEnMes = diasEnMes(nuevoMes, nuevoAno);

			if (nuevoDia + dias <= diasEnMes) {
				nuevoDia += dias;
				break;
			} else {
				dias -= (diasEnMes - nuevoDia + 1);
				nuevoDia = 1;
				nuevoMes++;

				if (nuevoMes > 12) {
					nuevoMes = 1;
					nuevoAno++;
				}
			}
		}

		return new Fecha(nuevoDia, nuevoMes, nuevoAno);
	}

	public boolean solapaConMes(Fecha inicio, Fecha fin, int mes, int anno) {

		boolean empiezaEnMes = (inicio.getMes() == mes && inicio.getAño() == anno);
		boolean terminaEnMes = (fin.getMes() == mes && fin.getAño() == anno);

		boolean abarcaMes = (inicio.isBefore(new Fecha(1, mes, anno))
				&& fin.isAfter(new Fecha(diasEnMes(mes, anno), mes, anno)));

		return empiezaEnMes || terminaEnMes || abarcaMes;
	}

	public void agregarDiasReservados(List<Fecha> lista, Fecha inicio, Fecha fin, int mes, int anno) {
		Fecha fechaActual = inicio;

		while (!fechaActual.isAfter(fin)) {
			if (fechaActual.getMes() == mes && fechaActual.getAño() == anno) {
				lista.add(new Fecha(fechaActual.getDia(), mes, anno));
			}
			fechaActual = fechaActual.sumarDias(1);
		}
	}

	public int diasEnMes(int mes, int ano) {
		switch (mes) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return esBisiesto(ano) ? 29 : 28;
		default:
			throw new IllegalArgumentException("Mes inválido: " + mes);
		}
	}

	private boolean esBisiesto(int ano) {
		return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
	}

	public boolean isBefore(Fecha otraFecha) {
		if (this.ano < otraFecha.ano)
			return true;
		if (this.ano > otraFecha.ano)
			return false;

		if (this.mes < otraFecha.mes)
			return true;
		if (this.mes > otraFecha.mes)
			return false;

		return this.dia < otraFecha.dia;
	}

	public boolean isAfter(Fecha otraFecha) {
		return otraFecha.isBefore(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Fecha otraFecha = (Fecha) obj;
		return this.dia == otraFecha.dia && this.mes == otraFecha.mes && this.ano == otraFecha.ano;
	}

	public boolean isValida(int dia, int mes, int anno) {
		try {
			LocalDate fecha = LocalDate.of(anno, mes, dia);
			return true;
		} catch (java.time.DateTimeException e) {
			return false;
		}
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public void setAño(int año) {
		this.ano = año;
	}

	public int getDia() {
		return this.dia;
	}

	public int getMes() {
		return this.mes;
	}

	public int getAño() {
		return this.ano;
	}

	@Override
	public String toString() {
		return dia + "/" + mes + "/" + ano;
	}
}