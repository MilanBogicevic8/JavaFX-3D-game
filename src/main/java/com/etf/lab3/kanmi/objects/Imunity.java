package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.security.SecurityPermission;

public class Imunity extends Group {

    private static final double IMUNITY_RADIUS=5.;
    private static final double IMUNITY_HEIGHT=10.;


    public Imunity(double x,double z){
        Cylinder cylinder1=new Cylinder(IMUNITY_RADIUS,IMUNITY_HEIGHT);
        Cylinder cylinder2=new Cylinder(IMUNITY_RADIUS,IMUNITY_HEIGHT);

        cylinder2.setMaterial(new PhongMaterial(Color.RED));

        cylinder1.getTransforms().addAll(
                new Rotate(90, new Point3D(0, 0, 1))
        );
        cylinder2.getTransforms().addAll(
                new Translate(IMUNITY_HEIGHT,0,0),
                new Rotate(90,new Point3D(0, 0, 1))
        );

        Sphere sphere1=new Sphere(IMUNITY_RADIUS);
        Sphere sphere2=new Sphere(IMUNITY_RADIUS);

        sphere2.setMaterial(new PhongMaterial(Color.RED));

        sphere1.getTransforms().add(
                new Translate(-IMUNITY_HEIGHT/2, 0,0)
        );
        sphere2.getTransforms().add(
                new Translate(IMUNITY_HEIGHT/2+IMUNITY_HEIGHT, 0,0)
        );

        super.getChildren().add(cylinder1);
        super.getChildren().add(cylinder2);
        super.getChildren().add(sphere1);
        super.getChildren().add(sphere2);

        this.getTransforms().add(new Scale(0.5, 0.5, 0.5));

        this.setTranslateX(x);
        this.setTranslateZ(z);

    }
}
