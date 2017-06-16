package it.polimi.ingsw.GC_32.Server.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Board.TowerRegion;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class MoveCheckerTest {
	MoveChecker moveChecker;
	Player p;
	Board b;
	Action a1, a2, a3;
	JsonObject ja, jb, jc;
	DevelopmentCard card;
	
	@Before
	public void initTest(){
		p = new Player();
		b = new Board();
		a1 = new Action("TEST", 3, 0, 5); // Tower
		a1.setAdditionalInfo(new JsonObject().add("FAMILYMEMBER_ID" , 0));
		a2 = new Action("TEST", 1, 1, 1); // Harvest multiple
		a3 = new Action("TEST", 3, 0, 2); // Council
		//b.setTowerRegion(2);
		this.moveChecker = new MoveChecker();
		ja = new JsonObject().add("MILITARY_POINTS", 10).add("WOOD", 1).add("STONE", 3);
		jb = new JsonObject().add("MILITARY_POINTS", 2).add("WOOD", 4);
		jc = new JsonObject().add("SERVANTS", 5).add("WOOD", 1);
		
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
		assertEquals(false, moveChecker.checkMove(b, p, a1));
		b.getRegion(1).getActionSpace(1).addFamilyMember(p.getFamilyMember()[1]);
		Action act = new Action("TEST", 1, 0, 1);
		JsonObject grey = new JsonObject();
		grey.add("FAMILYMEMBER_ID", 0);
		act.setAdditionalInfo(grey);
		assertEquals(true, moveChecker.checkMove(b, p, act));
	}
	
	@Test
	public void checkIsFreeSingleSpaceTest(){
		assertEquals(true, moveChecker.checkIsFreeSingleSpace(b, p, a1));
		b.getRegion(5).getActionSpace(0).addFamilyMember(p.getFamilyMember()[0]);
		b.getRegion(5).getActionSpace(1).addFamilyMember(p.getFamilyMember()[1]);
		b.getRegion(1).getActionSpace(1).addFamilyMember(p.getFamilyMember()[2]);
		b.getRegion(2).getActionSpace(0).addFamilyMember(p.getFamilyMember()[3]);

		assertEquals(false, moveChecker.checkIsFreeSingleSpace(b, p, a1));
		assertEquals(true, moveChecker.checkIsFreeSingleSpace(b, p, a2));
		assertEquals(true, moveChecker.checkIsFreeSingleSpace(b, p, a3));
		assertEquals(false, moveChecker.checkIsFreeSingleSpace(b, p, new Action("",0,1,5)));
	}
	
	@Test
	public void checkCoinForTributeTest(){
		p.getResources().addResource("WOOD", 1);
		b.getRegion(5).getActionSpace(3).addFamilyMember(p.getFamilyMember()[1]);
		System.out.println(p.toString());
		assertEquals(false, moveChecker.checkMove(b, p, a1));
		p.getResources().addResource("MILITARY_POINTS", 3);
		p.getResources().addResource("COINS", 3);
		
		assertEquals(true, moveChecker.checkMove(b, p, a1));
		p.getResources().addResource("SERVANTS", 5);
		Player p2 = new Player();
		p.getResources().addResource("COINS", -3);
		b.getRegion(5).getActionSpace(1).addFamilyMember(p2.getFamilyMember()[1]);
		assertEquals(false, moveChecker.checkMove(b, p, a1));
		p.getResources().addResource("COINS", 6);
		assertEquals(true, moveChecker.checkMove(b, p, a1));
	}
	
	@Test
	public void checkActionValueTest(){
		b.getRegion(5).getActionSpace(0).addFamilyMember(p.getFamilyMember()[0]);
		
		assertEquals(false, moveChecker.checkMove(b, p, a1));
		assertEquals(false, moveChecker.checkMove(b, p, a2));
		assertEquals(true, moveChecker.checkMove(b, p, a3));
		((TowerRegion) b.getRegion(4)).getTowerLayers()[3].setCard(card); 
		assertEquals(false, moveChecker.checkMove(b, p, new Action("", 1, 3, 4)));
		assertEquals(false, moveChecker.checkMove(b, p, new Action("", 3, 2, 4)));
		assertEquals(true, moveChecker.checkMove(b, p, new Action("", 7, 3, 4)));
	}
}
