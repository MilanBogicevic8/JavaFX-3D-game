package com.etf.lab3.kanmi.objects;

import com.etf.lab3.kanmi.objects.enums.CoinsTypeEnum;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.util.Duration;

import java.util.Random;

public class Coin extends Group
{
    private CoinsTypeEnum type;
    public Coin(double x, double z)
    {
        double sel=new Random().nextDouble()*100;

        if(sel>=0 && sel<=50){
            this.type=CoinsTypeEnum.YELLOW;
        }else if(sel>50 && sel<70){
            this.type=CoinsTypeEnum.BLUE;
        }else{
            this.type=CoinsTypeEnum.GREEN;
        }

        Cylinder outerCylinder = new Cylinder(4, 1);
        Cylinder innerCylinder = new Cylinder(3, 1);
        switch (type){
            case YELLOW -> {

                outerCylinder.setMaterial(new PhongMaterial(Color.GOLD));
                outerCylinder.setRotate(90);


                innerCylinder.setMaterial(new PhongMaterial(Color.GOLDENROD));
                innerCylinder.setRotate(90);
                break;
            }
            case BLUE -> {

                outerCylinder.setMaterial(new PhongMaterial(Color.ROYALBLUE));
                outerCylinder.setRotate(90);


                innerCylinder.setMaterial(new PhongMaterial(Color.BLUE));
                innerCylinder.setRotate(90);
                break;
            }
            case GREEN -> {

                outerCylinder.setMaterial(new PhongMaterial(Color.GREEN));
                outerCylinder.setRotate(90);


                innerCylinder.setMaterial(new PhongMaterial(Color.GREENYELLOW));
                innerCylinder.setRotate(90);
                break;
            }
        }


        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), this);
        rotateTransition.setAxis(new Point3D(0, 1, 0));
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

        setTranslateX(x);
        setTranslateZ(z);
        getChildren().addAll(outerCylinder, innerCylinder);
    }

    public CoinsTypeEnum getType(){
        return type;
    }
}
