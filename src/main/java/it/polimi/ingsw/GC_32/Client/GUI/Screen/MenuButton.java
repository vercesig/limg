package it.polimi.ingsw.GC_32.Client.GUI.Screen;


import java.util.ArrayList;

import javafx.scene.control.Button;

public class MenuButton extends Button{

	public MenuButton(String text){
		super();
		this.setId("button_idle");
		this.getStylesheets().add(this.getClass().getResource("/css/game_buttons.css").toExternalForm());
		this.setText(text);
		this.defaultButtonProperty().bind(this.focusedProperty());
		this.setOnMouseEntered(click -> this.setId("button_Entered"));
		this.setOnMouseExited(click -> this.setId("button_idle"));
		this.setOnMousePressed(click -> this.setId("button_Clicked"));
		this.setOnMouseReleased(click -> this.setId("button_idle"));
	}

	public static MenuButton FamilyContextButton(String text, ArrayList <FamilyMemberGUI> familiar){
		MenuButton button = new MenuButton(text);
		button.setOnMousePressed(click -> FamilyMemberContext.showFamily(familiar));
		return button;
	}
}
