package com.etf.lab3.kanmi.objects.InformationalObjects;

import com.etf.lab3.kanmi.Variables;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Translate;

public class HealthIndicator extends Group {

    private static HealthIndicator healthIndicator=null;
    private Rectangle rectangle=null;

    private final double max_width;

    private HealthIndicator(double x,double y,double width,double height){
        this.max_width=width;

        LinearGradient lg=new LinearGradient(
                0,0.5,
                1,0.5,
                true,
                CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("0x580E0E")),
                        new Stop(1,Color.web("0xE34F4F"))
                }
        );


        this.rectangle=new Rectangle(width,height,lg);
        this.rectangle.setStrokeWidth(2.0);
        this.rectangle.setStroke(Color.BLACK);
        this.rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
        this.rectangle.setArcHeight(height/4.0);
        this.rectangle.setArcWidth(height/4.0);

        this.getChildren().addAll(this.rectangle);

        Rectangle border=new Rectangle(width,height,Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setArcWidth(height/4.);
        border.setArcHeight(height/4.);
        border.setStrokeWidth(2.);

        this.getChildren().add(border);

        this.getTransforms().add(new Translate(x,y));

    }

    public static HealthIndicator getUnique(){
        if(HealthIndicator.healthIndicator==null){
            HealthIndicator.healthIndicator=new HealthIndicator(
                    Variables.HEALTH_BEGIN_X,
                    Variables.HEALTH_BEGIN_Y,
                    Variables.HEALTH_WIDTH,
                    Variables.HEALTH_HEIGHT
            );
        }
        return HealthIndicator.healthIndicator;
    }

    public double get_live(){
        return this.rectangle.getWidth()*100.0/this.max_width;
    }

    public void set_live(double value){
        this.rectangle.setWidth(this.max_width*value/100.);
    }
}

