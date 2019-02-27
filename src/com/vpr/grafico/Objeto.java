package com.vpr.grafico;

import java.awt.Graphics;

import com.vpr.util.Vector2;

public abstract class Objeto {
	
	// Atributos
	protected Vector2 posicion;
	protected Vector2 velocidad;
	
	
	// Constructor
	public Objeto() {
		posicion = new Vector2();
		velocidad = new Vector2();
	}
	
	public Objeto(Vector2 posicion) {
		this.posicion = new Vector2(posicion.x, posicion.y);
		this.velocidad = new Vector2();
	}
	
	
	// Metodos
	public abstract void tick();
	public abstract void render(Graphics g);

	public Vector2 getPosicion() {
		return posicion;
	}
	
	public void setPosicion(Vector2 posicion) {
		this.posicion.x = posicion.x;
		this.posicion.y = posicion.y;
	}
	
	public Vector2 getVelocidad() {
		return velocidad;
	}
	
	public void setVelocidad(Vector2 velocidad) {
		this.velocidad.x = velocidad.x;
		this.velocidad.y = velocidad.y;
	}
}
