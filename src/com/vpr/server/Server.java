package com.vpr.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	// Constantes
	private final String F_PETICION = "src" + File.separator + "peti.txt";
	private final String F_CLAVE = "src" + File.separator + "cla.txt";
	
	//Interfaz
	public static Interfaz interfaz;
	
	//Atributos
	
	// Comunicacion RMI con los buses
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
	
	// TCP
	private ServerSocket sctServer;
	private DataInputStream dis;
	private DataOutputStream dos;
	private InetSocketAddress addrTCP;
	private GestionaTCP gtcp;
	
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
		// Abro el server en TCP
		try {
			sctServer = new ServerSocket();
			addrTCP = new InetSocketAddress(Constantes.HOST, Constantes.PORT);
			sctServer.bind(addrTCP);
			System.out.println("Server iniciado");
			gtcp = new GestionaTCP(sctServer);
			gtcp.start();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		/*try {
			
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
		}*/
	}
	
	private void recibirPeticion() throws IOException {
		
		
		// Acepto peticion
		System.out.println("Esperando conexiones...");
		Socket sctUsuario = sctServer.accept();
		dis = new DataInputStream(sctUsuario.getInputStream());
		System.out.println("Conexion aceptada");
		
		// Guardo los ficheros
		recibirFichero(sctUsuario, F_PETICION);
		System.out.println("Peticion recibida");
		
		/*recibirFichero(sctUsuario, F_CLAVE);
		System.out.println("Clave recibida");*/
		sctUsuario.close();
		dis.close();
		sctServer.close();
		
	}
	
	private void recibirPeticion2() throws IOException {
		ServerSocket sctServer = new ServerSocket();
		addrTCP = new InetSocketAddress(Constantes.HOST, Constantes.PORT);
		sctServer.bind(addrTCP);
		System.out.println("Server iniciado");
		
		// Acepto peticion
		System.out.println("Esperando conexiones...");
		Socket sctUsuario = sctServer.accept();
		dis = new DataInputStream(sctUsuario.getInputStream());
		System.out.println("Conexion aceptada");
		
		// Guardo los ficheros
		/*recibirFichero(sctUsuario, F_PETICION);
		System.out.println("Peticion recibida");*/
		
		recibirFichero(sctUsuario, F_CLAVE);
		System.out.println("Clave recibida");
		
		sctUsuario.close();
		dis.close();
		sctServer.close();
	}
	
	private void recibirFichero(Socket sctUsuario, String fichero) throws IOException {
		// Recibo el tamano del fichero		
		int tamanoFichero = dis.readInt();
		System.out.println(fichero + " tamaño: " + tamanoFichero);
		int read = 0;
		int totalRead = 0;
		int remaining = tamanoFichero;
		
		// Recibo el fichero
		//DataInputStream dis = new DataInputStream(sctUsuario.getInputStream());
		FileOutputStream fos = new FileOutputStream(fichero);
		byte[] buffer = new byte[4096];
		
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		
		/*// Mandar confirmacion
		dos.writeBoolean(true);
		System.out.println("Confirmacion enviada");*/
	}
	
	private synchronized void conexionUsuario() throws IOException {
		// Recibo la peticion de los datos iniciales del usuario
		int f1 = recibirPrimeraPeticion();
		System.out.println("Petición de datos iniciales recibida correctamente");

		// Envio de las rutas codificadas
		if(f1 == Constantes.DAME_DATOS_INICIALES) {
			enviarRutas();
			System.out.println("Envío de datos iniciales correcto");
		}
		
		while(true) {
			try {
				enviarBusesProximos();
				System.out.println("Información sobre los próximos buses enviada correctamente");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private synchronized int recibirPrimeraPeticion() throws IOException {
		buffer = new byte[1024];
		recibo = new DatagramPacket(buffer, buffer.length);
		sct.receive(recibo);
		return Integer.parseInt(new String(recibo.getData()).trim());
	}
	
	private synchronized void recibirPeticionEncriptada() {
		
	}
	
	private synchronized void enviarRutas() throws IOException {
		
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
	
	private synchronized void enviarBusesProximos() throws IOException, ClassNotFoundException {
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
		out.writeObject(rutas[parada.numRuta].getBusesProximos(parada.numParada));
		out.flush();
		out.close();
		
		// Envio la parada
		byte[] enviar = baos.toByteArray();
		envio = new DatagramPacket(enviar, enviar.length, recibo.getAddress(), recibo.getPort());
		sct.send(envio);
	}
	
	/**
	 * Acualiza el tiempo del bus que va a pasar por la parada indicada
	 * @param bus (Bus) que va a pasar por la parada
	 * @param numParada (int) numero de la parada
	 * @param tiempo (int[]) tiempo que tardara en llegar el bus
	 */
	public synchronized static void actualizarTiempoBus(Bus bus, int numParada, int[] tiempo) {
		rutas[bus.ruta].actualizarTiempoBus(numParada, bus.linea, tiempo);
	}

	public static int getIdBus() {
		idBus++;
		return idBus;
	}
}
