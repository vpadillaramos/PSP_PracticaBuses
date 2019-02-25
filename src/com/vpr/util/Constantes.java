package com.vpr.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Constantes {
	//CASA
	public static final String HOST = "192.168.1.132";
	public static final int PORT = 5556;
	//CLASE
	/*public static String HOST = "";
	public static int PORT = 5555;*/
	
	
	//Buses
	public static final int MAX_BUSES = 10;
	public static final int MAX_VELOCIDAD = 90; // km/h
	public static final int MIN_VELOCIDAD = 80; // km/h
	
	//Paradas
	public static final int[] INICIO_LINEA = {3, 6, 9}; // km
	
	//RMI
	public static final String NOMBRE_CLASE = "GestionaBus";
	
	//INTERFAZ
	public static final Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = 980;
	public static final int HEIGHT = 735;
	
	public static final int x = 30; //inicio en x de todas las rutas
	public static final int y = HEIGHT-90; //inicio en y ruta 1
}
