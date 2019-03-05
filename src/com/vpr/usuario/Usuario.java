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
	// Constantes
	private final String F_PETICION = "peticionEncriptada.txt";
	private final String F_PETICION_TAMANO = "tamanoPeticion.txt";
	private final String F_CLAVE = "clave.txt";
	private final String F_CLAVE_TAMANO = "tamanoClave.txt";
	
	// Atributos
	private static DatagramSocket sct;
	private static DatagramPacket recibo;
	private static DatagramPacket envio;
	private static InetAddress addr;
	private byte[] buffer;
	private Ruta[] rutas;

	private Vista vista;
	
	// Conexion TCP para la peticion de datos iniciales
	//private Socket socket;
	private InetSocketAddress addrTCP;

	// Cifrado
	private KeyGenerator keyGen;
	private SecretKey key;
	private Cipher cipher;
	private DataOutputStream dos;
	private DataInputStream dis;


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
			//enviarPeticionDatos();
			
			
			try {
				enviarPeticion();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			System.out.println("Petición enviada correctamente");
			/*
			// Recibo los datos iniciales y los muestro
			rutas = recibirRutas();
			System.out.println("Datos iniciales recibidos correctamente");

			vista.poblarRutas(rutas);
			vista.poblarParadas(rutas[0].paradas);
			vista.mostrar();*/

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	private void enviarPeticion() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		Socket socket = new Socket();
		addrTCP = new InetSocketAddress(Constantes.HOST, Constantes.PORT);
		
		// Conexion con el servidor
		socket.connect(addrTCP);
		dos = new DataOutputStream(socket.getOutputStream());
		System.out.println("Conectado correctamente");
		
		// Genero los fichero a enviar
		generador2();
		
		// Envio la peticion y su tamaño
		enviarFichero(F_PETICION);
		System.out.println("Fichero encriptado enviado");
		
		// Envio la clave y su tamaño
		enviarFichero(F_CLAVE);
		System.out.println("Clave enviada");
	}
	
	private void enviarPeticion2() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		Socket socket = new Socket();
		addrTCP = new InetSocketAddress(Constantes.HOST, Constantes.PORT);
		
		// Conexion con el servidor
		socket.connect(addrTCP);
		dos = new DataOutputStream(socket.getOutputStream());
		System.out.println("Conectado correctamente");
		
		// Genero los fichero a enviar
		generador2();
		
		// Envio la peticion y su tamaño
		/*enviarFichero(F_PETICION);
		System.out.println("Fichero encriptado enviado");*/
		
		// Envio la clave y su tamaño
		enviarFichero(F_CLAVE);
		System.out.println("Clave enviada");
		
	}

	private void enviarFichero(String fichero) throws IOException {
		// Envio el tamaño del fichero
		File f = new File(fichero);
		int tamano = (int) f.length();
		dos.writeInt(tamano);
		System.out.println(fichero + " tamaño: " + tamano);

		// Envio el fichero
		//DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream(fichero);
		byte[] buffer = new byte[4096];
		while(fis.read(buffer) > 0) {
			dos.write(buffer, 0, buffer.length);
		}
		
		/*// Espero confirmacion
		if(dis.readBoolean())
			System.out.println("Confirmado");*/
	}

	private void enviarTamanoFichero(String ficheroOrigen, String ficheroDestino) throws IOException {
		//Obtengo el tamaño
		File f = new File(ficheroOrigen);
		long tamano = f.length();
		
		// Creo el fichero donde guardare el tamaño
		File ficheroTamano = new File(ficheroDestino);
		ficheroTamano.createNewFile();
		
		// Escribo el tamaño
		PrintWriter pw = new PrintWriter(new FileWriter(ficheroDestino));
		pw.print(tamano);
		pw.close();
		
		// Envio la peticion
		enviarFichero(ficheroDestino);
	}

	private void generador() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
		KeyPairGenerator kGen = KeyPairGenerator.getInstance("RSA");
		KeyPair kPair = kGen.generateKeyPair();
		
		// Encriptar
		Cipher ciph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		ciph.init(Cipher.ENCRYPT_MODE, kPair.getPrivate());
		
		// Creo el fichero que contendra la informacion a cifrar
		File peticion = new File("src" + File.separator + "peticionInicial.txt");
		peticion.createNewFile();
		FileInputStream is = new FileInputStream(peticion);
		FileOutputStream os = new FileOutputStream("src" + File.separator + "peticionEncriptada.txt");
		
		BigInteger bi = BigInteger.valueOf(Constantes.DAME_DATOS_INICIALES);
		byte[] buf = ciph.doFinal(bi.toByteArray(), 0, bi.toByteArray().length);
		os.write(buf);
		os.close();
	}
	
	private void generador2() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
		KeyPairGenerator kGen = KeyPairGenerator.getInstance("RSA");
		KeyPair kPair = kGen.generateKeyPair();

		// Encriptar
		Cipher ciph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		ciph.init(Cipher.ENCRYPT_MODE, kPair.getPrivate());
		
		// Creo el fichero donde escribir la peticion
		File fPeticion = new File(F_PETICION);
		fPeticion.createNewFile();
		// Abro el fichero
		FileOutputStream os = new FileOutputStream(fPeticion.getAbsolutePath());
		BigInteger bi = BigInteger.valueOf(Constantes.DAME_DATOS_INICIALES);
		byte[] buf = ciph.doFinal(bi.toByteArray(), 0, bi.toByteArray().length);
		os.write(buf);
		os.close();
		
		// Creo el fichero donde escribir la clave
		File fKey = new File(F_CLAVE);
		fKey.createNewFile();
		// Escribo la clave en un fichero
		KeyFactory kFac = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicSpec = kFac.getKeySpec(kPair.getPublic(), RSAPublicKeySpec.class);
		
		FileOutputStream osKey = new FileOutputStream(fKey.getAbsolutePath());
		PrintWriter pw = new PrintWriter(osKey);
		pw.println(publicSpec.getModulus());
		pw.println(publicSpec.getPublicExponent());
		pw.close();
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
