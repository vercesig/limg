package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class MoveUtilsTest{
	Player p;
	Board b;
	Action towerAction, harvestAction, councilAction;
	JsonObject ja, jb, jc;
	DevelopmentCard card;
	
	@Before
	public void initTest() throws IOException{
		Setup setup = new Setup();
		setup.loadCard("test.json");
		b = new Board();
		p = new Player();
		
		towerAction = new Action("TEST", 3, 0, 5); // Tower
		towerAction.setAdditionalInfo(new JsonObject().add("FAMILYMEMBER_ID" , 0));
		harvestAction = new Action("TEST", 1, 1, 1); // Harvest multiple
		councilAction = new Action("TEST", 3, 0, 2); // Council
		
		card = new DevelopmentCard ("Card", 1, "TERRITORYCARD", 0);
		card.registerCost(new JsonObject().add("WOOD", 1));
		((TowerRegion) b.getRegion(5)).getTowerLayers()[0].setCard(card); 
		
		for(int i=0; i<1; i++){
			p.getPersonalBoard().addCard(card);
		}
		p.getResources().addResource("WOOD", 1);
	}
	
	@Test
	public void checkFamilyColorTest(){
		b.getRegion(5).getActionSpace(2).addFamilyMember(p.getFamilyMember()[2]);
		b.getRegion(1).getActionSpace(1).addFamilyMember(p.getFamilyMember()[1]);
		Action act = new Action("TEST", 1, 0, 1);
		JsonObject grey = new JsonObject();
		grey.add("FAMILYMEMBER_ID", 0);
		act.setAdditionalInfo(grey);
		assertEquals(true, MoveUtils.familyColor(b, p, act));
	}
	
	@Test
	public void checkIsFreeSingleSpaceTest(){
		assertEquals(true, MoveUtils.isFreeSingleSpace(b, p, towerAction));
		b.getRegion(5).getActionSpace(0).addFamilyMember(p.getFamilyMember()[0]);
		b.getRegion(5).getActionSpace(1).addFamilyMember(p.getFamilyMember()[1]);
		b.getRegion(1).getActionSpace(1).addFamilyMember(p.getFamilyMember()[2]);
		b.getRegion(2).getActionSpace(0).addFamilyMember(p.getFamilyMember()[3]);

		assertEquals(false, MoveUtils.isFreeSingleSpace(b, p, towerAction));
		assertEquals(true, MoveUtils.isFreeSingleSpace(b, p, harvestAction));
		assertEquals(true, MoveUtils.isFreeSingleSpace(b, p, councilAction));
		assertEquals(false, MoveUtils.isFreeSingleSpace(b, p, new Action("",0,1,5)));
	}

	@Test
	public void checkCoinForTributeTest(){
		assertEquals(true, MoveUtils.checkCoinForTribute(b, p, harvestAction));
		assertEquals(true, MoveUtils.checkCoinForTribute(b, p, towerAction));
		
		b.getTowerRegion()[1].placeFamilyMember(p.getFamilyMember()[2], 2);
		
		p.getResources().setResource("COINS", 2);
		assertEquals(false, MoveUtils.checkCoinForTribute(b, p, towerAction));
		
		p.getResources().setResource("COINS", 4);
		assertEquals(true, MoveUtils.checkCoinForTribute(b, p, towerAction));
		
		p.getFlags().put("NOTRIBUTE", null);
		p.getResources().setResource("COINS", 2);
		assertEquals(true, MoveUtils.checkCoinForTribute(b, p, towerAction));
	}
	
	@Test
	public void checkActionValueTest(){
		b.getRegion(5).getActionSpace(0).addFamilyMember(p.getFamilyMember()[0]);
		
		assertEquals(true, MoveUtils.checkActionValue(b, towerAction));
		assertEquals(false, MoveUtils.checkActionValue(b, harvestAction));
		assertEquals(true, MoveUtils.checkActionValue(b, councilAction));
		((TowerRegion) b.getRegion(4)).getTowerLayers()[3].setCard(card); 
		assertEquals(false, MoveUtils.checkActionValue(b, new Action("", 1, 3, 4)));
		assertEquals(false, MoveUtils.checkActionValue(b, new Action("", 3, 2, 4)));
		assertEquals(true, MoveUtils.checkActionValue(b, new Action("", 7, 3, 4)));

	}
}