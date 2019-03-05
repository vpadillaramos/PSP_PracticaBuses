package com.vpr.usuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.vpr.pojo.Bus;
import com.vpr.pojo.Parada;
import com.vpr.pojo.Ruta;
import java.awt.Component;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Vista extends JFrame implements ItemListener, ActionListener {
	// Variables
	private Parada paradaActual;
	
	// Componentes
	public JComboBox<Ruta> cbRutas;
	public JComboBox<Parada> cbParadas;
	public JLabel lbRuta;
	public JLabel lblSeleccionaParada;
	public JScrollPane scrollPane;
	public JList<String> listaParadas;
	public DefaultListModel<String> modelo;
	public JLabel lbNoHayDatos;
	public JButton btRefrescar;
	public JLabel lbParada;

	public Vista() {
		this.setTitle("Consultar tiempo paradas");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setSize(500, 500);
		getContentPane().setLayout(null);
		
		cbRutas = new JComboBox();
		cbRutas.setBounds(26, 86, 149, 20);
		getContentPane().add(cbRutas);
		cbRutas.addItemListener(this);
		
		cbParadas = new JComboBox();
		cbParadas.setBounds(26, 174, 149, 20);
		getContentPane().add(cbParadas);
		
		lbRuta = new JLabel("Selecciona ruta");
		lbRuta.setBounds(62, 62, 97, 14);
		getContentPane().add(lbRuta);
		
		lblSeleccionaParada = new JLabel("Selecciona parada");
		lblSeleccionaParada.setBounds(62, 149, 115, 14);
		getContentPane().add(lblSeleccionaParada);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(234, 29, 245, 258);
		getContentPane().add(scrollPane);
		
		listaParadas = new JList<>();
		scrollPane.setViewportView(listaParadas);
		modelo = new DefaultListModel<>();
		listaParadas.setModel(modelo);
		
		lbNoHayDatos = new JLabel("");
		lbNoHayDatos.setHorizontalTextPosition(SwingConstants.CENTER);
		lbNoHayDatos.setHorizontalAlignment(SwingConstants.CENTER);
		lbNoHayDatos.setForeground(Color.RED);
		lbNoHayDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbNoHayDatos.setBounds(234, 298, 245, 14);
		getContentPane().add(lbNoHayDatos);
		
		btRefrescar = new JButton("Refrescar");
		btRefrescar.setActionCommand("REFRESCAR");
		btRefrescar.setBounds(46, 234, 113, 23);
		getContentPane().add(btRefrescar);
		
		lbParada = new JLabel("");
		lbParada.setHorizontalTextPosition(SwingConstants.CENTER);
		lbParada.setHorizontalAlignment(SwingConstants.CENTER);
		lbParada.setBounds(305, 11, 104, 14);
		getContentPane().add(lbParada);
		btRefrescar.addActionListener(this);
	}
	
	// Metodos
	public void mostrar() {
		setVisible(true);
	}
	
	/**
	 * Rellena el JComboBox de rutas con las rutas recibidas del Server
	 * @param rutas (Ruta[]) array con el que poblar el JComboBox
	 */
	public void poblarRutas(Ruta[] rutas) {
		cbRutas.setModel(new DefaultComboBoxModel(rutas));
		cbRutas.setSelectedIndex(0);
	}
	
	/**
	 * Rellena el JComboBox de paradas con las paradas recibidas del Server
	 * @param paradas (Parada[]) array con el que poblar el JComboBox
	 */
	public void poblarParadas(Parada[] paradas) {
		cbParadas.setModel(new DefaultComboBoxModel(paradas));
		cbParadas.setSelectedIndex(0);
		paradaActual = (Parada) cbParadas.getSelectedItem();
		try {
			refrescarLista(Usuario.pedirParada(paradaActual));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Actualiza la lista de los proximos buses
	 * @param busesProximos (List<Bus>) son los proximos buses para mostrar en la lista
	 */
	private void refrescarLista(List<Bus> busesProximos) {
		
		lbNoHayDatos.setText("");
		lbParada.setText(paradaActual.toString());
		
		modelo.removeAllElements();
		if(!busesProximos.isEmpty()) {
			for(Bus b : busesProximos) {
				int minutos = Math.abs(b.tiempoSiguienteParada[0]);
				int segundos = Math.abs(b.tiempoSiguienteParada[1]);
				modelo.addElement(new String(b + " -> " + minutos + "mins " + segundos + "segs"));
			}
		}
		else
			lbNoHayDatos.setText("No hay datos para la parada " + (paradaActual.numParada+1));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbRutas && e.getStateChange() == ItemEvent.SELECTED) {
			Ruta r = (Ruta) e.getItem();
			poblarParadas(r.paradas);
		}
		
		if(e.getSource() == cbParadas && e.getStateChange() == ItemEvent.SELECTED) {
			try {
				paradaActual = (Parada) e.getItem();
				paradaActual.setNumRuta(cbRutas.getSelectedIndex());
				refrescarLista(Usuario.pedirParada(paradaActual));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btRefrescar) {
			try {
				paradaActual = (Parada) cbParadas.getSelectedItem();
				paradaActual.setNumRuta(cbRutas.getSelectedIndex());
				refrescarLista(Usuario.pedirParada(paradaActual));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
