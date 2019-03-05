package com.vpr.grafico;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class BusActor extends Objeto {
	// Atributos
	// Bus
	public Rectangle r;
	private Color color;
	
	// Linea del bus
	private String linea = "";
	private Vector2 posicionLinea;
	private Color lineaColor;
	private Color bgLineaColor;
	private FontMetrics fm;
	private Rectangle2D bg;
	
	// Constructor
	public BusActor(int linea, Vector2 posicion) {
		super(posicion);
		// Bus
		Random rand = new Random();
		color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		// Linea
		this.linea = String.valueOf(linea+1);
		lineaColor = Color.WHITE;
		bgLineaColor = Color.BLACK;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		// Dibujo bus
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		r = new Rectangle(posicion.x, posicion.y, Constantes.BUS_WIDTH, Constantes.BUS_HEIGHT);

		//Lo dibujo
		g2d.draw(r);
		g2d.fill(r);

		// Linea
		posicionLinea = new Vector2(posicion.x + (Constantes.BUS_WIDTH/3) + 2, posicion.y + (Constantes.BUS_HEIGHT/2) + 5);
		fm = g.getFontMetrics();
		bg = fm.getStringBounds(linea, g);
		g.setColor(bgLineaColor);
		g.fillRect(posicionLinea.x, posicionLinea.y - fm.getAscent(), (int)bg.getWidth(), (int)bg.getHeight());
		g.setColor(lineaColor);
		g.drawString(linea, posicionLinea.x, posicionLinea.y);
	}
	
	public void moverBus(Vector2 posicion) {
		this.posicion.x = posicion.x;
		this.posicion.y = posicion.y;
	}
	
}
