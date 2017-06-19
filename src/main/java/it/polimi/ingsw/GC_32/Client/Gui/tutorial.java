package it.polimi.ingsw.GC_32.Client.Gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class tutorial extends Application {
 
    @Override
    public void start(Stage stage) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
        }
 
        Group root = new Group();
        BorderPane pane = new BorderPane();
        
        Scene scene = new Scene(pane, 500, 400);
        scene.setFill(Color.color(0.2, 0.2, 0.2, 1.0));
 
        HBox hbox = new HBox();
  
        hbox.setTranslateX(100);
        hbox.setTranslateY(100);
        hbox.setTranslateZ(-150);

        PhongMaterial phongMaterial = new PhongMaterial(Color.color(1.0, 0.2, 0.2));
       
        PhongMaterial territory = new PhongMaterial();
        territory.setDiffuseMap((new Image("/Asset/LorenzoCards_compressed_png/devcards_f_en_c_1.png")));
        PhongMaterial coin = new PhongMaterial();
        coin.setDiffuseMap(new Image("/Asset/Lorenzo_Punchboard_CUT_compressed/coin_1_back_.png"));
        PhongMaterial card = new PhongMaterial();
        card.setDiffuseMap(new Image("/Asset/Lorenzo_Leaders_compressed/leaders_f_c_12.jpg"));
        
        Cylinder cylinder1 = new Cylinder(40, 100);
        cylinder1.setMaterial(phongMaterial);

        Cylinder cylinder2 = new Cylinder(40, 100);
        cylinder2.setMaterial(phongMaterial);
        
        Box box1 = new Box(40, 100, 10);
        Cylinder box2 = new Cylinder(10, 10);
        Box box3 = new Box(40, 100, 10);
        
        box2.setRotate(-180);
        box1.setMaterial(territory);
        box2.setMaterial(coin);
        box3.setMaterial(card);

        
        /*SubScene noMsaa = createSubScene("MSAA = false", cylinder1,
                Color.TRANSPARENT,
                new PerspectiveCamera(), false);
        hbox.getChildren().add(noMsaa);*/
        cylinder1.setTranslateX(0);
        cylinder1.setTranslateY(0);
        cylinder1.setTranslateZ(0);
 
        cylinder1.setDrawMode(DrawMode.FILL);
        
        cylinder2.setTranslateX(10);
        cylinder2.setTranslateY(0);
        cylinder2.setTranslateZ(0);
        
        cylinder2.setDrawMode(DrawMode.FILL);

        /* SubScene msaa = createSubScene("MSAA = true", cylinder2,
                Color.TRANSPARENT,
                new PerspectiveCamera(), true);
        hbox.getChildren().add(msaa);
 
        Slider slider = new Slider(0, 360, 0);
        slider.setBlockIncrement(1);
        slider.setTranslateX(425);
        slider.setTranslateY(625);
        cylinder1.rotateProperty().bind(slider.valueProperty());
        cylinder2.rotateProperty().bind(slider.valueProperty());*/
        hbox.getChildren().addAll(cylinder1, cylinder2, box1, box2, box3);
        //root.getChildren().add(hbox);
        pane.getChildren().add(hbox);
        scene.setCamera(new PerspectiveCamera());
        stage.setScene(scene);
        stage.show();
    }
 
    private static Parent setTitle(String str) {
        final VBox vbox = new VBox();
        final Text text = new Text(str);
        text.setFont(Font.font("Times New Roman", 24));
        text.setFill(Color.WHEAT);
        vbox.getChildren().add(text);
        return vbox;
    }
 
    private static SubScene createSubScene(String title, Node node,
            Paint fillPaint, Camera camera, boolean msaa) {
        Group root = new Group();
 
        PointLight light = new PointLight(Color.RED);
      /*  light.setTranslateX(50);
        light.setTranslateY(-300);
        light.setTranslateZ(-400);*/
        PointLight light2 = new PointLight(Color.color(0.6, 0.3, 0.4));
 /*       light2.setTranslateX(400);
        light2.setTranslateY(0);
        light2.setTranslateZ(-400);*/
 
        AmbientLight ambientLight = new AmbientLight(Color.color(0.6, 0.3, 0.4));
        node.setRotationAxis(new Point3D(2, 1, 0).normalize());
        node.setTranslateX(0);
        node.setTranslateY(0);
        root.getChildren().addAll(setTitle(title), ambientLight, 
                                  light, light2, node);
 
        SubScene subScene = new SubScene(root, 500, 400, true, 
                msaa ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
        subScene.setFill(fillPaint);
        subScene.setCamera(camera);
 
        return subScene;
    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}