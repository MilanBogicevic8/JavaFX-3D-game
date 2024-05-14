package com.etf.lab3.kanmi.objects.InformationalObjects;

import com.etf.lab3.kanmi.Variables;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LeftTimeIndicator extends SubScene {

    Group root=null;
    Text text=new Text("");

    public LeftTimeIndicator(double width, double height) {
        super(new Group(), width, height);

        this.root=(Group)super.getRoot();

        this.text.setFont(Font.font(0.05*height));
        this.text.setFill(Color.RED);

        this.text.setLayoutX(Variables.TIME_LEFT_X);
        this.text.setLayoutY(0.1*height+Variables.TIME_LEFT_Y);

        this.root.getChildren().add(this.text);

    }

    public void set_snowflake_left_time(int time){
        this.text.setText("Preostalo vreme:"+time);
        this.text.setFill(Color.LIGHTBLUE);
    }

    public void set_immunity_left_time(int time){
        this.text.setText("Preostalo vreme:"+time);
        this.text.setFill(Color.PURPLE);
    }

    public void empty_string(){
        this.text.setText("");
    }
}
