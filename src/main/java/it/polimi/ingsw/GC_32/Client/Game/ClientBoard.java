package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class ClientBoard {

	private ArrayList<ClientRegion> region;
	
	public ClientBoard(JsonObject boardPacket){
		this.region = new ArrayList<ClientRegion>();
		Iterator<Member> regions = boardPacket.iterator();
		regions.forEachRemaining(region -> {
			String regionType = region.getName();
			JsonArray actionSpaces = Json.parse(region.getValue().asString()).asArray();
			
			this.region.add(new ClientRegion(regionType,actionSpaces));			
		});	
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		region.forEach(region -> tmp.append(region.toString()));
		return new String(tmp);
	}
	
}
