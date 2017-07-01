package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import it.polimi.ingsw.GC_32.Common.Network.RMIConnection;

import it.polimi.ingsw.GC_32.Common.Network.MsgConnection;

public class RMIMsgConnection implements MsgConnection{
	private RMIConnection serverConn;
	private UUID playerID;

	@Override
	public void open(String ip, int port) throws IOException{
		Registry rmiRegistry = LocateRegistry.getRegistry(ip, port);
		try{
			this.serverConn = (RMIConnection) rmiRegistry.lookup("LIMG_Conn");
		} catch(NotBoundException e){
			throw new IOException("Server Connection Object not Found");
		}
		this.playerID = this.serverConn.open();
	}

	@Override
	public void close() throws IOException{
		this.serverConn.close(playerID);
	}

	@Override
	public void sendMessage(String message) throws IOException{
		this.serverConn.sendMessage(playerID, message);
	}

	@Override
	public String getMessage() throws IOException{
		return this.serverConn.getMessage(playerID);
	}

	@Override
	public boolean hasMessage() throws IOException {
		return this.serverConn.hadMessage(playerID);
	}
}