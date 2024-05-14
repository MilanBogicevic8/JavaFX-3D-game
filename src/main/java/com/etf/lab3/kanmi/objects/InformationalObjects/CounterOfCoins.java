package com.etf.lab3.kanmi.objects.InformationalObjects;

import com.etf.lab3.kanmi.timer.MyTimer;
import com.etf.lab3.kanmi.timer.Updatable;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CounterOfCoins extends SubScene implements Updatable {



    private Group root;
    private MyTimer myTimer;

    private Text text=new Text("Poeni: 0");
    private int numOfCoins=0;


    public CounterOfCoins(double width, double height) {
        super(new Group(), width, height,true, SceneAntialiasing.BALANCED);


        this.root=(Group)super.getRoot();


        this.text.setFill(Color.DARKBLUE);
        this.text.setFont(Font.font(0.1*height));

        this.text.setLayoutX(50);
        this.text.setLayoutY(0.1*height);

        this.root.getChildren().add(this.text);

        myTimer=new MyTimer(this);
        myTimer.start();

    }

    private long prevDns=0;
    @Override
    public boolean update(long dns) {

        //System.out.println("uso"+" "+dns+" "+prevDns);
        if(prevDns==0 || prevDns/1.0E9D>=1){
            //System.out.println("usoo2");
            prevDns=0;
            this.text.setText(String.format("Poeni: %d", numOfCoins));
        }
        return false;
    }

    public void incNumOfCoins(int num){

        this.numOfCoins+=num;
    }
    public int getNumOfCoins(){

        return this.numOfCoins;
    }


    public static CounterOfCoins getUnique(){
       return null;
    }

    public static void setUnique(){
        return;
    }

    public void end(){
        this.myTimer.stop();
    }
}
