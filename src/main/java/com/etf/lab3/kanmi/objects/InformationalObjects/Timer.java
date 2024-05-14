package com.etf.lab3.kanmi.objects.InformationalObjects;

import com.etf.lab3.kanmi.timer.MyTimer;
import com.etf.lab3.kanmi.timer.Updatable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Timer extends SubScene implements Updatable {

    Group root;

    private Text text=new Text("00:00");
    private MyTimer myTimer;


    public Timer(double width, double height) {
        super(new Group(), width, height,true, SceneAntialiasing.BALANCED);
        this.root=(Group)super.getRoot();

        this.text.setFont(Font.font(0.1*height));
        this.text.setFill(Color.RED);

        this.text.setLayoutX(width-150);
        this.text.setLayoutY(0.1*height);


        System.out.println(this.text.getBoundsInParent());

        this.root.getChildren().add(this.text);
        myTimer=new MyTimer(this);
        myTimer.start();

    }


    private double previousTime=0;
    private double currTime=0;

    private long counter=0;
    @Override
    public boolean update(long dns) {

        //this.text.setText("milandnjndjsndkjdnksn");
        //System.out.println(dns+" "+previousTime);
        /**
        if(dns*1.0-previousTime*1.0>=1.0e9 || previousTime==0.0){
            previousTime=dns;
            currTime=currTime+1;

            this.text.setText(String.format("%02d:%02d", (int)(this.currTime/60), (int)(this.currTime%60)));
        }
        */
        counter+=dns;
        long seconds=(int)((double)counter/1.0E9D)%60;
        long minutes=(int)((double)counter/1.0E9D)/60;
        this.text.setText(String.format("%02d:%02d", minutes, seconds));

        return false;
    }


    public void endGame(){
        this.myTimer.stop();
    }
}
