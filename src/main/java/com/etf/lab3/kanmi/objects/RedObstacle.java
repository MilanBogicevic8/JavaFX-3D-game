package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class RedObstacle extends Group {

    public RedObstacle(int x,int z){
        Cylinder cylinder=new Cylinder(5, 20);
        cylinder.setMaterial(new PhongMaterial(Color.RED));

        cylinder.getTransforms().add(
                new Translate(0, 10,0)
        );

        Sphere sphere=new Sphere(5);
        sphere.setMaterial(new PhongMaterial(Color.RED));

        sphere.getTransforms().add(
                new Translate(0,0,0)
        );

        Cylinder eye1=new Cylinder(1,2);
        Cylinder eye2=new Cylinder(1,2);
        eye1.setMaterial(new PhongMaterial(Color.BLACK));
        eye2.setMaterial(new PhongMaterial(Color.BLACK));


        eye1.getTransforms().addAll(
                new Rotate(60,new Point3D(0, 1, 0)),
                new Rotate(90),
                new Translate(0,-4.5,0)
        );

        eye2.getTransforms().addAll(
                new Rotate(120,new Point3D(0, 1, 0)),
                new Rotate(90),
                new Translate(0,-4.5,0)
        );


        super.getChildren().add(cylinder);
        super.getChildren().add(sphere);
        super.getChildren().add(eye1);
        super.getChildren().add(eye2);

        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
