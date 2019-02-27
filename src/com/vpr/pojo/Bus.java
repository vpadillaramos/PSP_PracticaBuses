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
	public int[] tiempoSiguienteParada;
	public int[] tiempoAntesDeParar; 
	public boolean ida;
	
	//RMI
	private Registry reg;
	private BusInterfaz busInterfaz;
	
	
	
	//Constructor
	public Bus() {
		busInterfaz = null;
		velocidad = Constantes.MIN_VELOCIDAD; // velocidad con la que arranca
		
		ruta = intRandom(0, Constantes.MAX_RUTAS-1);
		posicion = new Vector2(Constantes.INICIO_RUTA[ruta].x, Constantes.INICIO_RUTA[ruta].y);
		
		ida = true;
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
						Thread.sleep(1000);
						
						if(busInterfaz.isBusParado(this)) {
							System.out.printf("He parado [Parada %d]\n", siguienteParada+1);
							Thread.sleep(3000);

							// Si estoy en ida, ire sumando paradas hasta llegar a la ultima
							if(ida) {
								// Si es la ultima parada de la ruta
								if(siguienteParada+1 == Constantes.PARADAS_RUTAS[ruta].length) {
									ida = false;
									siguienteParada--; // en cuanto llega al final pone como ruta la siguiente parada
									System.out.println("Ida false");
								}
								else
									siguienteParada++;
							}
							// Si estoy en vuelta, ire restando paradas hasta llegar a la primera
							else {
								if(siguienteParada == 0) {
									ida = true;
									siguienteParada++;
								}
								else 
									siguienteParada--;
							}
						}
						else {
							tiempoSiguienteParada = busInterfaz.tiempoEspera(this);
							if(velocidad != 0) {
								busInterfaz.moverBus(this);
								tiempoAntesDeParar = tiempoSiguienteParada.clone();
								System.out.printf("Voy a %.2fm/s. [%dm]. %dmin %ds\n", velocidad, posicion.x, tiempoSiguienteParada[0], tiempoSiguienteParada[1]);
							}
							else {
								tiempoSiguienteParada = tiempoAntesDeParar.clone();
								System.out.printf("Un semáforo. [%dm]. %dmin %ds\n", posicion.x, tiempoSiguienteParada[0], tiempoSiguienteParada[1]);
								Thread.sleep(1000);
							}
							actualizarPosicion();
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

		velocidad = dameVelocidad();
		posicion.x = posicion.x + Math.round(velocidad);
	}
	

	@Override
	public String toString() {
		return String.valueOf(linea);
	}
	
	private int intRandom(float minimo, float maximo) {
		Random num = new Random();
		int min = Math.round(minimo);
		int max = Math.round(maximo);
		return (min + num.nextInt(max-min+1));
	}
	
	private float dameVelocidad() {
		float[] velocidadesPosibles = new float[10];
		
		float aux = intRandom(Constantes.MIN_VELOCIDAD, Constantes.MAX_VELOCIDAD)+new Random().nextFloat();
		for(int i = 0; i < velocidadesPosibles.length; i++)
			velocidadesPosibles[i] = aux;
		velocidadesPosibles[0] = 0; // el bus puede pararse 
		
		//System.out.printf("velocidad posible: %.2f,return %.2f\n",velocidadesPosibles[1], a);
		return velocidadesPosibles[intRandom(0,velocidadesPosibles.length-1)];
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
