package com.vpr.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.vpr.pojo.Bus;

public interface BusInterfaz extends Remote {
	public int notificarInicio(Bus bus) throws RemoteException;
	public void moverBus(Bus bus) throws RemoteException;
}
