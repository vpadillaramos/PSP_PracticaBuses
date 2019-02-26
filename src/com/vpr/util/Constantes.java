package com.vpr.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Constantes {
	///////////////////////////////
	//////////CONEXIONES///////////
	///////////////////////////////
	
	// **Casa**
	/*public static final String HOST = "localhost";
	public static final int PORT = 5556;*/
	// **Clase**
	public static String HOST = "192.168.34.139";
	public static int PORT = 5555;
	
	
	///////////////////////////////
	/////////////LOGICA////////////
	///////////////////////////////
	
	// **Buses**
	public static final int MAX_BUSES = 10;
	public static final int MAX_VELOCIDAD = 90; // km/h
	public static final int MIN_VELOCIDAD = 80; // km/h
	
	// **RMI**
	public static final String NOMBRE_CLASE = "GestionaBus";
	
	
	
	///////////////////////////////
	////////////INTERFAZ///////////
	///////////////////////////////
	
	// **Tamaños**
	
	// Ventana
	public static final Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = 980;
	public static final int HEIGHT = 735;
	
	// Bus
	public static final Color BUS_COLOR = Color.RED;
	public static final int BUS_WIDTH = 30;
	public static final int BUS_HEIGHT = 15;
	
	// Parada
	public static final Color PARADA_COLOR = Color.GREEN;
	public static final int PARADA_WIDTH = 30;
	public static final int PARADA_HEIGHT = 30;
	
	// **Posiciones**
	//Matriz que guarda la coordenada x (posicion 0) e y (posicion y) del inicio de cada ruta
	public static final int[][] INICIO_RUTA = {
			{30, HEIGHT-90},		// ruta1
			{30, HEIGHT-180},		// ruta2
			{30, HEIGHT-270}		// ruta3
	};
	
	// **Paradas y rutas**
	// Ruta1
	public static final int[][] PARADAS_RUTA1 = {
			{INICIO_RUTA[0][0]+100, Constantes.INICIO_RUTA[0][1]-(Constantes.BUS_HEIGHT/2)},
			{INICIO_RUTA[0][0]+300, Constantes.INICIO_RUTA[0][1]-(Constantes.BUS_HEIGHT/2)}
	};
	
	public static final int MAX_RUTAS = INICIO_RUTA.length-1;
}
