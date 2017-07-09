package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * this class is the client-side representation of the server-side concept of Region. Because client only show information on the screen, the information contained into 
 * this class (like all the classes of the client-side game model) is really less then the server-side equivalent class.
 * 
 * <ul>
 * <li>{@link #actionSpaces}: list of action spaces contained into this region</li>
 * <li>{@link #type}: the type of this region</li>
 * </ul>
 *
 * @see ClientActionSpace
 */

public class ClientRegion {

	private String type;
	private ArrayList<ClientActionSpace> actionSpaces;
	
	/**
	 * initialize the region with the type indicated and setting up its action spaces according to the content of the actionSpaces JsonArray passed as argument, which
	 * is directly taken from the GMSTRT message
	 * @param type the type of this region
	 * @param actionSpaces a JsonArray of JSON action spaces
	 */
	public ClientRegion(String type, JsonArray actionSpaces){
		this.type = type;
		this.actionSpaces = new ArrayList<ClientActionSpace>();
		
		actionSpaces.forEach(actionSpace -> {
			JsonObject slimActionSpace = actionSpace.asObject();
			ResourceSet bonus = null;
			if(slimActionSpace.get("BONUS").isObject()){
				bonus = new ResourceSet(slimActionSpace.get("BONUS").asObject());
			}
			int actionValue = slimActionSpace.getInt("ACTIONVALUE", 1);
			boolean single = slimActionSpace.getBoolean("SINGLE", true);
			int regionID = slimActionSpace.get("REGIONID").asInt();
			int actionspaceID = slimActionSpace.get("SPACEID").asInt();
			boolean blockFlag = slimActionSpace.getBoolean("BLOCKFLAG",false);
			this.actionSpaces.add(new ClientActionSpace(bonus,actionValue,single,regionID,actionspaceID,blockFlag));
		});
	}
	
	/**
	 * return a string representation of this region
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append("************************** "+type.toUpperCase()+"\n");
		actionSpaces.forEach(actionSpace -> tmp.append(actionSpace.toString()));
		return new String(tmp);
	}
	
	/**
	 * get the action space list of this region
	 * @return an ArrayList of action spaces
	 */
	public ArrayList<ClientActionSpace> getActionSpaceList(){
		return this.actionSpaces;
	}
	
	/**
	 * get the type of this region 
	 * @return the region type string
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * remove all the family member on all the action spaces of this region
	 */
	public void flushFamilyMember(){
		actionSpaces.forEach(actionSpace -> actionSpace.flushFamilyMember());
	}
}
