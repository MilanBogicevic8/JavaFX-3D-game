package com.etf.lab3.kanmi.objects;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Snowflake extends Group {

    private static final double SNOWFLAKE_WIDTH=20;
    private static final double SNOWLAKE_HEIGHT=2;
    private static final double SNOWFLAKE_DEPTH=2;
    public Snowflake(double x,double z){
        Box b1=new Box(SNOWFLAKE_WIDTH,SNOWLAKE_HEIGHT,SNOWFLAKE_DEPTH);
        Box b2=new Box(SNOWFLAKE_WIDTH,SNOWLAKE_HEIGHT,SNOWFLAKE_DEPTH);
        Box b3=new Box(SNOWFLAKE_WIDTH,SNOWLAKE_HEIGHT,SNOWFLAKE_DEPTH);

        b1.setMaterial(new PhongMaterial(Color.AQUA));
        b2.setMaterial(new PhongMaterial(Color.AQUA));
        b3.setMaterial(new PhongMaterial(Color.AQUA));

        b2.setRotate(60);
        b3.setRotate(-60);

        super.getChildren().add(b1);
        super.getChildren().add(b2);
        super.getChildren().add(b3);

        Rotate r=new Rotate(0);
        this.getTransforms().add(r);
        r.setAxis(new Point3D(0, 1, 0));


        Timeline timeline=new Timeline(
                new KeyFrame[]{
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(r.angleProperty(),0)
                                ),
                        new KeyFrame(Duration.seconds(5),
                                new KeyValue(r.angleProperty(), 360)
                        )
                }
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        this.setTranslateX(x);
        this.setTranslateZ(z);
    }

}
