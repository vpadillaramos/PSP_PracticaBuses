package com.vpr.grafico;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.vpr.pojo.Bus;
import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class ParadaActor extends Objeto {
	//Atributos
	// Parada
	public Rectangle r;
	
	// Numero de parada
	private String numParada = "";
	private Vector2 posicionNumParada;
	private Color numParadaColor;
	private Color bgNumParadaColor;
	private FontMetrics fm;
	private Rectangle2D bgNumParada;
	
	//Constructor
	public ParadaActor(int numParada, Vector2 posicion) {
		super(posicion);
		// Parada
		r = new Rectangle(posicion.x, posicion.y, Constantes.PARADA_WIDTH, Constantes.PARADA_HEIGHT);
		
		// NumParada
		this.numParada = String.valueOf(numParada+1);
		numParadaColor = Color.BLACK;
		bgNumParadaColor = Color.WHITE;
	
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		// Dibujo la parada
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Constantes.PARADA_COLOR);
		g2d.draw(r);
		g2d.fill(r);
		
		// Dibujo el numero de la parada
		posicionNumParada = new Vector2(posicion.x + (Constantes.PARADA_WIDTH/2) - 2, posicion.y);
		fm = g.getFontMetrics();
		bgNumParada = fm.getStringBounds(numParada, g);
		g.setColor(bgNumParadaColor);
		g.fillRect(posicionNumParada.x, posicionNumParada.y - fm.getAscent(), (int)bgNumParada.getWidth(), (int)bgNumParada.getHeight());
		g.setColor(numParadaColor);
		g.drawString(numParada, posicionNumParada.x, posicionNumParada.y);
	}
	
	public boolean isBusParado(BusActor busActor, Bus bus) {
		
		// Si el bus esta en ida, la referencia para saber si ha parado es si parte izquierda
		if(bus.ida && busActor.posicion.between(posicion.x, posicion.x + Constantes.PARADA_WIDTH))
			return true;
		// Si no, sera su parte derecha
		else {
			if(!bus.ida && new Vector2(busActor.posicion.x + Constantes.BUS_WIDTH, busActor.posicion.y).between(posicion.x, posicion.x + Constantes.PARADA_WIDTH))
				return true;
			else
				return false;
		}
	}
}
