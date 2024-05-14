package com.etf.lab3.kanmi.objects.InformationalObjects;

import com.etf.lab3.kanmi.Variables;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.temporal.Temporal;

public class EndGameMessage extends Group {

    private Text text;
    public EndGameMessage(String message){

        this.text=new Text(message);

        this.text.setFill(Color.DARKRED);
        this.text.setFont(Font.font(100));

        this.setTranslateX(Variables.WINDOW_WIDTH/10.);
        this.setTranslateY(Variables.WINDOW_HEIGHT/2.);

        super.getChildren().add(this.text);
    }
}
