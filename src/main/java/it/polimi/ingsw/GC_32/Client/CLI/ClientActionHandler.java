package it.polimi.ingsw.GC_32.Client.CLI;

import java.io.PrintWriter;
import java.util.Scanner;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Client.CLI.CardCli;
import it.polimi.ingsw.GC_32.Client.Game.ClientCardRegistry;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Utils.Tuple;

public class ClientActionHandler{
    private ActionEffectContext actionContext;
    private PrintWriter out;
    private Scanner in;
    
    public ClientActionHandler(ActionEffectContext actionContext){
        this.actionContext = actionContext;
        this.out = actionContext.out;
        this.in = actionContext.in;
    }
    
    public JsonObject handleTower(boolean flagRegion, int regionID, int indexCost){
        String command;
        int chosenRegionID = 0;
        int chosenSpaceID = 0;
        boolean nullAction = false;
        if(flagRegion){
            out.println("you can select any one tower\nenter the regionID where you want to perform your bonus action. [4-7]");
            while(true){
                command = in.nextLine();
                try{
                    if(Integer.parseInt(command) < 4 && Integer.parseInt(command) > 7){
                        out.println("please, type a valid regionID");
                    }else{
                        chosenRegionID = Integer.parseInt(command);
                        break;
                    }
                }catch(NumberFormatException e){
                    if("q".equals(command)){
                        nullAction = true;
                        break;
                    }
                    System.out.println("type a valid number");
                }
            }
        } else {
            out.println("you can perform the action only on the region number "+regionID);
            chosenRegionID = regionID;
        }
        Tuple<Integer, Boolean> spaceResponse = getActionSpaceID(chosenSpaceID, 0, 3);
        chosenSpaceID = spaceResponse.getFirstArg();
        nullAction = spaceResponse.getSecondArg();
        if(!nullAction){
            out.println("Development card on this tower layer: ");              
            String cardName = actionContext.client.getBoard()
                                                  .getRegionList().get(chosenRegionID)
                                                  .getActionSpaceList().get(chosenSpaceID)
                                                  .getCardName();
                                        
            JsonObject card = ClientCardRegistry.getInstance().getDetails(cardName);
            
            if(card==null){
                out.println("no card on this tower layer");
                return null;
            }
            out.println(CardCli.print(cardName, card));

            if(card.get("cost")!=null){
                JsonArray costList = card.get("cost").asArray();
                if(costList.size() == 1){
                    indexCost = 0;
                } else {
                    out.println("Choose one cost of the card: ");
                    for(JsonValue js : costList){
                            out.println("> "+new ResourceSet(js.asObject()).toString() + " ");
                        }
                    out.println("type 0 or 1");
                    indexCost = getIntWithQuit(costList.size()).getFirstArg();
                }
            }
        }
        return makeContextPayload(chosenRegionID, chosenSpaceID, indexCost, 0, nullAction);
    }
    
    public JsonObject handleHarvest(boolean chooseServants, int maxServants){
        int chosenRegionID = 1;
        int chosenSpaceID = 0;
        int servants = 0;
        boolean nullAction = false;
        Tuple<Integer, Boolean> spaceResponse = getActionSpaceID(chosenSpaceID, 0, 1);
        chosenSpaceID = spaceResponse.getFirstArg();
        nullAction = spaceResponse.getSecondArg();
        if(chooseServants && !nullAction){
            out.println("ok, How many servant do you use?");
            Tuple<Integer, Boolean> servantResponse = getIntWithQuit(maxServants);
            servants = spaceResponse.getFirstArg();
            nullAction = spaceResponse.getSecondArg();
        }
        return makeContextPayload(chosenRegionID, chosenSpaceID, 0, servants, nullAction);
    }
    
    public JsonObject handleProduction(boolean chooseServants, int maxServants){
        int chosenRegionID = 0;
        int chosenSpaceID = 0;
        boolean nullAction = false;
        int servants = 0;
        Tuple<Integer, Boolean> spaceResponse = getActionSpaceID(chosenSpaceID, 0, 1);
        chosenSpaceID = spaceResponse.getFirstArg();
        nullAction = spaceResponse.getSecondArg();
        if(chooseServants && !nullAction){  // new feature
            out.println("ok, How many servant do you use?");
            Tuple<Integer, Boolean> servantResponse = getIntWithQuit(maxServants);
            servants = spaceResponse.getFirstArg();
            nullAction = spaceResponse.getSecondArg();
        }   
        return makeContextPayload(chosenRegionID, chosenSpaceID, 0, servants, nullAction);
    }
    
    public Tuple<Integer, Boolean> getActionSpaceID(int defaultSpaceID, int min, int max){
        String command;
        int chosenSpaceID = defaultSpaceID;
        boolean nullAction = false;
        out.println("ok, now type the spaceID where you would place your pawn");
        while(true){
            command = in.nextLine();
            try{
                if(Integer.parseInt(command) < min || Integer.parseInt(command) > max){
                    out.println("action space with that id does not exist\ntype a number between " + min + "-" + max);
                }else{
                    chosenSpaceID = Integer.parseInt(command);
                    break;
                }
            }catch(NumberFormatException e){
                if("q".equals(command)){
                    nullAction = true;
                    break;
                } else {
                    out.println("type a valid number");
                }
            }
        }
        return new Tuple<>(chosenSpaceID, nullAction);
    }
    
    public JsonObject makeContextPayload(int chosenRegionID, int chosenSpaceID, 
                                         int indexCost, int servants,
                                         boolean nullAction){
        JsonObject CONTEXTREPLYpayloadinfo = new JsonObject();
        CONTEXTREPLYpayloadinfo.add("REGIONID", chosenRegionID);
        CONTEXTREPLYpayloadinfo.add("SPACEID", chosenSpaceID);
        CONTEXTREPLYpayloadinfo.add("COSTINDEX", indexCost);
        CONTEXTREPLYpayloadinfo.add("CHOSESERVANT", servants); //new
        CONTEXTREPLYpayloadinfo.add("NULLACTION", nullAction);
        return CONTEXTREPLYpayloadinfo;
    }
    
    public Tuple<Integer, Boolean> getIntWithQuit(int max){
        String command;
        while(true){
            command = in.nextLine();
            try{
                if(Integer.parseInt(command) < 0 || Integer.parseInt(command) > max){
                    return new Tuple<>(Integer.parseInt(command), false);
                } else {
                    out.println("please, type a valid number");
                }
            } catch(NumberFormatException e){
                if("q".equals(command)){
                    return new Tuple<>(0, true);
                } else {
                    out.println("type a valid number");
                }
            }
        }
    }
}