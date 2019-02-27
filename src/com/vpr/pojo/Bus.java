package com.vpr.pojo;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import com.vpr.util.BusInterfaz;
import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class Bus implements Serializable {
	//Atributos
	public int linea;
	public Vector2 posicion; // metros
	public float velocidad; // metros/ssegundos
	public int ruta;
	public int siguienteParada;
	
	//RMI
	private Registry reg;
	private BusInterfaz busInterfaz;
	
	
	
	//Constructor
	public Bus() {
		busInterfaz = null;
		//velocidad = intRandom(Constantes.MIN_VELOCIDAD, Constantes.MAX_VELOCIDAD); // velocidad inicial
		velocidad = 6.9f;
		
		ruta = intRandom(0, Constantes.MAX_RUTAS-1);
		posicion = new Vector2(Constantes.INICIO_RUTA[ruta].x, Constantes.INICIO_RUTA[ruta].y);
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
			busInterfaz = (BusInterfaz) reg.lookup(Constantes.NOMBRE_CLASE);
			System.out.println("Registrado");
			
			
			if(busInterfaz != null) {
				
				/*
				//Recorrido del bus hasta terminar
				do {
					//Comunico que el bus comienza, mando informacion del bus
					bus.notificarInicio(this);
					
					//Voy actualizando mi posicion
					
					//Consulto el tiempo que falta para la siguiente parada
					
					
				}while(true);*/
				
				linea = busInterfaz.notificarInicio(this);
				System.out.printf("INICIO: Ruta: %d, Velocidad: %.2f. Posicion: %d\n", ruta+1, velocidad, posicion.x);
				
				siguienteParada = 0; // me dirijo a la primera parada
				while(true) {
					try {
						//Actualizo cada segundo
						Thread.sleep(2000);
						
						if(busInterfaz.isBusParado(this)) {
							System.out.printf("He parado [Parada %d]\n", siguienteParada+1);
							Thread.sleep(3000);
							siguienteParada++; // cambio la parada siguiente
						}
						else {
							int[] t = busInterfaz.tiempoEspera(this);
							System.out.printf("Voy a %.2fm/s. [%dm]. %dmin\n", velocidad, posicion.x, t[0]);
							
							actualizarPosicion();
							busInterfaz.moverBus(this);
						}
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
		
		//velocidad = intRandom(Constantes.MIN_VELOCIDAD, Constantes.MAX_VELOCIDAD);
		velocidad = 6.9f;
		posicion.x = posicion.x + Math.round(velocidad);
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
