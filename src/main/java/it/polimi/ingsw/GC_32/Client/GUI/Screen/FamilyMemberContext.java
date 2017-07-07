package it.polimi.ingsw.GC_32.Client.GUI.Screen;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;

public class FamilyMemberContext {
	 
	private Group family;
	
	public FamilyMemberContext(ArrayList <FamilyMemberGUI> familiar){ 
		
	       	this.family = new Group(); 
	       	familiar.forEach(member-> family.getChildren().add(member));
	       	
	        
	        PointLight light = new PointLight(Color.WHITE);
	        light.setTranslateX(0);
	        light.setTranslateY(100);
	        light.setTranslateZ(-150);

//	        family.getChildren().addAll(familyOne, familyTwo, familyThree, familyJolly, button, light);
	        family.getChildren().add(light);
	        family.setTranslateX(130);
	        family.setTranslateY(130);
	       family.setTranslateZ(-150);
	       	
	       
			for(int i=0; i<familiar.size(); i++){
				VBox vbox = new VBox();
				Button button = new Button("Move this!");
			//	button.setPadding(new Insets(10));
				vbox.getChildren().add(familiar.get(i).getInfo());
				shift(vbox, 0.5, i*100);
				shift(button, 0.5, i*70);
				family.getChildren().addAll(vbox, button);
			}

	
	        Scene window = new Scene(family, 700, 300);
	        window.setFill(Color.GREY);
	        window.setCamera(new PerspectiveCamera());
	        
	        Stage stage = new Stage();
	        stage.setTitle("Family Member view");
	        stage.setScene(window);
	        stage.setResizable(true);
	        stage.show();
	
		}
		
		private void shift(VBox vbox, double scale, int factor){
			vbox.setScaleX(scale);
			vbox.setScaleY(scale);

			vbox.setTranslateY(-80);
			vbox.setTranslateX(factor - 50);
			vbox.setTranslateZ(0);

		//	vbox.setAlignment(Pos.BOTTOM_LEFT);
		}
		
		private void shift(Button button, double scale, int factor){
			button.setScaleX(scale);
			button.setScaleY(scale);
		//	button.setPadding(new Insets(150));
			button.setTranslateY(100);
			button.setTranslateX(factor+100);
			button.setTranslateZ(0);

		}
	
	
		
		
/*		 private Cylinder set(int radius, int height, int factor, Color color){

		     Cylinder family= new Cylinder(radius, height);
			 PhongMaterial colored = new PhongMaterial(color);
			 family.setMaterial(colored);
			 family.setTranslateX(0+
					 factor);
			 family.setTranslateY(40);
			 family.setTranslateZ(0);
			 family.setDrawMode(DrawMode.FILL);
			 return family;
		 }
		*/ 
}