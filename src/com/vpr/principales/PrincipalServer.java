package com.vpr.principales;

import com.vpr.grafico.Interfaz;
import com.vpr.server.Server;

public class PrincipalServer {
	// Variables
	private static Server server;
	
	public static void main(String[] args) {
		// Inicializacion
		server = new Server();
		
		// Muestro la interfaz
		server.mostrarInterfaz();
		
		// Inicio el server
		server.iniciarServer();
	}
}
