/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec2.torresdehanoi;

import java.io.*;
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
            // 1. Validación de los argumentos y asignación de traza
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equals("-t")) {
                    traza = true; // Activar traza
                } else if (arg.equals("-h")) {
                    mostrarAyuda();
                    return;
                } else if (arg.endsWith(".txt")) {
                    if (ficheroEntrada == null) {
                        ficheroEntrada = arg; // Primer archivo que se encuentra es de entrada
                    } else {
                        ficheroSalida = arg; // Segundo archivo es de salida (si existe)
                    }
                } else {
                    throw new IllegalArgumentException("Argumento no válido: " + arg);
                }
            }

            // 2. Leer los parámetros del problema
            int[] parametros;
            if (ficheroEntrada != null && new File(ficheroEntrada).exists()) {
                parametros = leerParametros(ficheroEntrada);
            } else if (ficheroEntrada == null) { // Si no hay fichero de entrada, pedir por consola
                parametros = leerParametrosDesdeConsola();
            } else {
                parametros = leerParametrosDesdeConsola(); // Si no existe el archivo, pedir por consola
            }

            // 3. Resolver el problema
            Solucionador solucionador = new Solucionador(parametros[0], parametros[1], parametros[2], traza);
            String resultado = solucionador.resolver();

            // 4. Escribir resultado en archivo o por consola
            if (ficheroSalida != null) {
                escribirResultado(ficheroSalida, resultado);
            } else {
                System.out.println(resultado); // Si no hay fichero de salida, se imprime por consola
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            mostrarAyuda();
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static int[] leerParametros(String ficheroEntrada) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
        String[] valores = br.readLine().split(" ");
        int origen = Integer.parseInt(valores[0]);
        int destino = Integer.parseInt(valores[1]);
        int numDiscos = Integer.parseInt(valores[2]);
        br.close();
        return new int[]{origen, destino, numDiscos};
    }
    
    private static int[] leerParametrosDesdeConsola() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el poste origen:");
        int origen = scanner.nextInt();
        System.out.println("Introduce el poste destino:");
        int destino = scanner.nextInt();
        System.out.println("Introduce el número de discos:");
        int numDiscos = scanner.nextInt();
        return new int[]{origen, destino, numDiscos};
    }

    private static void escribirResultado(String ficheroSalida, String resultado) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida));
        bw.write(resultado);
        bw.close();
    }

    private static void mostrarAyuda() {
        System.out.println("SINTAXIS: java -jar hanoi.jar [-t] [-h] [fichero entrada] [fichero salida]");
        System.out.println("-t                  Traza el algoritmo");
        System.out.println("-h                  Muestra esta ayuda");
        System.out.println("[fichero entrada]   Nombre del fichero de entrada");
        System.out.println("[fichero salida]    Nombre del fichero de salida");
    }
}