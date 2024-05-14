package com.etf.lab3.kanmi.objects;

import com.etf.lab3.kanmi.Player;
import com.etf.lab3.kanmi.objects.Interfaces.Abecede;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class PObstacle extends Group implements Abecede {

    public static final double HEIGHT=15.0/2.;
    public static final double WIDTH=15;
    public static final double DEPTH=5;

    private Group g;

    public PObstacle(double x, double z){
        Box box1=new Box(WIDTH,HEIGHT,DEPTH);
        Box box2=new Box(WIDTH,HEIGHT,DEPTH);
        Box box3=new Box(WIDTH,HEIGHT,DEPTH);

        box1.setMaterial(new PhongMaterial(Color.YELLOW));
        box2.setMaterial(new PhongMaterial(Color.YELLOW));
        box3.setMaterial(new PhongMaterial(Color.YELLOW));

        box2.getTransforms().addAll(
                new Rotate(90,new Point3D(0,1,0)),
                new Translate(WIDTH-WIDTH/2,0,0),
                new Translate(0, 0,WIDTH/2-DEPTH/2)
        );

        box3.getTransforms().addAll(
                new Rotate(90,new Point3D(0,1,0)),
                new Translate(WIDTH-WIDTH/2,0,0),
                new Translate(0, 0,-WIDTH/2+DEPTH/2)
        );

        super.getChildren().add(box1);
        super.getChildren().add(box2);
        super.getChildren().add(box3);

        this.setTranslateX(x);
        this.setTranslateZ(z);
        this.setTranslateY(5);

    }

    public boolean intersects(Player player){
        //this.setTranslateY(-5);
        boolean iner=this.getBoundsInParent().intersects(player.getBoundsInParent());
        //this.setTranslateY(5);
        return iner;
    }
}
