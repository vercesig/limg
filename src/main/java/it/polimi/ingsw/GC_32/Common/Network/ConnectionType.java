package it.polimi.ingsw.GC_32.Common.Network;


/**
 * enums which contains the possible way to connect to the server throught the network
 * 
 * <ul>
 * <li>{@link #SOCKET}: socket connection mode</li>
 * <li>{@link #RMI}: RMI connection mode</li>
 * </ul>
 *
 */
public enum ConnectionType {
	SOCKET, RMI, FAKE
}
