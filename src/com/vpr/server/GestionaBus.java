package com.vpr.server;

import java.rmi.RemoteException;

import com.vpr.pojo.Bus;
import com.vpr.util.BusInterfaz;

public class GestionaBus implements BusInterfaz {
	
	@Override
	public void notificarInicio(Bus bus) throws RemoteException {
		int id = Server.getIdBus();
		bus.setLinea(id);
		Server.busesIniciados.put(id, bus);
	}
	
}
