package it.polimi.ingsw.GC_32.Client.Gui;

import it.polimi.ingsw.GC_32.Client.Gui.GameView.GameScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainJX extends Application {

	@Override
	public void start(Stage stage) throws Exception {
	     //Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/clientLayout.fxml"));
	     VBox root = new GameScreen().getVbox();	
	     
	        Scene scene = new Scene(root, 800.0, 600.0);
	        stage.setTitle("FXML Welcome");
	        stage.setScene(scene);
	        stage.show();
		
	}

	public static void main (String[] args){
		launch();
	}
}
