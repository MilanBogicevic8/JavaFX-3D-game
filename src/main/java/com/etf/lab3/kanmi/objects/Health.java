package com.etf.lab3.kanmi.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Health extends Group {

    private static final double HEALTH_WIDTH=20.;
    private static final double HEALTH_HEIGHT=5.;
    private static final double HEALTH_DEPTH=3.;


    public Health(double x,double z){

        Box b1=new Box(HEALTH_WIDTH,HEALTH_HEIGHT,HEALTH_DEPTH);
        Box b2=new Box(HEALTH_WIDTH,HEALTH_HEIGHT,HEALTH_DEPTH);

        b1.setMaterial(new PhongMaterial(Color.RED));
        b2.setMaterial(new PhongMaterial(Color.RED));


        b2.getTransforms().add(
                new Rotate(90)
        );

        Sphere sphere=new Sphere(HEALTH_WIDTH/2.+5);
        PhongMaterial mat=new PhongMaterial(new Color(1,0,0,0));
        sphere.setMaterial(mat);

        super.getChildren().add(b1);
        super.getChildren().add(b2);
        super.getChildren().add(sphere);

        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
