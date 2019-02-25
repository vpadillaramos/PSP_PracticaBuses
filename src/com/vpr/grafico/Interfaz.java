package com.vpr.grafico;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.vpr.util.Constantes;

public class Interfaz extends Canvas implements Runnable {
	
	// Atributos
	private static final long serialVersionUID = -2718677220860607195L;
	private Thread thread;
	private boolean running = false;
	
	private Handler handler;
	
	
	// Constructor
	public Interfaz() {
		handler = new Handler();
		
		
		//Añado los objetos iniciales
		//handler.addObjeto(new BusActor(30, Constantes.HEIGHT-90));
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
	
	
	// Metodos para añadir objetos de forma externa
	public BusActor addBus(int x, int y) {
		BusActor bus = new BusActor(x, y);
		handler.addObjeto(bus);
		return bus;
	}
}
