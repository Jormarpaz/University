/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec2.torresdehanoi;

import java.io.*;

/**
 *
 * @author jorge
 */
public class Main {
    public static void main(String[] args) {
        boolean traza = false;

        // -h: Mostrar ayuda
        if (args.length == 0 || args[0].equals("-h")) {
            mostrarAyuda();
            return;
        }

        // -t: Activar trazas
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                traza = true;
            }
        }

        try {
            // Comprobar argumentos
            if (args.length < 2) {
                System.out.println("Uso: java -jar hanoi.jar [-t] [fichero entrada] [fichero salida]");
                return;
            }

            String ficheroEntrada = args[args.length - 2];
            String ficheroSalida = args[args.length - 1];

            // Leer datos del fichero de entrada
            int[] parametros = leerParametros(ficheroEntrada);

            // Resolver Torres de Hanoi
            Solucionador solucionador = new Solucionador(parametros[0], parametros[1], parametros[2], traza);
            String resultado = solucionador.resolver();

            // Escribir resultado en el fichero de salida
            escribirResultado(ficheroSalida, resultado);

        } catch (Exception e) {
            e.printStackTrace();
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