package com.vpr.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GestionaTCP extends Thread {
	// Atributos
	private ServerSocket sctServer;
	
	// Constructor
	public GestionaTCP(ServerSocket sctServer) {
		this.sctServer = sctServer;
	}
	
	
	// Metodos
	public void run() {
		
		try {
			while(true) {
				// Acepto conexiones
				System.out.println("Esperando conexiones");
				Socket sctUsuario = sctServer.accept();
				System.out.println("Conexion aceptada");
				GestionaUsuario gu = new GestionaUsuario(sctUsuario);
				gu.start();
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
