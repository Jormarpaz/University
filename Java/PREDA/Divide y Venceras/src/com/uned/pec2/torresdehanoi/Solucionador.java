/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec2.torresdehanoi;

/**
 *
 * @author jorge
 */
public class Solucionador {
    private int origen;
    private int destino;
    private int numDiscos;
    private boolean traza;
    private StringBuilder pasos;

    public Solucionador(int origen, int destino, int numDiscos, boolean traza) {
        this.origen = origen;
        this.destino = destino;
        this.numDiscos = numDiscos;
        this.traza = traza;
        this.pasos = new StringBuilder();
    }

    public String resolver() {
        int auxiliar = 6 - origen - destino; // Los postes son 1, 2, 3. Suma 6 = 1+2+3.
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
        pasos.append(origen).append(" ").append(destino).append("\n");
        if (traza) {
            System.out.println("Moviendo disco de " + origen + " a " + destino);
        }
    }
}
