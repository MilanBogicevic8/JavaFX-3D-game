package com.etf.lab3.kanmi.objects.CannonBullet;

import com.etf.lab3.kanmi.Game;
import com.etf.lab3.kanmi.Player;
import com.etf.lab3.kanmi.Utilities;
import com.etf.lab3.kanmi.Variables;
import com.etf.lab3.kanmi.objects.GrayBarbObstacle;
import com.etf.lab3.kanmi.objects.InformationalObjects.HealthIndicator;
import com.etf.lab3.kanmi.objects.Interfaces.Abecede;
import com.etf.lab3.kanmi.objects.RedObstacle;
import com.etf.lab3.kanmi.objects.SquareObstacle;
import com.etf.lab3.kanmi.timer.MyTimer;
import com.etf.lab3.kanmi.timer.Updatable;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

public class BulletHandler {

    private Bullet bullet;
    public BulletHandler(){}

    private static List<MyTimer> list_of_timers=new ArrayList<>();

    public void handleBulletEvent(Player player,Group root,boolean first_third){
        double centerX=500.;
        double centerZ=0.;

        double offset_third=0;
        if(first_third){
            offset_third=Variables.WINDOW_WIDTH/6.;
        }else{
            offset_third=-Variables.WINDOW_WIDTH/6.;
        }

        this.bullet=null;

        Point3D speed=new Point3D(
                500,-1.,0
        ).normalize();

        Point3D offset=speed.multiply(0.01* Variables.BULLET_RADIUS);

        Translate t=new Translate(300, offset.getY(),offset.getZ()+offset_third);



        this.bullet=new Bullet(2*Variables.BULLET_RADIUS, t, speed);

        root.getChildren().add(this.bullet);

    }

    private static void createCanon(Translate t,Group root) {
        Cylinder cylinder=new Cylinder(2*Variables.BULLET_RADIUS,20);
        cylinder.getTransforms().addAll(
                new Rotate(-90,new Point3D(0, 1, 0)),
                t,
                new Rotate(90, new Point3D(1, 0, 0))

        );
        cylinder.setMaterial(new PhongMaterial(Color.BLACK));
        root.getChildren().add(cylinder);
    }

    long prevTime=0;
    public void createBullet(Group root, Player player, HealthIndicator hi, Game g,boolean first_third){

        this.handleBulletEvent(player, root,first_third);

        if(bullet!=null){

            Updatable updatable=dns->{
                boolean outOfBounds = this.bullet.update (
                        dns,
                        -Variables.WINDOW_WIDTH,
                        Variables.WINDOW_WIDTH,
                        -100,
                        100,
                        -Variables.WINDOW_HEIGHT,
                        Variables.WINDOW_HEIGHT
                );

                boolean collided = false;
                if ( outOfBounds ) {
                    root.getChildren ( ).remove ( bullet );
                }else {

                        collided = player.handleCollision ( bullet,hi );
                        //System.out.println(collided);
                        if ( collided ) {
                            hi.set_live(Utilities.clamp(hi.get_live()-15, 0, 100));

                            root.getChildren ( ).remove ( bullet );
                            if (hi.get_live()==0.) {
                                g.set_end_game_indicator();
                            }

                        }
                        BulletHandler.check_collision_with_other_objects(root,this.bullet);

                }

                return outOfBounds || collided;
            };


            MyTimer myTimer=new MyTimer(
                    (dns)->{
                        prevTime+=dns;
                        if(prevTime<0.1e9) return false;
                        prevTime=0;

                        updatable.update(dns);
                        return false;
                    }
            );

            myTimer.start();

            //BulletHandler.list_of_timers.add(myTimer);
            }
        }

    private static void check_collision_with_other_objects(Group root,Bullet bullet) {

        //System.out.println(root.getChildren().size());
        boolean is_in=false;
        for(int i=0;i<root.getChildren().size();i++){
            Node node=root.getChildren().get(i);
            //System.out.println(node.getClass());

            if(!bullet.equals(node) && node.getBoundsInParent().intersects(bullet.getBoundsInParent()) && ((node instanceof SquareObstacle)|| (node instanceof Abecede) || (node instanceof GrayBarbObstacle) || (node instanceof RedObstacle))){
                is_in=true;
                break;
            }
        }

        if(is_in){
            root.getChildren().remove(bullet);
        }
    }

    public static long tim_bullet=0;

        public static  Group root=null;
        public static  Player player=null;
        public static  HealthIndicator hi=null;
        public static  Game g=null;
        public static MyTimer timer=null;

        //private List<MyTimer> list_of_timers=new ArrayList<>();

        public static void create_bullets(Group r, Player p, HealthIndicator h, Game game,boolean first_third){

            if(first_third){
                createCanon(new Translate(Variables.WINDOW_WIDTH/6, 0,-300),r);
            }else{
                createCanon(new Translate(-Variables.WINDOW_WIDTH/6, 0,-300),r);
            }


            BulletHandler.root=r;
            BulletHandler.player=p;
            BulletHandler.hi=h;
            BulletHandler.g=game;

            BulletHandler.timer=new MyTimer((dns)->{

                BulletHandler.tim_bullet+=dns;
                //System.out.println((BulletHandler.tim_bullet/1.e9));
                if(BulletHandler.tim_bullet<1.e9) return false;
                BulletHandler.tim_bullet=0;


                BulletHandler bh=new BulletHandler();
                bh.createBullet(root, player, hi, g,first_third);

                return false;
            });
            BulletHandler.timer.start();

            if(!list_of_timers.contains(BulletHandler.timer)){
                list_of_timers.add(BulletHandler.timer);
            }
        }

        public static void end_bullets_firing(){
            for(MyTimer t:list_of_timers){
                t.stop();
            }
        }


}
