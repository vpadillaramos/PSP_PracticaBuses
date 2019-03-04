package com.vpr.server;

import java.rmi.RemoteException;

import com.vpr.grafico.BusActor;
import com.vpr.pojo.Bus;
import com.vpr.pojo.Parada;
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
		if(Server.interfaz.paradas[bus.ruta][bus.siguienteParada].isBusParado(busActor, bus)) 
			return true;
		// Si el bus no esta en la parada lo muevo
		else 
			return false;
	}

	@Override
	public int[] tiempoEspera(Bus bus, int numParada) throws RemoteException {
		// Variables
		double distancia; // distancia que hay entre el bus y la siguiente parada
		double velocidad; // velocidad del bus
		double tiempo = 0; // tiempo en segundos que tardara el bus en llegar a la parada
		
		
		velocidad = bus.velocidad;
		
		// si el bus no esta parado
		if(velocidad > 0) {
			distancia = Constantes.PARADAS_RUTAS[bus.ruta][numParada].x - bus.posicion.x;
			tiempo = distancia / velocidad; // calculo el tiempo en segundos
			return convierteSegundos(tiempo).clone();
		}
		else{
			return bus.tiempoAntesDeParar.clone();
		}
	}
	
	@Override
	public void actualizarTiemposDeEspera(Bus bus) throws RemoteException {
		for(int i = bus.siguienteParada; i < Server.rutas[bus.ruta].paradas.length; i++) {
			int[] t = tiempoEspera(bus, i);
			Server.actualizarTiempoBus(bus, i, t);
		}
	}

	private int[] convierteSegundos(double tiempo) {
		int minutos = 0, segundos = 0;
		if(tiempo < 60) {
			segundos = (int) Math.round(tiempo);
		}
		else {
			minutos = (int) Math.round(tiempo/60);
			segundos = (int) ((tiempo/60) - minutos) * 60;
		}
		int[] t = {minutos, segundos};
		return t;
	}

	@Override
	public void actualizarParadas(Bus bus) throws RemoteException {
		if(bus.ida) {
			for(int i = bus.siguienteParada; i < Server.rutas[bus.ruta].paradas.length; i++) {
				//Server.rutas[bus.ruta].paradas[i].busesProximos.clear();
				Server.rutas[bus.ruta].paradas[i].addBusProximo(bus.clone());
			}
		}
		else {
			for(int i = bus.siguienteParada; i >= 0; i--) {
				//Server.rutas[bus.ruta].paradas[i].busesProximos.clear();
				Server.rutas[bus.ruta].paradas[i].addBusProximo(bus.clone());
			}
		}
		
		/*System.out.print("GESTIONA actualiza busesProximos Parada 0 -> ");
		for(Bus b : Server.rutas[bus.ruta].paradas[0].busesProximos)
			System.out.print(b+";");
		System.out.println("");*/
	}

	@Override
	public void removeBusProximo(Bus bus, int numParada) throws RemoteException {
		Server.rutas[bus.ruta].paradas[numParada].removeBusProximo(bus.clone());
	}
}
