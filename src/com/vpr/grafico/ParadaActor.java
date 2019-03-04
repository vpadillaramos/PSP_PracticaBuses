package com.vpr.grafico;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.vpr.pojo.Bus;
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
