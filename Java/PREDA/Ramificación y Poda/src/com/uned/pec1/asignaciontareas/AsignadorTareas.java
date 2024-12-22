/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author jorge
 */
public class AsignadorTareas {
    private int[][] tablaCostes;
    private boolean traza;
    
    public AsignadorTareas(int[][] tablaCostes, boolean traza) {
        this.tablaCostes = tablaCostes;
        this.traza = traza;
    }
    
    public int[][] calcularAsignacionOptima(){
        PriorityQueue<Nodo> cola = new PriorityQueue<>((a, b) -> a.coste - b.coste);
        
        Nodo nodoInicial = new Nodo(0, 0, new ArrayList<>());
        cola.add(nodoInicial);
        
        Nodo mejorSolucion = null;
        
        while(!cola.isEmpty()) {
            Nodo nodoActual = cola.poll();
            
            if(traza) {
                System.out.println("Procesando nodo en nivel " + nodoActual.nivel + " con coste acumulado " + nodoActual.coste);
                System.out.println("Asignaciones hasta el momento: " + nodoActual.asignaciones);
            }
            
            
            if (nodoActual.nivel == tablaCostes.length) {
                if (mejorSolucion == null || nodoActual.coste < mejorSolucion.coste) {
                    mejorSolucion = nodoActual;
                    if(traza) {
                        System.out.println("Mejor solución encontrada con coste " + mejorSolucion.coste);
                    }
                }
            } else {
                for (Nodo hijo : nodoActual.generarHijos(tablaCostes)) {
                    cola.add(hijo);
                    if (traza) {
                        System.out.println("Generando hijo: " + hijo.asignaciones + " con coste " + hijo.coste);
                    }
                }
            }
        }
        
        if (mejorSolucion != null) {
            System.out.println("Asignacion optima final: ");
            for(int i = 0; i < mejorSolucion.asignaciones.size(); i++) {
                System.out.println("Agente " + (i + 1) + " realiza la tarea " + (mejorSolucion.asignaciones.get(i) + 1));
            }
        }
        
        int[][] asignacion = new int[tablaCostes.length][2];
        for(int i = 0; i < mejorSolucion.asignaciones.size(); i++) {
            asignacion[i][0] = i +1; //Agente
            asignacion[i][1] = mejorSolucion.asignaciones.get(i) + 1; //Tarea
        }
        return asignacion;
    }
}
