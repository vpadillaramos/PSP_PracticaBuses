package com.vpr.grafico;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Ventana extends Canvas {
	// Atributos
	private static final long serialVersionUID = -3177900806970467554L;
	private Interfaz interfaz;
	private int width;
	private int height;
	
	//Componentes
	private JFrame f;
	
	// Constructor
	public Ventana(String titulo, Interfaz interfaz, int width, int height) {
		this.interfaz = interfaz;
		this.width = width;
		this.height = height;
		f = new JFrame(titulo);
		
		// TAMAÑO PANTALLA
		f.setPreferredSize(new Dimension(width, height));
		f.setMaximumSize(new Dimension(width, height));
		f.setMinimumSize(new Dimension(width, height));
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		
		// AÑADO LA INTERFAZ GRAFICA
		f.add(interfaz);
	}

	// Metodos
	public void mostrar() {
		//Hago visible la ventana
		f.setVisible(true);
		interfaz.start(); //inicio la interfaz
	}
	
}
