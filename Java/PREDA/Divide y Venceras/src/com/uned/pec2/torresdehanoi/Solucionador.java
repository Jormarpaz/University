/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec2.torresdehanoi;

import java.util.Stack;

/**
 * @author jorge
 */
public class Solucionador {
    private int origen;
    private int destino;
    private int numDiscos;
    private boolean traza;
    private StringBuilder pasos;
    private Stack<Integer>[] postes;

    public Solucionador(int origen, int destino, int numDiscos, boolean traza) {
        this.origen = origen;
        this.destino = destino;
        this.numDiscos = numDiscos;
        this.traza = traza;
        this.pasos = new StringBuilder();
        this.postes = new Stack[3]; // Tres postes

        // Inicializamos los postes con discos
        for (int i = 0; i < 3; i++) {
            postes[i] = new Stack<>();
        }

        // Colocamos todos los discos en el poste de origen
        for (int i = numDiscos; i > 0; i--) {
            postes[origen - 1].push(i);
        }
    }

    public String resolver() {
        int auxiliar = 6 - origen - destino; // Los postes son 1, 2, 3. Suma 6 = 1+2+3.
        if (traza) {
            System.out.println("Parámetros iniciales: Origen=" + origen + ", Destino=" + destino + ", Discos=" + numDiscos);
            mostrarEstadoPostes();
        }
        resolverRecursivo(numDiscos, origen, destino, auxiliar);
        return pasos.toString();
    }

    private void resolverRecursivo(int discos, int origen, int destino, int auxiliar) {
        if (discos == 1) {
            registrarPaso(origen, destino);
        } else {
            resolverRecursivo(discos - 1, origen, auxiliar, destino);
            registrarPaso(origen, destino);
            resolverRecursivo(discos - 1, auxiliar, destino, origen);
        }
    }

    private void registrarPaso(int origen, int destino) {
        // Realizamos el movimiento de un disco
        int disco = postes[origen - 1].pop();
        postes[destino - 1].push(disco);

        pasos.append(origen).append(" ").append(destino).append("\n");
        if (traza) {
            System.out.println("Moviendo disco de " + origen + " a " + destino);
            mostrarEstadoPostes(); // Mostrar el estado después de cada movimiento
        }
    }
    
    private void mostrarEstadoPostes() {
        System.out.println("Estado actual:");
        for (int i = 0; i < 3; i++) {
            System.out.print("Poste " + (i + 1) + ": " + postes[i] + " ");
        }
        System.out.println();
    }
}

