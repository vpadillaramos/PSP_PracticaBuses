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
		if(Server.interfaz.paradas[bus.ruta][bus.siguienteParada].isBusParado(busActor)) 
			return true;
		// Si el bus no esta en la parada lo muevo
		else 
			return false;
	}

	@Override
	public int[] tiempoEspera(Bus bus) throws RemoteException {
		double distancia = (Constantes.PARADAS_RUTAS[bus.ruta][bus.siguienteParada].x + 730) - (bus.posicion.x + 100);
		double velocidad = bus.velocidad;
		System.out.printf("Distancia: %.2fm, Velocidad: %.2fm/s, Posicion: %dm\n", distancia, velocidad, bus.posicion.x);
		double tiempo = distancia / velocidad;
		System.out.println("Tiempo: "+tiempo);
		
		//Convierto el tiempo a horas y minutos
		int[] t = new int[2];
		t[0] = (int) Math.round((tiempo/60)); //minutos
		
		return t;
	}
}
