package com.vpr.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import com.vpr.grafico.BusActor;
import com.vpr.grafico.Interfaz;
import com.vpr.pojo.Bus;
import com.vpr.pojo.Ruta;
import com.vpr.util.Constantes;

public class Server {
	/**
	 * UDP para usuarios. 
	 * RMI para bus
	 */
	
	//Interfaz
	public static Interfaz interfaz;
	
	//Atributos
	private Registry reg;
	private static int idBus = 0;
	public static ConcurrentHashMap<Integer, Bus> busesIniciados;
	public static ConcurrentHashMap<Integer, BusActor> busActores;
	public GestionaBus gb;
	
	//Constructor
	public Server() {
		interfaz = new Interfaz();
		reg = null;
		busActores = new ConcurrentHashMap<>();
		busesIniciados = new ConcurrentHashMap<>();



	}

	//Metodos
	public void mostrarInterfaz() {
		interfaz.mostrar();
	}
	
	public void iniciarServer() {
		try {
			
			reg = LocateRegistry.createRegistry(Constantes.PORT);
			System.out.println("Registro creado");
			
			//Creo el objeto y lo inscribo
			gb = new GestionaBus();
			System.setProperty("java.rmi.server.hostname", Constantes.HOST);
			System.out.println("Estoy listo");
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
