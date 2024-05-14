package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.CannonBullet.Bullet;
import com.etf.lab3.kanmi.objects.InformationalObjects.HealthIndicator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Sphere;

public class Player extends Group implements EventHandler<Event>
{
    public static final double NEAR_CLIP = 0.1;
    public static final double FAR_CLIP = 10_000;
    public static final double FIELD_OF_VIEW = 60;
    public static  double PLAYER_SPEED = 0.8;
    public static final double PLAYER_RADIUS = 20;

    private final PerspectiveCamera camera;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed=false;
    private boolean isGameActive = true;


    public Player()
    {
        Sphere shape = new Sphere(PLAYER_RADIUS);
        shape.setVisible(false);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(NEAR_CLIP);
        camera.setFarClip(FAR_CLIP);
        camera.setFieldOfView(FIELD_OF_VIEW);

        setRotationAxis(new Point3D(0, 1, 0));
        this.getChildren().addAll(shape, camera);
    }

    @Override
    public void handle(Event event)
    {
        if (event instanceof KeyEvent keyEvent)
        {
            if (keyEvent.getCode() == KeyCode.ESCAPE && keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                System.exit(0);
            else
            {
                if (!isGameActive)
                {
                    upPressed = false;
                    downPressed = false;
                    rightPressed = false;
                    leftPressed = false;
                    return;
                }

                if (keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        upPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        upPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        downPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        downPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        leftPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        leftPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        rightPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        rightPressed = false;
                }else if(keyEvent.getCode() == KeyCode.SPACE){

                    if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        spacePressed=true;
                    else if(keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        spacePressed=false;
                }
            }
        }
        else if (event instanceof MouseEvent mouseEvent)
        {
            if (MouseEvent.MOUSE_MOVED.equals(mouseEvent.getEventType()))
                setRotate(mouseEvent.getSceneX() * 390. / getScene().getWidth() - 195.);
        }
    }

    public Camera getCamera()
    {
        return camera;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isSpacePressed(){
        return spacePressed;
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean gameActive) {
        isGameActive = gameActive;
    }

    public boolean handleCollision(Bullet bullet, HealthIndicator hi){
        Bounds bounds = this.getBoundsInParent ( );

        boolean collided=bounds.intersects ( bullet.getBoundsInParent ( ) );
        //if(collided){
            //hi.set_live(Utilities.clamp(hi.get_live()-0.02, 0, 100.));
        //}
        return collided;
    }
}
