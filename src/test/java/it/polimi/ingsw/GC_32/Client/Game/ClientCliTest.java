package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class ClientCliTest{
    
    private JsonObject jObject;

    @Before
    public void initTest(){
        jObject = spy(new JsonObject());
        jObject.add("cost", new JsonArray().add(new JsonObject().add("WOOD", 2).add("STONE", 1)));
        jObject.add("requirements", new JsonObject().add("COINS", 5));
    }
    
    @Test
    public void checkEmptyCard(){
        assertEquals("EMPTY", CardCli.print("TEST", new JsonObject()));
    }
    
    @Test
    public void checkPrint(){

        CardCli.print("Test Card", jObject);
        verify(jObject, atLeastOnce()).get("cost");
        verify(jObject, atLeastOnce()).get("requirements");
        verify(jObject, atLeastOnce()).get("instantEffect");
        verify(jObject, atLeastOnce()).get("instantPayload");
        verify(jObject, atLeastOnce()).get("permanentEffect");
        verify(jObject, atLeastOnce()).get("permanentPayload");
    }
    
    @Test 
    public void checkLoadEffect(){
        JsonArray jEffectArray = spy(new JsonArray());
        jEffectArray.add("ADD").add("ADD");
        JsonArray jEffectPayloadArray = spy(new JsonArray());
        jEffectPayloadArray.add(new JsonObject().add("WOOD", 1))
                           .add(new JsonObject().add("STONE", 1));
        jObject.add("instantEffect", jEffectArray);
        jObject.add("instantPayload", jEffectPayloadArray);
        CardCli.print("Test_Card", jObject);
        verify(jEffectArray, atLeastOnce()).iterator();
        verify(jEffectPayloadArray, atLeastOnce()).iterator();
    }
}