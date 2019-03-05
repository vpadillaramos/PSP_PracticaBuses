package com.vpr.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.vpr.pojo.Bus;

public interface BusInterfaz extends Remote {
	
	/**
	 * Añade el bus al ConcurrentHashMap de la interfaz y de los buses en marcha
	 * @param bus (Bus) el bus iniciado
	 * @return la línea asignada al bus
	 * @throws RemoteException
	 */
	public int notificarInicio(Bus bus) throws RemoteException;
	
	/**
	 * Mueve el bus, en la interfaz, pasado por parámetro
	 * @param bus (Bus) bus que se va a mover
	 * @throws RemoteException
	 */
	public void moverBus(Bus bus) throws RemoteException;
	
	/**
	 * Comprueba si el bus esta parado en una parada, en la interfaz
	 * @param bus (Bus) bus a comprobar
	 * @return true si esta parado, false si no lo está
	 * @throws RemoteException
	 */
	public boolean isBusParado(Bus bus) throws RemoteException;
	
	/**
	 * Calcula el tiempo que tardara el bus en llegar a la parada pasada
	 * @param bus (Bus) bus a comprobar
	 * @param numParada (int) numero de la parada 
	 * @return int[], donde [0] es el tiempo en minutos y [1] es el tiempo en segundos
	 * @throws RemoteException
	 */
	public int[] tiempoEspera(Bus bus, int numParada) throws RemoteException;
	
	/**
	 * Asigna a cada parada el tiempo que tardara el bus en llegar hasta ella
	 * @param bus (Bus)
	 * @throws RemoteException
	 */
	public void actualizarTiemposDeEspera(Bus bus) throws RemoteException;
	
	/**
	 * Asigna a cada parada un bus que se dirige hacia ella. Actualizará todas las paradas
	 * de la ruta del bus, teniendo en cuenta la ida y la vuelta
	 * @param bus (Bus) que se dirige hacia la parada
	 * @throws RemoteException
	 */
	public void actualizarParadas(Bus bus) throws RemoteException;
	
	/**
	 * Elimina un bus de la lista que tiene la parada de buses que se dirigen hacia ella
	 * @param bus (Bus) a eliminar
	 * @param numParada numero de la parada de donde eliminar el bus
	 * @throws RemoteException
	 */
	public void removeBusProximo(Bus bus, int numParada) throws RemoteException;
}
