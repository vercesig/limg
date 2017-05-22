package it.polimi.ingsw.GC_32.Server.Game.Board;

public class CouncilRegion extends Region{

	//private final int regionID;
	private ActionSpace councilSpace;
	
	public CouncilRegion(int id){
		super(id);
		this.councilSpace = new ActionSpace(null, 1, false, this.getRegionID(), 0);
	}
	
	@Override
	public void print(){ 			//NUOVO METODO
		this.councilSpace.print();
	}

	@Override
	public boolean contains(int id){			//NUOVO METODO
		if (this.councilSpace.getActionSpaceID() == id)
			return true;
		else
			return false;
	}
}
