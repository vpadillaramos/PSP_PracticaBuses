package com.vpr.pojo;

import java.io.Serializable;
import java.util.List;

public class Ruta implements Serializable {
	// Atributos
	public int numRuta;
	public Parada[] paradas;
	
	
	// Constructor
	public Ruta() {
		
	}
	
	public Ruta(int numRuta, int numParadas) {
		this.numRuta = numRuta;
		paradas = new Parada[numParadas];
	}
	
	
	// Metodos
	public void addParada(int i, Parada p) {
		paradas[i] = p;
	}
	
	/**
	 * Actualiza el tiempo del bus que va a psar por la parada
	 * @param numParada (int) parada por la que el bus va a pasar
	 * @param numBus (int) es la linea del bus
	 * @param tiempo (int[]) tiempo que tardara el bus en llegar
	 */
	public void actualizarTiempoBus(int numParada, int numBus, int[] tiempo) {
		paradas[numParada].actualizarTiempoBus(numBus, tiempo);
	}
	
	/**
	 * 
	 * @param numParada (int) parada referida
	 * @return devuelve List<Bus> de los buses que van a pasar por la parada indicada
	 */
	public List<Bus> getBusesProximos(int numParada) {
		return paradas[numParada].getBusesProximos();
	}
	
	@Override
	public String toString() {
		return "Ruta " + numRuta;
	}
	
}
