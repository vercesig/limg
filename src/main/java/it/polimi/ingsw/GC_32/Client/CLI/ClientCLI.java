package it.polimi.ingsw.GC_32.Client.CLI;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;

public class ClientCLI {

	ClientBoard boardReference;
	
	public void registerBoard(ClientBoard board){
		this.boardReference = board;
	}
	
}
