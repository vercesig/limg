package it.polimi.ingsw.GC_32.Client.Game;

import java.util.ArrayList;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class ClientBoard {

	private ArrayList<ClientRegion> region;
	int blackDice;
	int whiteDice;
	int orangeDice;
	
	public ClientBoard(JsonObject boardPacket){
		this.region = new ArrayList<ClientRegion>();
		Iterator<Member> regions = boardPacket.iterator();
		regions.forEachRemaining(region -> {
			String regionType = region.getName();
			JsonArray actionSpaces = Json.parse(region.getValue().asString()).asArray();
			
			this.region.add(new ClientRegion(regionType,actionSpaces));			
		});	
	}
	
	public void setDiceValue(int blackValue, int whiteValue, int orangeValue){
		this.blackDice = blackValue;
		this.whiteDice = whiteValue;
		this.orangeDice = orangeValue;
	}
	
	public String toString(){
		ArrayList<ClientRegion> towerList = new ArrayList<ClientRegion>(region.subList(4, region.size()));
		// dimensione delle torri
		int towerWidth = 56;
		String title = "LORENZO IL MAGNIFICO";
		
		StringBuilder tmpTower = new StringBuilder();
		
		// title
		tmpTower.append("+");
		for(int i=0; i<towerWidth*towerList.size() - 6; i++){
			tmpTower.append("-");
		}
		tmpTower.append("+\n|");
		int titlePosition = (towerWidth*towerList.size() - title.length() -6)/2;
		for(int i=0; i<titlePosition; i++){
			tmpTower.append(" ");
		}
		tmpTower.append(title);
		for(int i=0; i<titlePosition; i++){
			tmpTower.append(" ");
		}
		tmpTower.append("|\n");
		tmpTower.append("+");
		for(int i=0; i<towerWidth*towerList.size() - 6; i++){
			tmpTower.append("-");
		}
		tmpTower.append("+\n");		
			
		// ********************************** towers
		towerList.forEach(tower -> {
			int tmpTowerWidth = towerWidth;
			tmpTower.append("|");
			int numberOfDashes = (tmpTowerWidth - tower.getType().length())/2;
			for(int i=0; i<numberOfDashes-2; i++){ 
				tmpTower.append("-");
			}
			tmpTower.append(" "+tower.getType()+" ");
			for(int i=0; i<numberOfDashes-2; i++){
				tmpTower.append("-");
			}
			tmpTower.append("|");
			});
		tmpTower.append("\n");

		int numberOfActionSpaces = towerList.get(0).getActionSpaceList().size();
		String[] infoContainerMask = {"regionID","actionSpaceID","actionValue","singleFlag","bonus","occupants","card"};
		
		for(int i=numberOfActionSpaces-1; i>=0; i--){ // actionSpace
			for(int w=0; w<infoContainerMask.length; w++){ // informations
				for(int j=0; j<towerList.size(); j++){ // tower
					String item = towerList.get(j).getActionSpaceList().get(i).getInfoContainer()[w];
					String field = infoContainerMask[w];
					tmpTower.append("| "+field+": "+item);
					int numberOfWhiteSpaces = towerWidth - item.length() - field.length() - 6;
					while(numberOfWhiteSpaces>0){
						tmpTower.append(" ");
						numberOfWhiteSpaces--;
					}
					tmpTower.append("|");
				}
				tmpTower.append("\n");
			}
			for(int j=0; j<towerList.size(); j++){
				tmpTower.append("|");
				int numberOfDashes = towerWidth - 3;
				while(numberOfDashes>0){
					tmpTower.append("-");
					numberOfDashes--;
				}
				tmpTower.append("|");
			}
			tmpTower.append("\n");
		}
		
		int halfBoardWidth = (towerWidth*towerList.size())/2;
		
		tmpTower.append("|");
		String productionTitle = region.get(0).getType();
		int numberOfDashes = (halfBoardWidth - productionTitle.length())/2 -3;
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append(" "+productionTitle+" ");
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append("||");
		String councilTitle = region.get(2).getType();
		numberOfDashes = (halfBoardWidth - councilTitle.length())/2 - 3;
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append(" "+councilTitle+" ");
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append("-|\n");
		
		int singleActionSpaceWidth = halfBoardWidth/3;
		int multipleActionSpaceWidth = halfBoardWidth - singleActionSpaceWidth;
		
		for(int w=0; w<infoContainerMask.length-1; w++){
			String field = infoContainerMask[w];
			String item = region.get(0).getActionSpaceList().get(0).getInfoContainer()[w];
			tmpTower.append("| "+field+": "+item);
			int numberOfWhiteSpaces = singleActionSpaceWidth - field.length() - item.length() - 4;
			while(numberOfWhiteSpaces>0){
				tmpTower.append(" ");
				numberOfWhiteSpaces--;
			}
			tmpTower.append("|");
			item = region.get(0).getActionSpaceList().get(1).getInfoContainer()[w];
			tmpTower.append("| "+field+": "+item);
			numberOfWhiteSpaces = multipleActionSpaceWidth - field.length() - item.length() - 8;
			while(numberOfWhiteSpaces>0){
				tmpTower.append(" ");
				numberOfWhiteSpaces--;
			}
			tmpTower.append("|");
			item = region.get(2).getActionSpaceList().get(0).getInfoContainer()[w];
			tmpTower.append("| "+field+": "+item);
			numberOfWhiteSpaces = halfBoardWidth - field.length() - item.length() - 7;
			while(numberOfWhiteSpaces>0){
				tmpTower.append(" ");
				numberOfWhiteSpaces--;
			}
			tmpTower.append("|\n");	
		}
		
		int marketActionSpaceWidth = halfBoardWidth/4 + 2;
		
		tmpTower.append("|");
		String harvastTitle = region.get(1).getType();
		numberOfDashes = (halfBoardWidth - harvastTitle.length())/2 -3;
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append(" "+harvastTitle+" ");
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append("-||");
		String marketTitle = region.get(3).getType();
		numberOfDashes = (halfBoardWidth - marketTitle.length())/2 - 3;
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append(" "+marketTitle+" ");
		for(int i=0; i<numberOfDashes; i++){
			tmpTower.append("-");
		}
		tmpTower.append("-|\n");
		
		for(int w=0; w<infoContainerMask.length-1; w++){
			String field = infoContainerMask[w];
			String item = region.get(1).getActionSpaceList().get(0).getInfoContainer()[w];
			tmpTower.append("| "+field+": "+item);
			int numberOfWhiteSpaces = singleActionSpaceWidth - field.length() - item.length() - 4;
			while(numberOfWhiteSpaces>0){
				tmpTower.append(" ");
				numberOfWhiteSpaces--;
			}
			tmpTower.append("|");
			item = region.get(1).getActionSpaceList().get(1).getInfoContainer()[w];
			tmpTower.append("| "+field+": "+item);
			numberOfWhiteSpaces = multipleActionSpaceWidth - field.length() - item.length() - 8;
			while(numberOfWhiteSpaces>0){
				tmpTower.append(" ");
				numberOfWhiteSpaces--;
			}
			tmpTower.append("|");
			for(int j=0; j<region.get(3).getActionSpaceList().size(); j++){
				item = region.get(3).getActionSpaceList().get(j).getInfoContainer()[w];
				tmpTower.append("| "+field+": "+item);
				numberOfWhiteSpaces = marketActionSpaceWidth - field.length() - item.length() - 7;
				while(numberOfWhiteSpaces>0){
					tmpTower.append(" ");
					numberOfWhiteSpaces--;
				}
			}
			tmpTower.append(" |\n");	
		}
		
		
		return new String(tmpTower);
	}
	
	public ArrayList<ClientRegion> getRegionList(){
		return this.region;
	}
}
