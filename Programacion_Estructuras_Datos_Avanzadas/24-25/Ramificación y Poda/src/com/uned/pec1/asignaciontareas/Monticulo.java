/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uned.pec1.asignaciontareas;

/**
 * @author jorge
 */
public class Monticulo<E extends Comparable<E>> {
    
    // Array para almacenar los elementos
    private E[] elementos; 
    // Número actual de elementos
    private int cantidad;  
    // Capacidad maxima
    private int capacidad; 

    /**
     * Constructor principal de la clase Monticulo.
     */
    public Monticulo(int capacidadMaxima) {
        this.capacidad = capacidadMaxima;
        this.cantidad = 0;
        this.elementos = (E[]) new Comparable[capacidadMaxima];
    }

    public boolean estaVacio() {
        return cantidad == 0;
    }

    // Agregar un elemento al monticulo
    public void agregar(E elemento) {
        if (cantidad == capacidad) {
            throw new IllegalStateException("El monticulo esta lleno.");
        }
        elementos[cantidad] = elemento;
        reorganizarHaciaArriba(cantidad);
        cantidad++;
    }

    // Extraer el elemento de la cima
    public E extraer() {
        if (estaVacio()) {
            throw new IllegalStateException("El monticulo esta vacio.");
        }
        E cima = elementos[0];
        elementos[0] = elementos[--cantidad];
        reorganizarHaciaAbajo(0);
        return cima;
    }

    // Obtener el primer elemento sin extraerlo
    public E primero() {
        if (estaVacio()) {
            return null;
        }
        return elementos[0];
    }

    // Reorganizar el elemento hacia arriba (flotar)
    private void reorganizarHaciaArriba(int indice) {
        int padre = (indice - 1) / 2;
        while (indice > 0 && elementos[indice].compareTo(elementos[padre]) < 0) {
            intercambiar(indice, padre);
            indice = padre;
            padre = (indice - 1) / 2;
        }
    }

    // Reorganizar el elemento hacia abajo (hundir)
    private void reorganizarHaciaAbajo(int indice) {
        int menor = indice;
        int hijoIzq = 2 * indice + 1;
        int hijoDer = 2 * indice + 2;

        if (hijoIzq < cantidad && elementos[hijoIzq].compareTo(elementos[menor]) < 0) {
            menor = hijoIzq;
        }
        if (hijoDer < cantidad && elementos[hijoDer].compareTo(elementos[menor]) < 0) {
            menor = hijoDer;
        }
        if (menor != indice) {
            intercambiar(indice, menor);
            reorganizarHaciaAbajo(menor);
        }
    }

    // Intercambiar dos elementos
    private void intercambiar(int i, int j) {
        E temp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = temp;
    }

    // Vaciar el montículo y establecer una nueva capacidad
    public void vaciar(int capacidadMaxima) {
        this.cantidad = 0;
        this.capacidad = capacidadMaxima;
        this.elementos = (E[]) new Comparable[capacidadMaxima];
    }

    // Verificar si el monticulo esta vacio
    public boolean monticuloVacio() {
        return cantidad == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Montículo: [");
        for (int i = 0; i < cantidad; i++) {
            sb.append(elementos[i]);
            if (i < cantidad - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}



