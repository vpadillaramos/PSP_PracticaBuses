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
		bus.setLinea(id);
		Server.busesIniciados.put(id, bus);
		BusActor busActor = Server.interfaz.addBus(bus.x, bus.y);
		Server.busActores.put(id, busActor);
		return id;
	}

	@Override
	public void moverBus(Bus bus) throws RemoteException {
		BusActor busActor = Server.busActores.get(bus.getLinea());
		busActor.moverBus(bus.x, bus.y);
		
		if(bus.x >= )
	}
	
}
