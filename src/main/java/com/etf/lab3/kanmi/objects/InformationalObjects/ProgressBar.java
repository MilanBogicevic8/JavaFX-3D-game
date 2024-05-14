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

public class ProgressBar extends Group {

    private static  ProgressBar unique=null;
    private Rectangle rectangle;

    private final double max_width;
    private ProgressBar(double x,double y,double width,double height){

        this.max_width=width;

        LinearGradient lg=new LinearGradient(
                0,0.5,
                1,0.5,
                true,
                CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("0xe6b400")),
                        new Stop(1,Color.web("0xe8e337"))
                }
        );

        this.rectangle=new Rectangle(width,height, lg);
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
        //border.getTransforms().add(new Translate(x,y));
        this.getChildren().add(border);

        this.getTransforms().add(new Translate(x,y));
    }

    public static ProgressBar getUnique(){
        if(ProgressBar.unique==null){
            ProgressBar.unique=new ProgressBar(
                    Variables.PRGRESS_BEGIN_X,
                    Variables.PROGRESS_BEGIN_Y,
                    Variables.PROGRESS_WIDTH,
                    Variables.PROGRESS_HEIGHT
            );
        }
        return ProgressBar.unique;
    }

    public double get_live(){
        return this.rectangle.getWidth()*100.0/this.max_width;
    }

    public void set_live(double value){
        this.rectangle.setWidth(this.max_width*value/100.);
    }
}
