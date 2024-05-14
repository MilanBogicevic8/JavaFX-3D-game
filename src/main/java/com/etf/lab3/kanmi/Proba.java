package com.etf.lab3.kanmi;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Proba extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Group main_root=new Group();
        Group root=new Group();
        main_root.getChildren().addAll(root);

        ParallelCamera pc=new ParallelCamera();
        pc.setFarClip(10000);
        pc.getTransforms().addAll(
                new Translate(0, 0,200),
                new Rotate(-45, Rotate.X_AXIS)

        );


        Cylinder cylinder=new Cylinder(200  , 20);
        root.getChildren().add(cylinder);
        root.getTransforms().add(new Translate(375, 375));


        Cylinder c2=new Cylinder(200,40);
        Cylinder c3=new Cylinder(100,20);
        PhongMaterial p2=new PhongMaterial();
        p2.setDiffuseColor(Color.ORANGE);
        PhongMaterial p3=new PhongMaterial();
        p3.setDiffuseColor(Color.RED);
        c3.getTransforms().addAll(new Translate(100,30,0));

        c2.setMaterial(p2);
        c3.setMaterial(p3);
        Group su_root=new Group(c2,c3);
        //root.getChildren().addAll(su_root);
        main_root.getChildren().addAll(su_root);


        Scene scene=new Scene(main_root,750,750);
        //scene.setCamera(pc);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
