package com.etf.lab3.kanmi.objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class SquareObstacle extends Box {

    public static final double SIZE=15.0;

    public SquareObstacle(double x, double z){
        super(SIZE,SIZE,SIZE);
        PhongMaterial phong=new PhongMaterial(Color.SADDLEBROWN);
        this.setMaterial(phong);

        this.setTranslateX(x);
        this.setTranslateY(10-SIZE/2.0);//pod je spusten za 10, a ako i ovo spustimo za 10 onda se vidi samo gornja polovina
        this.setTranslateZ(z);
    }
}
