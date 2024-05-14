package com.etf.lab3.kanmi.objects;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Joker extends Group {

    public static final double HEIGHT=15.0/2./2.;
    public static final double WIDTH=15;
    public static final double DEPTH=5;

    public Joker(double x,double z){


        Box box1=new Box(WIDTH,HEIGHT,DEPTH);
        Box box2=new Box(WIDTH,HEIGHT,DEPTH);
        Box box3=new Box(WIDTH/2,HEIGHT,DEPTH);

        box1.setMaterial(new PhongMaterial(Color.GREEN));
        box2.setMaterial(new PhongMaterial(Color.GREEN));
        box3.setMaterial(new PhongMaterial(Color.GREEN));

        box2.getTransforms().addAll(
                new Rotate(90,new Point3D(0,1,0)),
                new Translate(WIDTH-WIDTH/2,0,0),
                new Translate(0, 0,WIDTH/2-DEPTH/2)
        );

        box3.getTransforms().addAll(
                new Rotate(90,new Point3D(0,1,0)),
                new Translate(WIDTH-WIDTH/2,0,0),
                new Translate(-5, 0,-WIDTH/2+DEPTH/2)
        );

        super.getChildren().add(box1);
        super.getChildren().add(box2);
        super.getChildren().add(box3);

        this.getTransforms().addAll(
                new Translate(0, -10,0),
                new Rotate(180, new Point3D(1, 0, 0)),
                new Translate(0,-10,0),
                new Rotate(90,new Point3D(1, 0, 0))
        );
        //System.out.println(this.getBoundsInParent());
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
