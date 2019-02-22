package com.vpr.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.vpr.pojo.Bus;

public interface BusInterfaz extends Remote {
	public void notificarInicio(Bus bus) throws RemoteException;
}
