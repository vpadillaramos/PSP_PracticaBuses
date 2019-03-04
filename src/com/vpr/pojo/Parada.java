package com.vpr.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vpr.util.Vector2;

public class Parada implements Serializable {
	//Atributos
	public int numRuta;
	public int numParada;
	public Vector2 posicion;
	public List<Bus> busesProximos;
	
	//Constructor
	public Parada() {
		busesProximos = new ArrayList<>();
	}
	
	public Parada(int numRuta, int numParada, Vector2 posicion) {
		busesProximos = new ArrayList<>();
		this.numParada = numParada;
		this.posicion = posicion.clone();
	}
	
	//Metodos
	public int getNumRuta() {
		return numRuta;
	}
	
	public int getNumParada() {
		return numParada;
	}
	
	public Vector2 getPosicion() {
		return posicion.clone();
	}
	
	public List<Bus> getBusesProximos(){
		/*for(Bus b : busesProximos) {
			System.out.println("Parada "+ numParada + ": "+b+", "+b.tiempoSiguienteParada[1]+"segundos");
		}*/
		return busesProximos;
	}
	
	public void setNumRuta(int numRuta) {
		this.numRuta = numRuta;
	}
	
	public void setNumParada(int numParada) {
		this.numParada = numParada;
	}
	
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion.clone();
	}
	
	public void addBusProximo(Bus bus) {
		if(!busesProximos.contains(bus))
			busesProximos.add(bus);
		/*for(Bus b : busesProximos)
			System.out.println(b);*/
	}
	
	public void removeBusProximo(Bus bus) {
		busesProximos.remove(bus);
	}
	
	public void actualizarTiempoBus(int numBus, int[] tiempo) {
		
		for(Bus b : busesProximos) {
			if(b.linea == numBus) {
				b.tiempoSiguienteParada = tiempo;
			}
		}
	}
	
	public Parada clone() {
		Parada p = new Parada(numRuta, numParada, posicion);
		for(Bus b : busesProximos)
			p.addBusProximo(b);
		return p;
	}
	
	@Override
	public String toString() {
		return "Parada " + numParada;
	}
}
