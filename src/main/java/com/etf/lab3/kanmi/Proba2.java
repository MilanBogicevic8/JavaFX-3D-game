package com.etf.lab3.kanmi;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;

import java.util.HashMap;


public class Proba2 extends Application {

    private static interface Emmiter {
        public void emit ( int x, int y );
    }

    private static void circle ( int r, Emmiter emmiter ) {
        int d = 3 - 2 * r;
        int x = r;
        int y = 0;

        while ( y <= x ) {
            emmiter.emit ( x, y );

            if ( d < 0 ) {
                d += 4 * y + 6;
            } else {
                d += 4 * ( y - x ) + 10;
                x--;
            }
            y++;
        }
    }

    private static void hLine ( int xStart, int xEnd, int y, PixelWriter pixelWriter, Color color ) {
        for ( int x = xStart; x < xEnd; ++x ) {
            pixelWriter.setColor ( x, y, color );
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Canvas canvas = new Canvas ( 600, 600 );

        PixelWriter pixelWriter = canvas.getGraphicsContext2D ( ).getPixelWriter ( );

        final int cx = 300;
        final int cy = 300;
        final int r  = 300;


        Emmiter c=(x,y)->{
            hLine(cx,cx+x,cy+y,pixelWriter,Color.BLACK);
            hLine(  cx-x, cx, cy+y, pixelWriter, Color.BLACK);
            hLine(cx, cx+x, cy-y, pixelWriter, Color.BLACK);
            hLine(cx-x, cx, cx-y, pixelWriter, Color.BLACK);

            hLine(cy, cy+y, cx+x, pixelWriter, Color.BLACK);
            hLine(cy-y, cy, cx+x, pixelWriter, Color.BLACK);
            hLine(cy, cy+y, cx-x,pixelWriter, Color.BLACK);
            hLine(cy-y, cy, cx-x, pixelWriter, Color.BLACK);
        };

        Emmiter squared=(x,y)->{

            hLine(cx, cx+x, cy+y, pixelWriter, Color.BLACK);
            hLine(cy, cy+y, cx+x, pixelWriter, Color.BLACK);

            hLine(cx-x, cx, cy-y, pixelWriter, Color.BLACK);
            hLine(cy-y, cy, cx-x, pixelWriter, Color.BLACK);

        };

        Emmiter jin=(x,y)->{
            hLine(cx, cx+x, cy+y, pixelWriter, Color.BLACK);
            hLine(cx, cx+x, cy-y, pixelWriter, Color.BLACK);

            hLine(cy, cy+y, cx+x, pixelWriter, Color.BLACK);
           hLine(cy, cy+y, cx-x, pixelWriter, Color.BLACK);

        };

        Emmiter outerLine=(x,y)->{
            hLine(cx-x-1, cx-x, cy-y, pixelWriter, Color.BLACK);
            hLine(cx-x-1, cx-x, cy+y, pixelWriter, Color.BLACK);

            hLine(cy-y-1, cy-y, cx-x, pixelWriter, Color.BLACK);
            hLine(cy-y-1, cy-y, cx+x, pixelWriter, Color.BLACK);

        };

        Emmiter jin_inner=(x,y)->{
            hLine(cx, cx+x, cy+y-r/2, pixelWriter, Color.WHITE);
            hLine(cx, cx+x, cy-y-r/2, pixelWriter, Color.WHITE);

            hLine(cy, cy+y, cx-x-r/2, pixelWriter, Color.WHITE);
            hLine(cy, cy+y, cx+x-r/2, pixelWriter, Color.WHITE);

        };

        Emmiter jang_outer=(x,y)->{
            hLine(cx-x, cx, cy+y+r/2, pixelWriter, Color.BLACK);
            hLine(cx-x,cx, cy-y+r/2, pixelWriter, Color.BLACK);

            hLine(cy-y, cy, cx-x+r/2, pixelWriter, Color.BLACK);
            hLine(cy-y, cy, cx+x+r/2, pixelWriter, Color.BLACK);

        };

        Emmiter for_square=(x,y)->{
            hLine(cx, cx+y, cy+y, pixelWriter, Color.BLACK);
            hLine(cy,cy+x,cx+x, pixelWriter, Color.BLACK);
        };

/**
        this.circle(r,jin);
        this.circle(r,outerLine);
        this.circle(r/2,jin_inner);
        this.circle(r/2,jang_outer);
*/

        this.circle(r,for_square);

        Group root=new Group(canvas);
        Scene scene=new Scene(root,750,750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
