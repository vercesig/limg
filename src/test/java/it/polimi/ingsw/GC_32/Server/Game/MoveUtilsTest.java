package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
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
	}
	
	@Test
	public void checkFamilyColorTest(){
	    assertEquals(true, MoveUtils.familyColor(b, p, new Action("TEST", 3, 1, 2)));
	    assertEquals(true, MoveUtils.familyColor(b, p, new Action("TEST", 3, 1, 3)));
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
	
	@Test()
	public void checkCardCostTest(){
	    assertEquals(true, MoveUtils.checkCardCost(b, p, harvestAction));
	    assertEquals(false, MoveUtils.checkCardCost(b, p, towerAction));
	    p.getResources().setResource("WOOD", 1);
	    assertEquals(true, MoveUtils.checkCardCost(b, p, towerAction));
	    
	    b.getTowerRegion()[1].getTowerLayers()[1].setCard(new DevelopmentCard("Card", 1, "TERRITORYCARD", 0));
	    p.getResources().setResource("WOOD", 0);
	    assertEquals(true, MoveUtils.checkCardCost(b, p, new Action("TEST", 3, 1, 5)));
	    
	    DevelopmentCard requirementsCard = new DevelopmentCard("Card", 1, "TERRITORYCARD", 0);
	    requirementsCard.setRequirments(Json.parse("{\"MILITARY_POINTS\": 3}").asObject());
	    p.getResources().setResource("MILITARY_POINTS", 3);
	    b.getTowerRegion()[1].getTowerLayers()[2].setCard(requirementsCard);
	    assertEquals(true, MoveUtils.checkCardCost(b, p, new Action("TEST", 3, 2, 5)));
	    
	    Action bogusAction = new Action("TEST", 3, 2, 5);
	    bogusAction.setAdditionalInfo(new JsonObject().add("COSTINDEX", 10));
	    assertEquals(true, MoveUtils.checkCardCost(b, p, bogusAction));
	    
	    DevelopmentCard bonusCard = new DevelopmentCard("Card", 1, "TERRITORYCARD", 0);
	    bonusCard.registerCost(new JsonObject().add("WOOD", 2));
	    b.getTowerRegion()[1].getTowerLayers()[3].setCard(bonusCard);
	    p.getResources().setResource("WOOD", 2);
	    Action bonusAction = new Action("TEST", 3, 3, 5);
	    bonusAction.setAdditionalInfo(new JsonObject().add("BONUSRESOURCE", new JsonObject().add("WOOD", 1)));
	    assertEquals(true, MoveUtils.checkCardCost(b, p, bonusAction));
	    assertEquals(1, p.getResources().getResource("WOOD"));
	}
	
	@Test
	public void checkPersonalBoardRequirementTest(){
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    
	    p.getPersonalBoard().addCard(card);
	    p.getPersonalBoard().addCard(card);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    p.getResources().setResource("MILITARY_POINTS", 3);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    
	    p.getPersonalBoard().addCard(card);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    p.getResources().setResource("MILITARY_POINTS", 7);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	        
	    p.getPersonalBoard().addCard(card);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    p.getResources().setResource("MILITARY_POINTS", 12);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	        
	    p.getPersonalBoard().addCard(card);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    p.getResources().setResource("MILITARY_POINTS", 18);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    
	    p.getResources().setResource("MILITARY_POINTS", 0);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    p.getFlags().put("NOMILITARYRULE", null);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    
	    p.getPersonalBoard().addCard(card);
	    assertEquals(false, MoveUtils.checkPersonalBoardRequirement(b, p, towerAction));
	    
	    DevelopmentCard newCard = new DevelopmentCard("TEST", 1, "BUILDINGCARD", 0);
	    b.getTowerRegion()[0].getTowerLayers()[0].setCard(newCard);
	    assertEquals(true, MoveUtils.checkPersonalBoardRequirement(b, p, new Action("TEST", 3, 0, 4)));
	}
	
	@Test
	public void checkServantsTest(){
	    Action testAction = new Action("TEST", 3, 2, 4);
	    assertEquals(true, MoveUtils.checkServants(b, p, towerAction));
	    assertEquals(false, MoveUtils.checkServants(b, p, testAction));
	    p.getResources().setResource("SERVANTS", 2);
	    assertEquals(true, MoveUtils.checkServants(b, p, testAction));
	    p.getResources().setResource("SERVANTS", 2);
	    p.getFlags().put("DOUBLESERVANTS", null);
	    assertEquals(false, MoveUtils.checkServants(b, p, testAction));
	    p.getResources().setResource("SERVANTS", 5);
	    assertEquals(true, MoveUtils.checkServants(b, p, testAction));
	    assertEquals(1, p.getResources().getResource("SERVANTS"));
	}
	
	@Test
	public void checkBlockedZoneTest(){
	    assertEquals(false, MoveUtils.checkBlockedZone(2, towerAction));
	    assertEquals(false, MoveUtils.checkBlockedZone(2, new Action("TEST", 3, 0, 1)));
	    assertEquals(false, MoveUtils.checkBlockedZone(2, new Action("TEST", 3, 1, 3)));
	    assertEquals(true, MoveUtils.checkBlockedZone(2, new Action("TEST", 3, 1, 1)));
	    assertEquals(true, MoveUtils.checkBlockedZone(3, new Action("TEST", 3, 2, 3)));
        assertEquals(false, MoveUtils.checkBlockedZone(3, new Action("TEST", 3, 1, 1)));
        assertEquals(false, MoveUtils.checkBlockedZone(4, new Action("TEST", 3, 2, 3)));
	}
	
	@Test
	public void checkAddActionSpaceBonus(){
	    b.getMarketRegion().getActionSpace(0)
	                       .setBonus(new ResourceSet(new JsonObject().add("COINS", 5)));
	    p.getResources().setResource("COINS", 0);
	    MoveUtils.addActionSpaceBonus(b, p, new Action("TEST", 3, 0, 3));
	    assertEquals(5, p.getResources().getResource("COINS"));
	    p.getResources().setResource("COINS", 0);
	    p.getFlags().put("LESSRESOURCE", new JsonArray().add(Json.value("COINS")));
	    MoveUtils.addActionSpaceBonus(b, p, new Action("TEST", 3, 0, 3));
	    assertEquals(4, p.getResources().getResource("COINS"));
	}
	
	@Test
	public void checkNotFoundCardTest(){
	    assertEquals(true, MoveUtils.checkNotFoundCard(b, harvestAction));
	    assertEquals(false, MoveUtils.checkNotFoundCard(b, new Action("TEST", 3, 3, 6)));
	    assertEquals(true, MoveUtils.checkNotFoundCard(b, new Action("TEST", 3, 0, 5)));
	}
	
	@Test
	public void checkValidIds(){
	    for(int i = 0; i < 4; i++){
	        assertEquals(true, MoveUtils.checkValidRegionID(b, p, new Action("TEST", 3, 0, i)));
	    }
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 0, 0)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 1, 0)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 0, 1)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 1, 1)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 0, 2)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 0, 3)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 1, 3)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 2, 3)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 3, 3)));
	    for(int i = 0; i < 4; i++){
	        assertEquals(true, MoveUtils.checkValidRegionID(b, p, new Action("TEST", 3, 0, i+4)));
	        for(int j = 0; j < 4; j++){
	            assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, j, i+4)));
	        }
	    }
	    assertEquals(false, MoveUtils.checkValidRegionID(b, p, new Action("TEST", 3, 0, 8)));
	    assertEquals(true, MoveUtils.checkValidActionSpaceID(b, p, new Action("TEST", 3, 1, 7)));
	}
}