package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;

public class TowerLayerTest{
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 
	
	@Mock
	public DevelopmentCard devCard;
	
	@Test
	public void checkCardSetting(){
		TowerLayer towerLayer = new TowerLayer(0,0);
		towerLayer.setCard(this.devCard);
		assertEquals(towerLayer.takeCard(), this.devCard);
	}
}