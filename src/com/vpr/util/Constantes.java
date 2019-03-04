package com.vpr.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Constantes {
	///////////////////////////////
	//////////CONEXIONES///////////
	///////////////////////////////
	
	// **Casa**
	public static final String HOST = "localhost";
	public static final int PORT = 5556;
	// **Clase**
	/*public static String HOST = "192.168.34.139";
	public static int PORT = 5555;*/
	

	///////////////////////////////
	///////////USUARIO/////////////
	///////////////////////////////
	public static final int DAME_DATOS_INICIALES = 0;
	
	
	///////////////////////////////
	/////////////LOGICA////////////
	///////////////////////////////
	
	// **Buses**
	public static final int MAX_BUSES = 10;
	public static final float MAX_VELOCIDAD = 12.5f; // m/s
	public static final float MIN_VELOCIDAD = 9.72f; // m/s
	
	// **RMI**
	public static final String NOMBRE_CLASE = "GestionaBus";
	
	
	
	///////////////////////////////
	////////////INTERFAZ///////////
	///////////////////////////////
	
	// **Tamaños**
	
	// Ventana
	public static final Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = 1180;  // 980
	public static final int HEIGHT = 535; // 735
	
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
	public static final Vector2[] INICIO_RUTA = {
			new Vector2(30, HEIGHT-90),		// ruta1
			new Vector2(30, HEIGHT-180),	// ruta2
			new Vector2(30, HEIGHT-270)		// ruta3
	};
	
	// **Paradas y rutas**
	
	public static final Vector2[][] PARADAS_RUTAS = {
			// paradas de la ruta1
			{
				new Vector2(INICIO_RUTA[0].x +100, INICIO_RUTA[0].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[0].x +150, INICIO_RUTA[0].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[0].x +400, INICIO_RUTA[0].y - BUS_HEIGHT/2)
			},
			// paradas de la ruta2
			{
				new Vector2(INICIO_RUTA[1].x +100, INICIO_RUTA[1].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[1].x +200, INICIO_RUTA[1].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[1].x +300, INICIO_RUTA[1].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[1].x +400, INICIO_RUTA[1].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[1].x +500, INICIO_RUTA[1].y - BUS_HEIGHT/2)
			},	
			// paradas de la ruta3
			{
				new Vector2(INICIO_RUTA[2].x +100, INICIO_RUTA[2].y - BUS_HEIGHT/2),
				new Vector2(INICIO_RUTA[2].x +200, INICIO_RUTA[2].y - BUS_HEIGHT/2)
			}	
	};
	
	public static final int MAX_RUTAS = PARADAS_RUTAS.length;
}
