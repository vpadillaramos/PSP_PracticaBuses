package com.vpr.grafico;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class BusActor extends Objeto {
	// Atributos
	public Rectangle r;
	private Color color;
	
	// Constructor
	public BusActor(Vector2 posicion) {
		super(posicion);
		Random rand = new Random();
		color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		/*g.setColor(Color.RED);
		g.fillRect(x, y, 30, 15);*/
		
		Graphics2D g2d = (Graphics2D) g;
		
		
		g2d.setColor(color);
		r = new Rectangle(posicion.x, posicion.y, Constantes.BUS_WIDTH, Constantes.BUS_HEIGHT);
		//Lo dibujo
		g2d.draw(r);
		g2d.fill(r);
	}
	
	public void moverBus(Vector2 posicion) {
		this.posicion.x = posicion.x;
		this.posicion.y = posicion.y;
	}
	
}
