package it.polimi.ingsw.GC_32.Game;


public interface Resource {
    
	public int getValue();
	public int getQuantity();
	public void increase(int quantity);
	public void decrease(int quantity);
	public ResourceType getType();
}
