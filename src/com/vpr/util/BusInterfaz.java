package com.vpr.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.vpr.pojo.Bus;

public interface BusInterfaz extends Remote {
	public int notificarInicio(Bus bus) throws RemoteException;
	public void moverBus(Bus bus) throws RemoteException;
	public boolean isBusParado(Bus bus) throws RemoteException;
	public int[] tiempoEspera(Bus bus, int numParada) throws RemoteException;
	public void actualizarTiemposDeEspera(Bus bus) throws RemoteException;
	public void actualizarParadas(Bus bus) throws RemoteException;
	public void removeBusProximo(Bus bus, int numParada) throws RemoteException;
}
