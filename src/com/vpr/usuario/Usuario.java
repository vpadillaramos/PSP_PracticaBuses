package com.vpr.usuario;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.vpr.pojo.Bus;
import com.vpr.pojo.Parada;
import com.vpr.pojo.Ruta;
import com.vpr.util.Constantes;

public class Usuario {
	
	// Atributos
	private static DatagramSocket sct;
	private static DatagramPacket recibo;
	private static DatagramPacket envio;
	private static InetAddress addr;
	private byte[] buffer;
	private Ruta[] rutas;

	private Vista vista;
	
	// Constructor
	public Usuario() {
		vista = new Vista();
	}

	// Metodos
	public void iniciarUsuario() {
		try {
			// Inicializacion
			sct = new DatagramSocket();
			addr = InetAddress.getByName(Constantes.HOST);

			// Envio la peticion de los datos iniciales
			enviarPeticionDatos();
			
			System.out.println("Petición enviada correctamente");
			
			// Recibo los datos iniciales y los muestro
			rutas = recibirRutas();
			System.out.println("Datos iniciales recibidos correctamente");

			vista.poblarRutas(rutas);
			vista.poblarParadas(rutas[0].paradas);
			vista.mostrar();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia una peticion para recibir los datos iniciales
	 * @throws IOException
	 */
	private void enviarPeticionDatos() throws IOException {
		String datosIniciales = String.valueOf(Constantes.DAME_DATOS_INICIALES);
		envio = new DatagramPacket(datosIniciales.getBytes(), datosIniciales.getBytes().length,
				addr, Constantes.PORT);
		sct.send(envio);
	}

	/**
	 * Recibe los datos iniciales
	 * @return (Ruta[]) array de las rutas de los buses
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Ruta[] recibirRutas() throws IOException, ClassNotFoundException {
		byte[] contenido = new byte[5000];
		recibo = new DatagramPacket(contenido, contenido.length);
		System.out.println("Esperando a recibir los datos...");
		sct.receive(recibo);
		System.out.println("Recibido");

		byte[] datos = recibo.getData();
		ByteArrayInputStream bais = new ByteArrayInputStream(datos);
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bais));
		return (Ruta[]) in.readObject();
	}

	/**
	 * Pide los proximos buses de la parada especificada
	 * @param parada (Parada) sobre la que se quieren obtener los proximos buses
	 * @return (List<Bus>) devuelve la lista de los proximos buses con los datos necesarios
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Bus> pedirParada(Parada parada) throws IOException, ClassNotFoundException {
		// Envio la parada de la que quiero los datos
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(parada);
		out.flush();
		out.close();

		byte[] enviar = baos.toByteArray();
		envio = new DatagramPacket(enviar, enviar.length, addr, Constantes.PORT);
		sct.send(envio);


		// Recibo los buses proximos de la parada
		byte[] recibido = new byte[5000];
		recibo = new DatagramPacket(recibido, recibido.length);
		sct.receive(recibo);

		byte[] datos = recibo.getData();
		ByteArrayInputStream bais = new ByteArrayInputStream(datos);
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bais));
		return  (List<Bus>) in.readObject();
	}
}
