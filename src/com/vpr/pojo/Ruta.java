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
	
	public void actualizarTiempoBus(int numParada, int numBus, int[] tiempo) {
		paradas[numParada].actualizarTiempoBus(numBus, tiempo);
	}
	
	public List<Bus> getBusesProximos(int numParada) {
		/*for(Bus b : paradas[numParada].getBusesProximos()) {
			System.out.println("Parada "+ numParada + ": "+b+", "+b.tiempoSiguienteParada[1]+"segundos");
		}*/
		return paradas[numParada].getBusesProximos();
	}
	
	@Override
	public String toString() {
		return "Ruta " + numRuta;
	}
	
}
