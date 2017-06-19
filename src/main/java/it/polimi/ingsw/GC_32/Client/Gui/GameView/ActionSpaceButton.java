package it.polimi.ingsw.GC_32.Client.Gui.GameView;

import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;

public class ActionSpaceButton extends Button{
	
	int id, region;
	
	public ActionSpaceButton(int id, int region, String cssId){
		super();
		this.id = id;
		this.region = region;
		
		this.setId(cssId);
		this.getStylesheets().add(this.getClass().getResource("/css/actionSpace.css").toExternalForm());
		
		this.setOnAction(action -> {
			Light.Distant light = new Light.Distant();
			 light.setAzimuth(-135.0);

			 Lighting lighting = new Lighting();
			 lighting.setLight(light);
			 lighting.setSurfaceScale(5.0);
			 this.setEffect(lighting);
		});
	}
}
