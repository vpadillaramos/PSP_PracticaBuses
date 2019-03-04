package com.vpr.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import com.vpr.grafico.BusActor;
import com.vpr.grafico.Interfaz;
import com.vpr.pojo.Bus;
import com.vpr.pojo.Parada;
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
	private static int idBus = -1;
	public static ConcurrentHashMap<Integer, Bus> busesIniciados;
	public static ConcurrentHashMap<Integer, BusActor> busActores;
	public GestionaBus gb;
	public static Ruta[] rutas = new Ruta[Constantes.PARADAS_RUTAS.length];
	
	// Comunicacion UDP usuarios
	private DatagramSocket sct;
	private InetAddress addr;
	private DatagramPacket recibo;
	private DatagramPacket envio;
	private byte[] buffer;
	
	//Constructor
	public Server() {
		interfaz = new Interfaz();
		reg = null;
		busActores = new ConcurrentHashMap<>();
		busesIniciados = new ConcurrentHashMap<>();
		
		iniciarRutas();
	}

	//Metodos
	public void mostrarInterfaz() {
		interfaz.mostrar();
	}
	
	private void iniciarRutas() {
		for(int i = 0; i < rutas.length; i++) {
			// Creo la ruta
			rutas[i] = new Ruta(i, Constantes.PARADAS_RUTAS[i].length);
			
			// Añado las paradas a la ruta
			for(int j = 0; j < Constantes.PARADAS_RUTAS[i].length; j++) {
				Parada p = new Parada(i, j, Constantes.PARADAS_RUTAS[i][j]);
				rutas[i].addParada(j, p);
			}
		}
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
		
		// Comunicacion con los usuarios
		try {
			sct = new DatagramSocket(Constantes.PORT);
			addr = InetAddress.getByName(Constantes.HOST);
			
			conexionUsuario();
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void conexionUsuario() throws IOException {
		// Recibo la peticion de los datos iniciales del usuario
		int f1 = recibirPrimeraPeticion();

		// Envio de las rutas
		if(f1 == Constantes.DAME_DATOS_INICIALES) {
			enviarRutas();
		}
		
		while(true) {
			try {
				enviarParada();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int recibirPrimeraPeticion() throws IOException {
		buffer = new byte[1024];
		recibo = new DatagramPacket(buffer, buffer.length);
		System.out.println("Recibo petición de datos iniciales");
		sct.receive(recibo);
		return Integer.parseInt(new String(recibo.getData()).trim());
	}
	
	private void enviarRutas() throws IOException {
		// Escribo el objeto Rutas[] para enviarlo
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(rutas);
		out.flush();
		out.close();
		
		// Envio el objeto
		byte[] contenido = baos.toByteArray();
		envio = new DatagramPacket(contenido, contenido.length, recibo.getAddress(), recibo.getPort());
		System.out.println("Envio datos iniciales");
		sct.send(envio);
		System.out.println("Enviados");
	}
	
	private void enviarParada() throws IOException, ClassNotFoundException {
		// Espero a la peticion de una parada
		byte[] recibido = new byte[5000];
		recibo = new DatagramPacket(recibido, recibido.length);
		sct.receive(recibo); // recibo la parada
		
		byte[] datos = recibo.getData();
		ByteArrayInputStream bais = new ByteArrayInputStream(datos);
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bais));
		Parada parada = (Parada) in.readObject(); // esta parada no tiene los buses actualizados
		
		// Escribo el objeto Parada para enviarlo
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		
		/*for(Ruta r : rutas) {
			System.out.print(r + "  ");
			for(Parada p : r.paradas)
				System.out.print(p+";");
			System.out.println("");
		}*/
		
		/*System.out.print("SERVER envio busesProximos Parada "+ parada.numParada + " -> ");
		for(Bus b : rutas[parada.numRuta].paradas[parada.numParada].busesProximos)
			System.out.print(b+";");
		System.out.println("");*/
		
		/*System.out.print("SERVER envio busesProximos Parada "+ parada.numParada + " -> ");
		for(Bus b : rutas[parada.numRuta].paradas[parada.numParada].busesProximos) {
			System.out.print(b + " ("+b.tiempoSiguienteParada[0]+","+b.tiempoSiguienteParada[1]+")");
		}
		System.out.println("");*/
		
		/*System.out.println("SERVER tiempos");
		for(int i = 0; i < rutas[parada.numRuta].paradas.length; i++) {
			System.out.print("Parada -> ");
			for(Bus b : rutas[parada.numRuta].paradas[i].busesProximos)
				System.out.print(b + " en " + b.tiempoSiguienteParada[1] + "segs");
			System.out.println("");
		}*/
		
		/*System.out.print("SERVER: ");
		for(int i = 0; i < rutas[parada.numRuta].paradas.length; i++) {
			if(!rutas[parada.numRuta].paradas[i].busesProximos.isEmpty())
				System.out.print(rutas[parada.numRuta].paradas[i] + " " + rutas[parada.numRuta].paradas[i].busesProximos.get(0).tiempoSiguienteParada[1] + "segs");
		}*/
		
		/*for(Bus b : rutas[parada.numRuta].getBusesProximos(parada.numParada)) {
			System.out.println(parada + ": "+b+", "+b.tiempoSiguienteParada[1]+"segundos");
		}*/
		
		out.writeObject(rutas[parada.numRuta].getBusesProximos(parada.numParada));
		out.flush();
		out.close();
		
		// Envio la parada
		byte[] enviar = baos.toByteArray();
		envio = new DatagramPacket(enviar, enviar.length, recibo.getAddress(), recibo.getPort());
		sct.send(envio);
	}
	
	public static void actualizarTiempoBus(Bus bus, int numParada, int[] tiempo) {
		rutas[bus.ruta].actualizarTiempoBus(numParada, bus.linea, tiempo);
	}

	public static int getIdBus() {
		idBus++;
		return idBus;
	}
}
