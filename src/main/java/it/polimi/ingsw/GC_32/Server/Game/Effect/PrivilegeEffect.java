package it.polimi.ingsw.GC_32.Server.Game.Effect;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Common.Network.ServerContextMessageFactory;
import it.polimi.ingsw.GC_32.Common.Network.ServerMessageFactory;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Network.MessageManager;

public class PrivilegeEffect {
	
static EffectBuilder buildPrivilege = (JsonValue payload) -> {
				int number = payload.asObject().get("NUMBER").asInt();
		
		Effect e = (Board b, Player p, Action a) ->	{
			/*MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONTEXTMessage(
					ServerContextMessageFactory.buildPrivilegeMessage(number)));*/
		};
		return e;
	};
	
	public static void loadBuilder() {
		EffectRegistry.getInstance().registerBuilder("PRIVILEGE", buildPrivilege);
	}
}
