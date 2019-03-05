package com.vpr.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GestionaUsuario extends Thread {
	// Constantes
	private final String F_PETICION = "src" + File.separator + "peti.txt";
	private final String F_CLAVE = "src" + File.separator + "cla.txt";
	
	// Atributos
	private Socket sctUsuario;
	private DataInputStream dis;
	
	// Constructor
	public GestionaUsuario(Socket sctUsuario) {
		this.sctUsuario = sctUsuario;
	}
	
	// Metodos
	public void run() {
		try {
			dis = new DataInputStream(sctUsuario.getInputStream());
			
			// Recibo primer fichero
			recibirFichero(F_PETICION);
			System.out.println("Peticion recibida");
			
			// Recibo segundo fichero
			recibirFichero(F_CLAVE);
			System.out.println("Clave recibida");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void recibirFichero(String fichero) throws IOException {
		// Creo el fichero donde volcaremos el contenido recibido
		File f = new File(fichero);
		f.createNewFile();
		
		// Recibo el tamano del fichero
		int tamanoFichero = dis.readInt();
		int cont = 0;
		while(tamanoFichero <= 0) {
			System.out.println(cont);
			cont++;
			tamanoFichero = dis.readInt();
		}
		System.out.println(fichero + " tamaño: " + tamanoFichero);
		int read = 0;
		int totalRead = 0;
		int remaining = tamanoFichero;
		
		// Recibo el fichero
		FileOutputStream fos = new FileOutputStream(fichero);
		byte[] buffer = new byte[4096];
		
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			fos.write(buffer, 0, read);
		}
		
	}
}
