package it.polimi.ingsw.GC_32.Common.Network;

import java.io.IOException;

public interface MsgConnection{
	public void open(String ip, int port) throws IOException;
	public void close() throws IOException;
	public void sendMessage(String message) throws IOException;
	public String getMessage() throws IOException;
	public boolean hasMessage() throws IOException;
}