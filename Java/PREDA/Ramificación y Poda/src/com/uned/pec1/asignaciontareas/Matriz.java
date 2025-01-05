/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.io.*;

public class Matriz {

    private final String rutaArchivo;
    private int filas;
    private int columnas;
    private int[][] tabla;

    public Matriz(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        File file = new File(rutaArchivo);
        if (!file.exists()) {
            throw new IllegalArgumentException("El archivo no existe en la ruta especificada");
        }
    }
    
    // Constructor para inicializar directamente con una matriz
    public Matriz(int[][] matrizCostes) {
        this.rutaArchivo = null;
        this.filas = matrizCostes.length;
        this.columnas = matrizCostes[0].length;
        this.tabla = matrizCostes;
    }

    public int[][] obtenerTabla() {
        return tabla;
    }

    public void cargarDesdeArchivo() throws IOException {
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String[] dimensiones = lector.readLine().split(" ");
            if (dimensiones.length != 2) {
                throw new IllegalArgumentException("El archivo no tiene las dimensiones correctamente especificadas.");
            }

            filas = Integer.parseInt(dimensiones[0]);
            columnas = Integer.parseInt(dimensiones[1]);
            tabla = new int[filas][columnas];

            for (int i = 0; i < filas; i++) {
                String[] valores = lector.readLine().split(" ");
                if (valores.length != columnas) {
                    throw new IllegalArgumentException("El numero de columnas no coincide con las dimensiones especificadas.");
                }
                for (int j = 0; j < columnas; j++) {
                    tabla[i][j] = Integer.parseInt(valores[j]);
                }
            }
        }
    }

    public String generarResultado(int[] asignaciones) {
        StringBuilder resultado = new StringBuilder();
        resultado.append(filas).append(" ").append(columnas).append("\n");

        for (int[] fila : tabla) {
            for (int valor : fila) {
                resultado.append(valor).append(" ");
            }
            resultado.append("\n");
        }

        resultado.append("\nAsignaciones optimas:\n");
        // Cambiar de Agente -> Tarea a Tarea -> Agente
        for (int i = 0; i < asignaciones.length; i++) {
            resultado.append("Tarea ").append(i + 1)
                     .append(" -> Agente ").append(asignaciones[i] + 1)
                     .append("\n");
        }

        return resultado.toString();
    }



    public void guardarEnArchivo(String datos, String rutaSalida) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaSalida))) {
            escritor.write(datos);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] fila : tabla) {
            for (int valor : fila) {
                sb.append(valor).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}



