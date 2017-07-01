package it.polimi.ingsw.GC_32.Common.Network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface RMIConnection extends Remote{
	UUID open() throws RemoteException;
	void close(UUID id) throws RemoteException;
	void sendMessage(UUID id, String Message) throws RemoteException;
	String getMessage(UUID id) throws RemoteException;
	boolean hadMessage(UUID id) throws RemoteException;
}