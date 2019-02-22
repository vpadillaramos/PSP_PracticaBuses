package com.vpr.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import com.vpr.pojo.Bus;
import com.vpr.util.Constantes;

public class Server {
	/**
	 * UDP para usuarios. 
	 * RMI para bus
	 */
	
	//Atributos
	private Registry reg;
	private static int idBus = 0;
	public static ConcurrentHashMap<Integer, Bus> busesIniciados;

	//Constructor
	public Server() {
		reg = null;
		busesIniciados = new ConcurrentHashMap<>();
	}

	//Metodos
	public void iniciarServer() {
		try {
			System.setProperty("java.rmi.server.hostname", Constantes.HOST);
			reg = LocateRegistry.createRegistry(Constantes.PORT);
			System.out.println("Registro creado");
			
			//Creo el objeto y lo inscribo
			GestionaBus  gb = new GestionaBus();
			reg.rebind(Constantes.NOMBRE_CLASE, UnicastRemoteObject.exportObject(gb, 0));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static int getIdBus() {
		idBus++;
		return idBus;
	}
}
