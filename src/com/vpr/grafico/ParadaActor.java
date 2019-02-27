package com.vpr.grafico;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.vpr.util.Constantes;
import com.vpr.util.Vector2;

public class ParadaActor extends Objeto {
	//Atributos
	public Rectangle r;
	public int id;
	
	//Constructor
	public ParadaActor(int id, Vector2 posicion) {
		super(posicion);
		this.id = id;
		r = new Rectangle(posicion.x, posicion.y, Constantes.PARADA_WIDTH, Constantes.PARADA_HEIGHT);
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Constantes.PARADA_COLOR);
		
		//Dibujo la parada
		g2d.draw(r);
		g2d.fill(r);
	}
	
	public boolean isBusParado(BusActor bus) {
		return bus.posicion.x >= posicion.x?true:false;
		/*if(r.intersects(bus.r)) {
			System.out.println("Parado");
			return true;
		}
		else {
			System.out.println("No parado");
			return false;
		}*/
		//return r.intersects(bus.r);
	}
}
