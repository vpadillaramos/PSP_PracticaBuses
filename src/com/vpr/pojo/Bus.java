package com.vpr.pojo;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import com.vpr.util.BusInterfaz;
import com.vpr.util.Constantes;

public class Bus implements Serializable {
	//Atributos
	private int linea;
	public int x, y;
	private int velocidad; // km/h
	private int ruta;
	
	//RMI
	private Registry reg;
	private BusInterfaz bus;
	
	
	
	//Constructor
	public Bus() {
		bus = null;
		velocidad = intRandom(Constantes.MIN_VELOCIDAD, Constantes.MAX_VELOCIDAD); // velocidad inicial
		ruta = intRandom(0, Constantes.MAX_RUTAS);
		x = Constantes.INICIO_RUTA[ruta][0];
		y = Constantes.INICIO_RUTA[ruta][1];
	}
	
	//Metodos
	public void iniciarBus() {
		try {
			//Creo el registro
			System.setProperty("java.rmi.server.hostname", Constantes.HOST);
			reg = LocateRegistry.getRegistry(Constantes.HOST, Constantes.PORT);
			System.out.println("Conexion establecida");
			
			//Registro el objeto
			System.out.print("Registrando objeto...");
			bus = (BusInterfaz) reg.lookup(Constantes.NOMBRE_CLASE);
			System.out.println("Registrado");
			
			
			if(bus != null) {
				
				/*
				//Recorrido del bus hasta terminar
				do {
					//Comunico que el bus comienza, mando informacion del bus
					bus.notificarInicio(this);
					
					//Voy actualizando mi posicion
					
					//Consulto el tiempo que falta para la siguiente parada
					
					
				}while(true);*/
				
				linea = bus.notificarInicio(this);
				System.out.printf("INICIO: Ruta: %d, Velocidad: %d. Posicion: %d\n", ruta+1, velocidad, x);
				
				while(true) {
					try {
						//Actualizo cada segundo
						Thread.sleep(2000);
						actualizarPosicion();
						System.out.printf("Voy a %dkm/h. [%dm]\n", velocidad, x);
						bus.moverBus(this);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
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
		velocidad = intRandom(Constantes.MIN_VELOCIDAD, Constantes.MAX_VELOCIDAD);
		x = x + toMpS(velocidad/2);
	}

	public int getLinea() {
		return linea;
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

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	

	@Override
	public String toString() {
		return String.valueOf(linea);
	}
	
	private int intRandom(int min, int max) {
		Random num = new Random();
		return (min + num.nextInt(max-min+1));
	}
	
	/**
	 * Pasa de km*h a m*s
	 * @param x
	 * @return
	 */
	private int toMpS(int x) {
		return Math.round((x*5)/18);
	}
	
	/**
	 * Pasa de m*s a km*h
	 * @param x
	 * @return
	 */
	private int toKMpS(int x) {
		return Math.round(x*0.001f*3600);
	}
	
}
