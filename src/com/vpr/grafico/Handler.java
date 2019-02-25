package com.vpr.grafico;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	LinkedList<Objeto> objetos = new LinkedList<>();
	
	public void tick() {
		for(int i = 0; i < objetos.size(); i++) {
			Objeto tempObjeto = objetos.get(i);
			tempObjeto.tick();
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < objetos.size(); i++) {
			Objeto tempObjeto = objetos.get(i);
			tempObjeto.render(g);
		}
	}
	
	public void addObjeto(Objeto o) {
		objetos.add(o);
	}
	
	public void removeObjeto(Objeto o) {
		objetos.remove(o);
	}
}
