/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.util.Arrays;

/**
 * @author jorge
 */
public class Nodo implements Comparable<Nodo> {
    
    // Tareas asignadas
    private final boolean[] tareasAsignadas; 
    // Agentes asignados a las tareas
    private final int[] agentes; 
    // Coste acumulado
    private int coste; 
    // Estimacion del coste optimo
    private int estimacionOptima; 
    // Estimación del coste pesimista
    private int estimacionPesimista; 
    // Valor usado para decidir si se poda
    private int cota; 
    // Nivel del nodo en el arbol
    private final int nivel; 

    /**
     * Constructor principal de la clase Nodo.
     */
    public Nodo(boolean[] tareasAsignadas, int[] agentes, int coste, int estimacionOptima, int estimacionPesimista, int cota, int nivel) {
        this.tareasAsignadas = Arrays.copyOf(tareasAsignadas, tareasAsignadas.length);
        this.agentes = Arrays.copyOf(agentes, agentes.length);
        this.coste = coste;
        this.estimacionOptima = estimacionOptima;
        this.estimacionPesimista = estimacionPesimista;
        this.cota = cota;
        this.nivel = nivel;
    }

    /**
     * Constructor con copia. Crea un nuevo nodo hijo a partir de un nodo padre.
     */
    public Nodo(Nodo nodoPadre) {
        this.tareasAsignadas = Arrays.copyOf(nodoPadre.tareasAsignadas, nodoPadre.tareasAsignadas.length);
        this.agentes = Arrays.copyOf(nodoPadre.agentes, nodoPadre.agentes.length);
        this.coste = nodoPadre.coste;
        this.estimacionOptima = nodoPadre.estimacionOptima;
        this.estimacionPesimista = nodoPadre.estimacionPesimista;
        this.cota = nodoPadre.cota;
        this.nivel = nodoPadre.nivel + 1;
    }

    // Crear un nodo hijo al asignar una nueva tarea a un agente.
    public Nodo crearHijo(int agente, int[][] tablaCostes) {
        boolean[] nuevasTareas = Arrays.copyOf(tareasAsignadas, tareasAsignadas.length);
        int[] nuevosAgentes = Arrays.copyOf(agentes, agentes.length);

        nuevasTareas[nivel] = true; // Marca la tarea actual como asignada
        nuevosAgentes[nivel] = agente;

        int nuevoCoste = coste + tablaCostes[agente][nivel];
        int nuevaEstimacionOptima = calcularEstimacionOptima(tablaCostes, nuevasTareas, nivel + 1);
        int nuevaEstimacionPesimista = calcularEstimacionPesimista(tablaCostes, nuevasTareas, nivel + 1);
        int nuevaCota = Math.max(nuevaEstimacionOptima, nuevaEstimacionPesimista);

        return new Nodo(nuevasTareas, nuevosAgentes, nuevoCoste, nuevaEstimacionOptima, nuevaEstimacionPesimista, nuevaCota, nivel + 1);
    }

    // Calcular la estimacion optima.
    private int calcularEstimacionOptima(int[][] tablaCostes, boolean[] tareas, int nivelActual) {
        int sumaMinimos = 0;
        for (int i = nivelActual; i < tablaCostes.length; i++) {
            int minimo = Integer.MAX_VALUE;
            for (int j = 0; j < tablaCostes[i].length; j++) {
                if (!tareas[j]) {
                    minimo = Math.min(minimo, tablaCostes[i][j]);
                }
            }
            sumaMinimos += minimo;
        }
        return sumaMinimos;
    }

    // Calcular la estimacion pesimista.
    private int calcularEstimacionPesimista(int[][] tablaCostes, boolean[] tareas, int nivelActual) {
        int suma = 0;
        for (int tarea = nivelActual; tarea < tablaCostes.length; tarea++) {
            int maximo = Integer.MIN_VALUE;
            for (int[] tablaCoste : tablaCostes) {
                if (!tareas[tarea]) {
                    maximo = Math.max(maximo, tablaCoste[tarea]);
                }
            }
            suma += maximo;
        }
        return suma;
    }
    
    // Comprobar si el agente está asignado
    public boolean agenteAsignado(int agente) {
    for (int asignado : agentes) {
        if (asignado == agente) {
            return true;
        }
    }
    return false;
}


    /**
     * Métodos getter para acceder a los atributos del nodo.
     * @return 
     */
    public int[] obtenerAsignaciones() {
        return agentes;
    }

    public boolean tareaAsignada(int tarea) {
        return tareasAsignadas[tarea];
    }

    public int getNivel() {
        return nivel;
    }

    public int getCoste() {
        return coste;
    }
    
    public int[] getAgentes() {
        return agentes; // Un arreglo de enteros que representa la asignación de tareas
    }
    
    public boolean[] getTareasAsignadas() {
        return tareasAsignadas;
    }

    public int getEstimacionOptima() {
        return estimacionOptima;
    }

    public int getEstimacionPesimista() {
        return estimacionPesimista;
    }

    public int getCota() {
        return cota;
    }
    
    public void setCoste(int coste) {
        this.coste = coste;
    }

    public void setEstimacionOptima(int estimacion) {
        this.estimacionOptima = estimacion;
    }

    public void setEstimacionPesimista(int estimacion) {
        this.estimacionPesimista = estimacion;
    }

    public void setCota(int cota) {
        this.cota = cota;
    }

    @Override
    public int compareTo(Nodo otro) {
        return Integer.compare(this.estimacionOptima, otro.estimacionOptima);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Asignaciones: ");
        // Mostrar tareas primero y luego agentes
        for (int i = 0; i < tareasAsignadas.length; i++) {
            if (tareasAsignadas[i]) {
                sb.append("Tarea ").append(i + 1).append(" -> Agente ").append(agentes[i] + 1).append("; ");
            }
        }
        sb.append("\nCoste acumulado: ").append(coste);
        sb.append(", Estimacion Optima: ").append(estimacionOptima);
        sb.append(", Estimacion Pesimista: ").append(estimacionPesimista);
        sb.append(", Cota: ").append(cota);
        sb.append(", Nivel: ").append(nivel);
        return sb.toString();
    }
}




