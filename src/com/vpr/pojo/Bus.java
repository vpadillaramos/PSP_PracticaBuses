package com.vpr.pojo;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.vpr.util.BusInterfaz;
import com.vpr.util.Constantes;

public class Bus implements Serializable {
	//Atributos
	private int linea;
	private int posicion; // en metros
	private float velocidad; // km/h
	
	//RMI
	private Registry reg;
	private BusInterfaz bus;
	
	
	
	//Constructor
	public Bus() {
		bus = null;
	}
	
	//Metodos
	public void iniciarBus() {
		try {
			//Creo el registro
			reg = LocateRegistry.getRegistry(Constantes.HOST, Constantes.PORT);
			System.out.println("Conexion establecida");
			
			//Registro el objeto
			bus = (BusInterfaz) reg.lookup(Constantes.NOMBRE_CLASE);
			
			if(bus != null) {
				
				/*
				//Recorrido del bus hasta terminar
				do {
					//Comunico que el bus comienza, mando informacion del bus
					bus.notificarInicio(this);
					
					//Voy actualizando mi posicion
					
					//Consulto el tiempo que falta para la siguiente parada
					
					
				}while(true);*/
				
				bus.notificarInicio(this);
				System.out.println("He comenzado");
				
			}
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private void actualizarPosicion() {
		//segun una velocidad aleatoria va cambiando
		//de forma progresiva la posicion del bus
	}

	public int getLinea() {
		return linea;
	}

	public int getPosicion() {
		return posicion;
	}

	public float getVelocidad() {
		return velocidad;
	}

	public BusInterfaz getBus() {
		return bus;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public void setBus(BusInterfaz bus) {
		this.bus = bus;
	}

	@Override
	public String toString() {
		return String.valueOf(linea);
	}
	
	
}
