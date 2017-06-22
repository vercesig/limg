package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;

public interface MsgConnection {

	public void open() throws IOException;
	public void close() throws IOException;
	public void sendMessage(String message);
	public String getMessage();
	public boolean hasMessage() throws IOException;
	
}
