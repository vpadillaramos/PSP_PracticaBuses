package com.vpr.grafico;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Ventana extends Canvas implements ActionListener {
	// Atributos
	private static final long serialVersionUID = -3177900806970467554L;
	private Interfaz interfaz;
	private int width;
	private int height;
	public boolean isOpen;
	
	//Componentes
	private JFrame f;
	private JButton btSalir;
	
	// Constructor
	public Ventana(String titulo, Interfaz interfaz, int width, int height) {
		this.interfaz = interfaz;
		this.width = width;
		this.height = height;
		f = new JFrame(titulo);
		
		btSalir = new JButton("Salir");
		btSalir.setBounds(width-90, height-70, 70, 30);
		btSalir.addActionListener(this);
		f.add(btSalir);
		
		// TAMAÑO PANTALLA
		f.setPreferredSize(new Dimension(width, height));
		f.setMaximumSize(new Dimension(width, height));
		f.setMinimumSize(new Dimension(width, height));
		
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btSalir) {
			System.exit(0);
		}
		
	}
	
}
