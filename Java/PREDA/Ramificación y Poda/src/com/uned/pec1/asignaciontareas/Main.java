/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author jorge
 */
public class Main {

    public static void main(String[] args) {
        boolean traza = false;
        String ficheroEntrada = null;
        String ficheroSalida = null;

        try {
            // Procesar argumentos
            for (String arg : args) {
                if (arg.equals("-t")) {
                    traza = true;
                } else if (arg.equals("-h")) {
                    mostrarAyuda();
                    return;
                } else if (arg.endsWith(".txt")) {
                    if (ficheroEntrada == null) {
                        ficheroEntrada = arg;
                    } else if (ficheroSalida == null) {
                        ficheroSalida = arg;
                    } else {
                        throw new IllegalArgumentException("Demasiados archivos proporcionados.");
                    }
                } else {
                    throw new IllegalArgumentException("Argumento no válido: " + arg);
                }
            }

            // Leer parámetros
            int[][] matrizCostes;
            if (ficheroEntrada != null) {
                matrizCostes = leerParametros(ficheroEntrada);
            } else {
                matrizCostes = leerParametrosDesdeConsola();
            }

            // Ejecutar el algoritmo
            String resultado = AsignadorTareas.ejecutarAlgoritmo(matrizCostes, traza);

            // Guardar o mostrar el resultado
            if (ficheroSalida != null) {
                escribirResultado(ficheroSalida, resultado);
            } else {
                System.out.println(resultado);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            mostrarAyuda();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
    
    // Leer los parametros con archivo
    private static int[][] leerParametros(String ficheroEntrada) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada))) {
            // Leer las dimensiones de la matriz desde la primera línea
            String[] dimensiones = br.readLine().split(" ");
            int filas = Integer.parseInt(dimensiones[0]);
            int columnas = Integer.parseInt(dimensiones[1]);

            // Crear la matriz de costes
            int[][] matriz = new int[filas][columnas];

            // Leer las filas de la matriz
            for (int i = 0; i < filas; i++) {
                String[] valores = br.readLine().split(" ");
                for (int j = 0; j < columnas; j++) {
                    matriz[i][j] = Integer.parseInt(valores[j]);
                }
            }

            return matriz;
        } catch (IOException e) {
            System.out.println("No se encontro el archivo de entrada, por lo que se procedera a pedir los datos por consola.");
            return leerParametrosDesdeConsola();
        }
    }
    
    // Leer los parametros sin archivo
    private static int[][] leerParametrosDesdeConsola() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el numero de agentes de la matriz:");
        int filas = scanner.nextInt();

        System.out.println("Introduce el numero de tareas de la matriz:");
        int columnas = scanner.nextInt();

        int[][] matriz = new int[filas][columnas];
        System.out.println("Introduce los valores de la matriz (fila por fila):");

        for (int i = 0; i < filas; i++) {
            System.out.println("Introduce los valores de la fila " + (i + 1) + " separados por espacios:");
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = scanner.nextInt();
            }
        }

        return matriz;
    }
    
    private static void escribirResultado(String ficheroSalida, String resultado) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida))) {
            bw.write(resultado);
        }
    }
    
    private static void mostrarAyuda() {
        System.out.println("SINTAXIS: java -jar tareas.jar [-t][-h] [fichero entrada] [fichero salida]");
        System.out.println("-t    Traza del algoritmo.");
        System.out.println("-h    Muestra esta ayuda.");
        System.out.println("[fichero entrada]   Archivo con la tabla de costes.");
        System.out.println("[fichero salida]    Archivo para guardar los resultados (opcional).");
    }
}


