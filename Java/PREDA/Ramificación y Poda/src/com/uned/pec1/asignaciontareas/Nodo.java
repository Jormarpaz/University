/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge
 */
public class Nodo {
    int nivel; //Nivel en el arbol
    int coste; //Coste acumulado
    List<Integer> asignaciones; //Tareas asignadas a agentes
    
    public Nodo(int nivel, int coste, List<Integer> asignaciones) {
        this.nivel = nivel;
        this.coste = coste;
        this.asignaciones = new ArrayList<>(asignaciones);
    }
    
    public List<Nodo> generarHijos(int[][] tablaCostes) {
        List<Nodo> hijos = new ArrayList<>();
        for(int tarea = 0; tarea < tablaCostes[0].length; tarea++) {
            if(!asignaciones.contains(tarea)) {
                List<Integer> nuevaAsignacion = new ArrayList<>(asignaciones);
                nuevaAsignacion.add(tarea);
                int nuevoCoste = coste + tablaCostes[nivel][tarea];
                hijos.add(new Nodo(nivel +1, nuevoCoste, nuevaAsignacion));
            }
        }
        return hijos;
    }
    
}
