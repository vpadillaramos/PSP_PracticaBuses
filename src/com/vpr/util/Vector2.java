package com.vpr.util;

import java.io.Serializable;

public class Vector2 implements Serializable {
	// Atributos
	public int x, y;
	
	// Constructor
	public Vector2() {
		x = 0;
		y = 0;
	}
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// Metodos
	/**
	 * Analiza si un Vector2 está entre dos coordenadas x, ambas incluidas
	 * @param x1 es la coordenada menor
	 * @param x2 es la corrdenada mayor
	 */
	public boolean between(int x1, int x2) {
		return x >= x1 && x <= x2?true:false;
	}
	
	public Vector2 clone() {
		return new Vector2(x, y);
	}
	
}
