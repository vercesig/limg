package it.polimi.ingsw.GC_32.Client.Game;

public class ClientFamilyMember {

	private int actionValue = 0;
	private String owner;
	private boolean busy;
	
	public ClientFamilyMember(String owner){
		this.owner = owner;
		this.busy = false;
	}
	
	public void setBusyFlag(boolean busyFlag){
		this.busy = busy;
	}
	
	public void setActionValue(int actionValue){
		this.actionValue = actionValue;
	}
	
	public boolean isBusy(){
		return this.busy;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		if(busy==true)
			tmp.append(" > ");
		else
			tmp.append("   ");
		tmp.append("actionValue :"+this.actionValue+"\n");
		return new String(tmp);
	}
	
}
