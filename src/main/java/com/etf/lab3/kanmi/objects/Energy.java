package com.etf.lab3.kanmi.objects;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import javafx.util.Duration;

public class Energy extends Group {

    public static final double BOX_WIDTH=20;
    private static final double BOX_HEIGHT=3;
    private static final double BOX_DEPTH=2;

    private static final double SCALE=0.2;

    public Energy(double x,double z){
        Box downBox=new Box(BOX_WIDTH  ,BOX_HEIGHT,BOX_DEPTH);

        downBox.setMaterial(new PhongMaterial(Color.BLUE));
        downBox.getTransforms().addAll(
                new Translate(0,-5,0),
                new Rotate(-60)
        );

        Box horizontal_box=new Box(BOX_WIDTH,BOX_HEIGHT,BOX_DEPTH);

        horizontal_box.setMaterial(new PhongMaterial(Color.BLUE));
        horizontal_box.getTransforms().addAll(
                new Translate(0,-5,0),
                new Translate(0,-10,0),
                new Translate(-2,0,0)
        );

        Box uper_box=new Box(BOX_WIDTH,BOX_HEIGHT,BOX_DEPTH);

        uper_box.setMaterial(new PhongMaterial(Color.BLUE));

        uper_box.getTransforms().addAll(
            new Translate(0,-8*BOX_HEIGHT,0),
            new Translate(-BOX_WIDTH/2+2,0,0),
            new Rotate(-60)
        );


        this.getChildren().add(downBox);
        this.getChildren().add(horizontal_box);
        this.getChildren().add(uper_box);

        Scale scale = new Scale();
        scale.setX(0.2); // Postavlanje faktora skaliranja po x osi na 0.2 (smanjenje na petinu)
        scale.setY(0.2); // Postavlanje faktora skaliranja po y osi na 0.2 (smanjenje na petinu)
        scale.setZ(0.2); // Postavlanje faktora skaliranja po z osi na 0.2 (smanjenje na petinu)


        Rotate r=new Rotate(0);
        r.setAxis(new Point3D(0., 1., 0.));

        this.getTransforms().addAll(
                scale,
                new Translate(x,0,z),
                r
        );


        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(r.angleProperty(), 0)
                        ),
                new KeyFrame(Duration.seconds(5.),
                        new KeyValue(r.angleProperty(), 360.))
                );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();



        //System.out.println(this.getBoundsInParent());



    }
}
