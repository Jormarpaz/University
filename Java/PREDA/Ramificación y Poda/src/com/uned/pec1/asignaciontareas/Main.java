/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.io.*;

/**
 *
 * @author jorge
 */
public class Main {
    public static void main(String[] args) {
        boolean traza = false;
        
        //-h
        if (args.length == 0 || args[0].equals("-h")) {
            mostrarAyuda();
            return;
        }
        
        //-t
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                traza = true;
            }
        }
        
        
        
        try {
            //Comprobar argumentos
            if (args.length < 2){
                System.out.println("Uso: java -jar tareas.jar [fichero entrada] [fichero salida]");
                return;
            }
            
            String ficheroEntrada = args[args.length -2];
            String ficheroSalida = args[args.length -1];
            
            //Leer la tabla de costes
            int[][] tablaCostes = leerTablaCostes(ficheroEntrada);
            
            //Calcular la asignación óptima
            AsignadorTareas asignador = new AsignadorTareas(tablaCostes, traza);
            int[][] asignacion = asignador.calcularAsignacionOptima();
            
            //Escribir la salida
            escribirResultado(ficheroSalida, asignacion);
            
        } catch(Exception e) {
            e.printStackTrace();
        }    
    }
    
    private static int[][] leerTablaCostes(String ficheroEntrada) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
        String[] dimensiones = br.readLine().split(" ");
        int filas = Integer.parseInt(dimensiones[0]);
        int columnas = Integer.parseInt(dimensiones[1]);
        
        int[][] tabla = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            String[] valores = br.readLine().split(" ");
            for (int j = 0; j < columnas; j++) {
                tabla[i][j] = Integer.parseInt(valores[j]);
            }
        }
        br.close();
        return tabla; 
    }
        
    private static void escribirResultado(String ficheroSalida, int[][] asignacion) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida));
        for (int[] par : asignacion) {
            bw.write(par[0] + " " + par[1]);
            bw.newLine();
        }
        bw.close();
    }
    
    private static void mostrarAyuda() {
        System.out.println("SINTAXIS: tareas [-t] [-h] [fichero entrada] [fichero salida]");
        System.out.println("-t                  Traza el algoritmo");
        System.out.println("-h                  Muestra esta ayuda");
        System.out.println("[fichero entrada]   Nombre del fichero de entrada ");
        System.out.println("[fichero salida]    Nombre del fichero de salida ");
    }
}
