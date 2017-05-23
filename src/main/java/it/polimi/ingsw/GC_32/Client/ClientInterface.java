package it.polimi.ingsw.GC_32.Client;

public interface ClientInterface {
	public void moveFamiliar(int familiar, int regionID, int spaceID);
	public void updateScore(String update);
	public String getMessage();
	public int getScreenId();
	public void openScreen(int screenId, String additionalData);
	public void sendMessage(String message);
}