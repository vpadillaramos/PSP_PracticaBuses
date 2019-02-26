package com.vpr.grafico;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.vpr.util.Constantes;

public class ParadaActor extends Objeto {
	//Atributos
	
	//Constructor
	public ParadaActor(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle r = new Rectangle(x ,y, Constantes.PARADA_WIDTH, Constantes.PARADA_HEIGHT);
		g2d.setColor(Constantes.PARADA_COLOR);
		
		//Dibujo la parada
		g2d.draw(r);
		g2d.fill(r);
	}
	
}
