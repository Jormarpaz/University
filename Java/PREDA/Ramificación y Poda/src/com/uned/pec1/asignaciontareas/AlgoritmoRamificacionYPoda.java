/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

public class AlgoritmoRamificacionYPoda {

    private final int[][] tablaCostes;
    private final Monticulo<Nodo> colaPrioridad;
    private Nodo mejorSolucion;
    private final boolean mostrarTraza;
    private final int[] minimos;
    private final int[] maximos;
    private int cota;

    public AlgoritmoRamificacionYPoda(int[][] tablaCostes, boolean mostrarTraza) {
        this.tablaCostes = tablaCostes;
        this.colaPrioridad = new Monticulo<>(100);
        this.mostrarTraza = mostrarTraza;
        this.minimos = calcularExtremos(tablaCostes, true); // Mínimos de los costes
        this.maximos = calcularExtremos(tablaCostes, false); // Máximos de los costes
    }

    public void ejecutar(Nodo nodoInicial) {
        cota = calcularEstimacion(maximos, nodoInicial, false);  // Usa la estimación pesimista del nodo inicial.

        colaPrioridad.agregar(nodoInicial);

        if (mostrarTraza) {
            System.out.println("Ejecutando el algoritmo de Ramificacion y Poda.");
        }

        while (!colaPrioridad.estaVacio()) {
            Nodo nodoActual = colaPrioridad.extraer();

            if (mostrarTraza) {
                System.out.println("Procesando nodo en nivel " + nodoActual.getNivel() +
                        " con coste acumulado " + nodoActual.getCoste() +
                        " y estimacion optimista " + nodoActual.getEstimacionOptima());
            }

            if (esSolucionCompleta(nodoActual)) {
                actualizarMejorSolucion(nodoActual);
                if (mostrarTraza) {
                    System.out.println("Mejor solucion provisional encontrada con coste " + mejorSolucion.getCoste());
                }
            } else {
                expandirNodo(nodoActual);
            }
        }
    }

    private void expandirNodo(Nodo nodo) {
        if (mostrarTraza) {
            System.out.println("Expandiendo nodo en nivel " + nodo.getNivel() + " con coste " + nodo.getCoste());
        }

        int tarea = nodo.getNivel(); // La tarea corresponde al nivel actual
        for (int agente = 0; agente < tablaCostes.length; agente++) {
            if (!nodo.agenteAsignado(agente)) { // Si el agente no está asignado
                Nodo nuevoNodo = new Nodo(nodo); // Crear una copia del nodo actual
                nuevoNodo.getAgentes()[tarea] = agente; // Asignar el agente a la tarea actual
                nuevoNodo.getTareasAsignadas()[tarea] = true; // Marcar la tarea como asignada

                // Actualizamos el coste del hijo
                nuevoNodo.setCoste(nodo.getCoste() + tablaCostes[agente][tarea]);

                int estimacionOptima = calcularEstimacion(minimos, nuevoNodo, true);
                nuevoNodo.setEstimacionOptima(Math.max(estimacionOptima, 0)); // Garantizamos que no sea negativa

                int estimacionPesimista = calcularEstimacion(maximos, nuevoNodo, false);
                nuevoNodo.setEstimacionPesimista(estimacionPesimista);

                if (nuevoNodo.getEstimacionPesimista() < cota) {
                    nuevoNodo.setCota(nuevoNodo.getEstimacionPesimista());
                }

                if (nuevoNodo.getEstimacionPesimista() > cota) {
                    if (mostrarTraza) {
                        System.out.println("Nodo podado debido a su estimacion pesimista superior a la cota.");
                    }
                    continue; // Salta la expansión del nodo
                }

                if (nuevoNodo.getEstimacionOptima() > cota) {
                    if (mostrarTraza) {
                        System.out.println("Nodo podado debido a su estimacion optimista superior a la cota.");
                    }
                    continue; // Salta la expansión del nodo
                }

                colaPrioridad.agregar(nuevoNodo);
                if (mostrarTraza) {
                    System.out.println("Nodo generado con asignacion de tarea " + (tarea + 1) +
                            " a Agente " + (agente + 1) + " con coste acumulado " + nuevoNodo.getCoste());
                }
            }
        }
    }

    private boolean esSolucionCompleta(Nodo nodo) {
        return nodo.getNivel() == tablaCostes[0].length;
    }

    private void actualizarMejorSolucion(Nodo nodo) {
        if (mejorSolucion == null || nodo.getCoste() < mejorSolucion.getCoste()) {
            mejorSolucion = nodo;
            cota = mejorSolucion.getCoste(); // Actualizar la cota a la nueva mejor solución
        }
    }

    private int calcularEstimacion(int[] valores, Nodo nodo, boolean optimista) {
        int estimacion = nodo.getCoste();
        for (int tarea = nodo.getNivel(); tarea < tablaCostes[0].length; tarea++) {
            int valor = optimista ? minimos[tarea] : maximos[tarea];
            estimacion += valor;
        }
        return estimacion;
    }

    public int[] calcularExtremos(int[][] tablaCostes, boolean buscarMinimos) {
        int[] extremos = new int[tablaCostes[0].length];
        for (int tarea = 0; tarea < tablaCostes[0].length; tarea++) {
            int extremo = buscarMinimos ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            for (int agente = 0; agente < tablaCostes.length; agente++) {
                if (buscarMinimos) {
                    extremo = Math.min(extremo, tablaCostes[agente][tarea]);
                } else {
                    extremo = Math.max(extremo, tablaCostes[agente][tarea]);
                }
            }
            extremos[tarea] = extremo;
        }
        return extremos;
    }

    public Nodo obtenerMejorSolucion() {
        return mejorSolucion;
    }
}









