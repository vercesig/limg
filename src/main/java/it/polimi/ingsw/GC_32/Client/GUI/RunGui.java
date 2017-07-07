package it.polimi.ingsw.GC_32.Client.GUI;

import it.polimi.ingsw.GC_32.Client.GUI.Screen.GameScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunGui extends Application {
	
	private GameScreen game;
	//private ClientGUI clientGui;
	
	public RunGui(){
		//this.clientGui = new ClientGui();
	
//		this.clientGui = client;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
	     //Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/clientLayout.fxml"));
	     
		this.game = new GameScreen(null);
	     Scene scene = new Scene(this.game, 1200.0, 680.0);
	     stage.setTitle("Lorenzo il Magnifico");
	     stage.setScene(scene);
	     stage.show();
	    // stage.setResizable(false);
	}

	public static void main(String [] args){
		RunGui run = new RunGui();
		launch();
	}
}
