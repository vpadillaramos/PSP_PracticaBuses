package com.vpr.grafico;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BusActor extends Objeto {
	// Atributos
	
	
	// Constructor
	public BusActor(int x, int y) {
		super(x, y);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		/*g.setColor(Color.RED);
		g.fillRect(x, y, 30, 15);*/
		
		Graphics2D g2d = (Graphics2D) g;
		Rectangle r = new Rectangle(x, y, 30, 15);
		
		g2d.setColor(Color.RED);
		
		//Lo dibujo
		g2d.draw(r);
		g2d.fill(r);
	}
	
	public void moverBus(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
