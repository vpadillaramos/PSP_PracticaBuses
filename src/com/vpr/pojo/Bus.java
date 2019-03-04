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
	public boolean ida; // si es true el bus esta en ida, si es false esta en vuelta

	//RMI
	private Registry reg;
	private BusInterfaz busInterfaz;


	//Constructor
	public Bus() {
		posicion = new Vector2(Constantes.INICIO_RUTA[ruta].x, Constantes.INICIO_RUTA[ruta].y);
		velocidad = Constantes.MIN_VELOCIDAD; // velocidad con la que arranca
		//ruta = intRandom(0, Constantes.MAX_RUTAS-1);
		ruta = 0;
		siguienteParada = 0;
		tiempoSiguienteParada = new int[2];
		tiempoAntesDeParar = new int[2];
		busInterfaz = null;
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

				linea = busInterfaz.notificarInicio(this);
				System.out.printf("INICIO: Ruta: %d, Velocidad: %.2f. Posicion: %d\n", ruta+1, velocidad, posicion.x);

				siguienteParada = 0; // me dirijo a la primera parada
				ida = true;
				// Añado el bus a la lista de busesProximos de todas las paradas por las que el bus pasara
				busInterfaz.actualizarParadas(this);
				
				while(true) {
					//Actualizo cada segundo
					Thread.sleep(1000);

					if(busInterfaz.isBusParado(this)) {
						// Compruebo la parada en la que estoy
						controlIdaVuelta();
						
						// Añado el bus a la lista de busesProximos de todas las paradas por las que el bus pasara
						busInterfaz.actualizarParadas(this);
					}
					else {
						tiempoSiguienteParada = busInterfaz.tiempoEspera(this, siguienteParada);
						if(velocidad != 0) {
							busInterfaz.moverBus(this);
							tiempoAntesDeParar = tiempoSiguienteParada.clone();
							/*System.out.printf("Voy a %.2fm/s. [%dm]. %dmin %ds\n", velocidad, posicion.x, 
									Math.abs(tiempoSiguienteParada[0]), Math.abs(tiempoSiguienteParada[1]));*/
						}
						else {
							tiempoSiguienteParada = tiempoAntesDeParar.clone();
							//System.out.printf("Un semáforo. [%dm]. %dmin %ds\n", posicion.x, Math.abs(tiempoSiguienteParada[0]), Math.abs(tiempoSiguienteParada[1]));
							Thread.sleep(intRandom(1500, 2500));
						}
						
						busInterfaz.actualizarTiemposDeEspera(this);
					}
					
					// Actualizo la posicion a la que el bus se va a mover
					actualizarPosicion();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizarPosicion() {
		//segun una velocidad aleatoria va cambiando la posicion del bus

		velocidad = dameVelocidad();
		if(ida)
			posicion.x = posicion.x + Math.round(velocidad);
		else
			posicion.x = posicion.x - Math.round(velocidad);
	}
	
	/**
	 * Comprueba la parada a la que se dirige el bus y controla si es la ultima o quedan mas, tanto
	 * en la ida como en la vuelta
	 * @throws InterruptedException
	 * @throws RemoteException 
	 */
	private void controlIdaVuelta() throws InterruptedException, RemoteException {
		// Si estoy en ida, ire sumando paradas hasta llegar a la ultima
		if(ida) {
			// Si es la ultima parada de la ruta
			if(siguienteParada+1 == Constantes.PARADAS_RUTAS[ruta].length) {
				ida = false;
				
				// Espero un tiempo porque es el final de la ruta
				System.out.println("Final de trayecto. Cambio de conductor y espero a gente");
				Thread.sleep(intRandom(4500, 6000));
				busInterfaz.removeBusProximo(this, siguienteParada);
				siguienteParada--; // en cuanto llega al final pone como ruta la siguiente parada
			}
			else {
				System.out.printf("Pasajeros subiendo [Parada %d]\n", siguienteParada+1);
				Thread.sleep(intRandom(2500, 3500));
				
				// Una vez que se han subido los pasajeros, elimino este bus de la lista de la parada
				busInterfaz.removeBusProximo(this, siguienteParada);
				
				// Paso a la siguiente parada
				siguienteParada++;
			}
		}
		// Si estoy en vuelta, ire restando paradas hasta llegar a la primera
		else {
			if(siguienteParada == 0) {
				ida = true;
				
				System.out.println("Cambio de conductor y espero a gente");
				Thread.sleep(intRandom(4500, 6000));
				busInterfaz.removeBusProximo(this, siguienteParada);
				siguienteParada++;
			}
			else {
				System.out.printf("Pasajeros subiendo [Parada %d]\n", siguienteParada+1);
				Thread.sleep(intRandom(2500, 3500));
				
				// Una vez que se han subido los pasajeros, elimino este bus de la lista de la parada
				busInterfaz.removeBusProximo(this, siguienteParada);
				
				// Paso a la siguiente parada
				siguienteParada--;
			}
		}
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
		
		return velocidadesPosibles[intRandom(0,velocidadesPosibles.length-1)];
	}
	
	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public Vector2 getPosicion() {
		return posicion.clone();
	}

	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion.clone();
	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public int getRuta() {
		return ruta;
	}

	public void setRuta(int ruta) {
		this.ruta = ruta;
	}

	public int getSiguienteParada() {
		return siguienteParada;
	}

	public void setSiguienteParada(int siguienteParada) {
		this.siguienteParada = siguienteParada;
	}

	public int[] getTiempoSiguienteParada() {
		return tiempoSiguienteParada.clone();
	}

	public void setTiempoSiguienteParada(int[] tiempoSiguienteParada) {
		this.tiempoSiguienteParada = tiempoSiguienteParada.clone();
	}

	public int[] getTiempoAntesDeParar() {
		return tiempoAntesDeParar.clone();
	}

	public void setTiempoAntesDeParar(int[] tiempoAntesDeParar) {
		this.tiempoAntesDeParar = tiempoAntesDeParar.clone();
	}

	public boolean isIda() {
		return ida;
	}

	public void setIda(boolean ida) {
		this.ida = ida;
	}

	public Bus clone() {
		Bus bus = new Bus();
		bus.setLinea(this.linea);
		bus.setPosicion(this.posicion.clone());
		bus.setVelocidad(this.velocidad);
		bus.setRuta(this.ruta);
		bus.setSiguienteParada(this.siguienteParada);
		bus.setTiempoSiguienteParada(this.tiempoSiguienteParada.clone());
		bus.setTiempoAntesDeParar(this.tiempoAntesDeParar.clone());
		bus.setIda(this.ida);
		return bus;
	}
	
	@Override
	public String toString() {
		return "Bus " + linea;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Bus))
			return false;
		
		Bus bus = (Bus) obj;
		return this.linea == bus.linea;
	}
	
	@Override
	public int hashCode() {
		return linea;
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
