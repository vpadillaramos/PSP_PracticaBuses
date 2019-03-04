package com.vpr.grafico;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class Interfaz extends Canvas implements Runnable {
	
	// Atributos
	private static final long serialVersionUID = -2718677220860607195L;
	private Thread thread;
	private boolean running = false;
	public ParadaActor[][] paradas = new ParadaActor[Constantes.PARADAS_RUTAS.length][];
	
	private Handler handler;


	// Constructor
	public Interfaz() {
		handler = new Handler();

		//A�ado los objetos iniciales
		/*ParadaActor p = new ParadaActor(0, new Vector2(20, 20));
		handler.addObjeto(p);
		BusActor b = new BusActor(new Vector2(25,30));
		handler.addObjeto(b);
		
		p.isBusParado(b);*/
		
		/*if(p.isBusParado(b)) {
			System.out.println("Parado");
		}
		else {
			System.out.println("No parado");
		}*/
		
		// A�ado todas las rutas
		for(int i=0; i < Constantes.MAX_RUTAS; i++) {
			addRuta(i, Constantes.PARADAS_RUTAS[i]);
		}
	}
	
	
	
	// Metodos
	
	public void mostrar() {
		Ventana v = new Ventana("Buses", this, Constantes.WIDTH, Constantes.HEIGHT);
		v.mostrar();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		//Atributos
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		//Cuerpo
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while(delta >= 1) {
				tick();

				delta--;
			}

			if(running)
				render();
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		handler.tick();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		//pantalla
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Constantes.WIDTH, Constantes.HEIGHT);

		handler.render(g);

		g.dispose();
		bs.show();
	}
	
	
	// Metodos para a�adir objetos de forma externa
	public BusActor addBus(Vector2 posicion) {
		BusActor bus = new BusActor(posicion);
		handler.addObjeto(bus);
		return bus;
	}
	
	/**
	 * A�ade una ruta entera con sus paradas
	 * @param paradas es de tipo Vector2[], que contiene las posiciones de las paradas
	 */
	private void addRuta(int idRuta, Vector2[] posicionesParadas) {
		int i = 0;
		paradas[idRuta] = new ParadaActor[posicionesParadas.length];
		for(Vector2 posicion : posicionesParadas) {
			ParadaActor parada = new ParadaActor(i+1, posicion);
			paradas[idRuta][i] = parada;
			handler.addObjeto(parada);
			i++;
		}
	}
	
}
