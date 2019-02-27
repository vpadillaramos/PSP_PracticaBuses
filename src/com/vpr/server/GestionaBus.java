package com.vpr.server;

import java.rmi.RemoteException;

import com.vpr.grafico.BusActor;
import com.vpr.pojo.Bus;
import com.vpr.util.BusInterfaz;
import com.vpr.util.Constantes;

public class GestionaBus implements BusInterfaz {
	@Override
	public int notificarInicio(Bus bus) throws RemoteException {
		int id = Server.getIdBus();
		bus.linea = id;
		Server.busesIniciados.put(id, bus);
		BusActor busActor = Server.interfaz.addBus(bus.posicion);
		Server.busActores.put(id, busActor);
		return id;
	}

	@Override
	public void moverBus(Bus bus) throws RemoteException {
		BusActor busActor = Server.busActores.get(bus.linea);
		busActor.moverBus(bus.posicion);
	}

	@Override
	public boolean isBusParado(Bus bus) throws RemoteException {
		// Compruebo si el bus se encuentra en la parada o no
		
		BusActor busActor = Server.busActores.get(bus.linea);
		// Si el bus esta parado
		System.out.printf("SiguienteParada: %d, x_Parada: %d\n", bus.siguienteParada, Server.interfaz.paradas[bus.ruta][bus.siguienteParada].getPosicion().x);
		if(Server.interfaz.paradas[bus.ruta][bus.siguienteParada].isBusParado(busActor)) 
			return true;
		// Si el bus no esta en la parada lo muevo
		else 
			return false;
	}

	@Override
	public int[] tiempoEspera(Bus bus) throws RemoteException {
		// Variables
		double distancia; // distancia que hay entre el bus y la siguiente parada
		double velocidad; // velocidad del bus
		double tiempo; // tiempo en segundos que tardara el bus en llegar a la parada
		int minutos = 0, segundos = 0; // tiempo pasado a minutos y segundos
		
		
		velocidad = bus.velocidad;
		
		// si el bus no esta parado
		if(velocidad > 0) {
			distancia = Constantes.PARADAS_RUTAS[bus.ruta][bus.siguienteParada].x - bus.posicion.x;
			
			//System.out.printf("Distancia: %.2fm, Velocidad: %.2fm/s, Posicion: %dm\n", distancia, velocidad, bus.posicion.x);
			tiempo = distancia / velocidad; // calculo el tiempo en segundos
			
			//Convierto el tiempo a horas y minutos
			
			// si el tiempo no llega a 60 segundos no paso a minutos
			if(tiempo < 60) {
				segundos = (int) Math.round(tiempo);
			}
			else {
				minutos = (int) Math.round(tiempo/60);
				segundos = (int) ((tiempo/60) - minutos) * 60;
			}
		}
		
		int[] t = {minutos, segundos};
		return t.clone();
	}
}
