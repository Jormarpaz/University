/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.util.Arrays;

/**
 * @author jorge
 */
public class AsignadorTareas {

    public static String ejecutarAlgoritmo(int[][] matrizCostes, boolean traza) {
        try {
            // Validar que la matriz no este vacia
            if (matrizCostes.length == 0 || matrizCostes[0].length == 0) {
                throw new IllegalArgumentException("La matriz de entrada está vacía.");
            }

            // Inicializar el algoritmo de Ramificacion y Poda
            AlgoritmoRamificacionYPoda algoritmo = new AlgoritmoRamificacionYPoda(matrizCostes, traza);
            
            // Inicializar las asignaciones y estimaciones
            boolean[] tareasAsignadas = new boolean[matrizCostes.length];
            int[] agentes = new int[matrizCostes.length];
            Arrays.fill(agentes, -1);

            // Calcular estimaciones iniciales
            int estimacionOptima = 0;
            int estimacionPesimista = 0;
            for (int tarea = 0; tarea < matrizCostes[0].length; tarea++) {
                int minimo = Integer.MAX_VALUE;
                int maximo = Integer.MIN_VALUE;
                for (int agente = 0; agente < matrizCostes.length; agente++) {
                    minimo = Math.min(minimo, matrizCostes[agente][tarea]);
                    maximo = Math.max(maximo, matrizCostes[agente][tarea]);
                }
                estimacionOptima += minimo;
                estimacionPesimista += maximo;
            }

            // Crear el nodo raíz
            Nodo nodoRaiz = new Nodo(
                tareasAsignadas,       // Ninguna tarea asignada
                agentes,               // Sin agentes asignados
                0,                     // Coste inicial
                estimacionOptima,      // Estimación optimista inicial
                estimacionPesimista,   // Estimación pesimista inicial
                0,                     // Nivel inicial
                0                      // Cota inicial
            );


            // Ejecutar el algoritmo
            algoritmo.ejecutar(nodoRaiz);

            // Validar si solucion
            if (algoritmo.obtenerMejorSolucion() == null) {
                return "No se encontro una solucion optima.";
            }

            // Generar el resultado en el formato esperado
            // Matriz auxiliar para formatear el resultado
            Matriz matriz = new Matriz(matrizCostes); 
            return matriz.generarResultado(algoritmo.obtenerMejorSolucion().obtenerAsignaciones());

        } catch (IllegalArgumentException e) {
            return "Error de validación: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error inesperado durante la ejecucion del algoritmo.";
        }
    }
}





