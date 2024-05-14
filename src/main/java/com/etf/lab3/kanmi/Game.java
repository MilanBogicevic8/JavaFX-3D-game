package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.*;
import com.etf.lab3.kanmi.objects.CannonBullet.Bullet;
import com.etf.lab3.kanmi.objects.CannonBullet.BulletHandler;
import com.etf.lab3.kanmi.objects.InformationalObjects.*;
import com.etf.lab3.kanmi.objects.Interfaces.Abecede;
import com.etf.lab3.kanmi.timer.MyTimer;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;

public class Game extends Application
{
    private volatile boolean end_game=false;
    private boolean frozen_game=false;
    private boolean immunity_active=false;
    private double frozen_time=0.;
    private double immunity_time=0;
    private static final double WINDOW_WIDTH = 800.0;
    private static final double WINDOW_HEIGHT = 600.0;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.CADETBLUE;

    private static final double WINDOW_SIZE=600.0;

    private Group objects;
    private Scene scene;
    private Stage stage;
    private Player player;
    private World world;

    private RedObstacle red_obstacle;
    private final UpdateTimer timer = new UpdateTimer();

    //Timer
    private Timer timer_subscene=new Timer(WINDOW_WIDTH, WINDOW_HEIGHT);
    private CounterOfCoins cnt=new CounterOfCoins(WINDOW_WIDTH,WINDOW_HEIGHT);

    private ProgressBar progress_bar=ProgressBar.getUnique();
    private HealthIndicator healthIndicator=HealthIndicator.getUnique();

    private MyTimer thunder_timer=null;

    private MyTimer snowflake_timer=null;
    private MyTimer health_timer=null;
    private MyTimer imunity_timer=null;
    private MyTimer joker_timer=null;
    private List<GrayBarbObstacle> list_of_gray_obstacles=new ArrayList<>();


    private LeftTimeIndicator lti=new LeftTimeIndicator(WINDOW_WIDTH, WINDOW_HEIGHT);


    private EndGameMessage egm=null;
    private class UpdateTimer extends AnimationTimer
    {
        @Override
        public void handle(long now)
        {
            if(Game.this.get_end_game_indicator()==false){
                Game.this.updatePlayer(now);

                if(!frozen_game){
                    Game.this.updateGrayObstacles(now);
                    Game.this.updateRedPlayer(now);
                }

                setup_coin(now);
            }else{
                if(Game.this.egm==null){
                    Game.this.egm=new EndGameMessage("KRAJ ("+Game.this.cnt.getNumOfCoins()+" poena).");
                    Game.this.stop_program();
                    Game.this.mainRoot.getChildren().add(Game.this.egm);
                }
            }

        }
    }

    private long prevousGratObstacleUpdate=0;
    private void updateGrayObstacles(long now) {
        //CHANGING OF GRAY OBSTACLE DIRRECTION

        if(now-prevousGratObstacleUpdate>20.e8){
            prevousGratObstacleUpdate=now;
            for(GrayBarbObstacle gbo:list_of_gray_obstacles){
                gbo.setRotate((new Random()).nextDouble()*360);
            }
        }

        for(GrayBarbObstacle gbo:list_of_gray_obstacles){
            double oldX=gbo.getTranslateX();
            double oldZ=gbo.getTranslateZ();

            gbo.setTranslateX(gbo.getTranslateX()+0.3D*Math.cos(Math.toRadians(gbo.getRotate()-90.)));
            gbo.setTranslateZ(gbo.getTranslateZ()+0.3*Math.sin(Math.toRadians(gbo.getRotate()-90.D)));


            //bounds of the world
            if(
                    gbo.getTranslateZ()+10.>this.world.getBoundsInLocal().getMaxZ()
                    ||
                    gbo.getTranslateZ()-10<=this.world.getBoundsInLocal().getMinZ()
            ){
                gbo.setTranslateZ(oldZ);
            }

            if(
                    gbo.getTranslateX()+10.>this.world.getBoundsInLocal().getMaxX()
                     ||
                    gbo.getTranslateX()-10<=this.world.getBoundsInLocal().getMinX()
            ){
                gbo.setTranslateX(oldX);
            }

            //gray_obstacle coud not interscts with other obstacles

            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);

                if(!node.equals(gbo) && node.getBoundsInParent().intersects(gbo.getBoundsInParent())){
                    if((node instanceof Coin) || node instanceof Energy || (node instanceof Snowflake)){
                        --i;//decrement because of overflow
                        this.objects.getChildren().remove(node);
                    }else{
                        gbo.setTranslateX(oldX);
                        gbo.setTranslateZ(oldZ);
                    }
                }
            }



        }
    }

    private void updateRedPlayer(long now) {
        double oldX = this.red_obstacle.getTranslateX();
        double oldZ = this.red_obstacle.getTranslateZ();
        double playerX = this.player.getTranslateX();
        double playerZ = this.player.getTranslateZ();

        double distanceX = playerX - oldX;
        double distanceZ = playerZ - oldZ;
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceZ, 2));
        //System.out.println(distance);

        if(distance==0) return;

        double rotation;

        if (distanceZ < 0) {
            rotation = Math.toDegrees(Math.asin(-distanceX / distance));
        } else {
            rotation = 90.0 + Math.toDegrees(Math.acos(-distanceX / distance));
        }
        //System.out.println(rotation);
        this.red_obstacle.setRotate(rotation);

        double redPlayerSpeed = 0.4 * Player.PLAYER_SPEED; // 40% brzine igrača

        double newX = oldX + redPlayerSpeed * Math.cos(Math.toRadians(rotation + 90.0));
        double newZ = oldZ - redPlayerSpeed * Math.sin(Math.toRadians(rotation + 90.0));

        red_obstacle.setTranslateX(newX);
        red_obstacle.setTranslateZ(newZ);
        //System.out.println(newX+" "+oldX+" "+newZ+" "+oldZ);

        for (int i = 0; i < this.objects.getChildren().size(); ++i) {
            Node node = this.objects.getChildren().get(i);

            if (!node.equals(red_obstacle) && node.getBoundsInParent().intersects(this.red_obstacle.getBoundsInParent())) {
                if((node instanceof Coin) || node instanceof Energy || (node instanceof Snowflake)){
                    --i;//decrement because of overflow
                    this.objects.getChildren().remove(node);
                }else{
                    this.red_obstacle.setTranslateX(oldX);
                    this.red_obstacle.setTranslateZ(oldZ);
                }
            }
        }

    }

    Group mainRoot=null;
    private void setupScene()
    {
        mainRoot=new Group();
        Group root = new Group();
        objects = new Group();
        scene = new Scene(mainRoot,
                WINDOW_WIDTH,
                WINDOW_HEIGHT,
                true,
                SceneAntialiasing.DISABLED);

        scene.setFill(DEFAULT_BACKGROUND_COLOR);
        scene.setCursor(Cursor.NONE);

        player = new Player();
        //scene.setCamera(player.getCamera());
        scene.setOnMouseMoved(player);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);


        SubScene scene3d = new SubScene(root, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.DISABLED);
        scene3d.setFill(DEFAULT_BACKGROUND_COLOR);
        scene3d.setCamera(this.player.getCamera());

        world = new World();

        AmbientLight ambientLight = new AmbientLight(Color.DARKGRAY);
        ambientLight.setOpacity(0.2);
        ambientLight.setBlendMode(BlendMode.SOFT_LIGHT);
        PointLight pointLight = new PointLight(Color.WHITESMOKE);
        pointLight.setTranslateY(-100);

        Coin coin1 = new Coin(200, 200);
        Coin coin2 = new Coin(200, -200);
        Coin coin3 = new Coin(-200, 200);
        Coin coin4 = new Coin(-200, -200);
        objects.getChildren().addAll(coin1, coin2, coin3, coin4);
        root.getChildren().addAll(world, player, ambientLight, pointLight, objects);

        mainRoot.getChildren().add(scene3d);
        mainRoot.getChildren().addAll(cnt);
        mainRoot.getChildren().add(timer_subscene);
        mainRoot.getChildren().add(progress_bar);
        mainRoot.getChildren().add(healthIndicator);
        mainRoot.getChildren().add(lti);

        //info_about_left_time.getTransforms().add(new Translate(Variables.TIME_LEFT_X, Variables.TIME_LEFT_Y));
        //root.getChildren().add(new Energy(10, 0));

        //System.out.println(cnt.getBoundsInParent());
        //System.out.println("player"+player.getBoundsInParent());

        this.generate_gray_obstacles();
        this.initialize_snowflake_generator();

       // root.getChildren().add(new GrayBarbObstacle(0, 0));
        root.getChildren().add(red_obstacle=new RedObstacle(300,300));//only one
        this.red_obstacle.setRotationAxis(new Point3D(0, 1, 0));
        this.objects.getChildren().add(red_obstacle);

        //root.getChildren().add(new Snowflake(0, 0));

        //root.getChildren().add(new LObstacle(0, 0));

        //root.getChildren().add(new Health(0, 0));
        //root.getChildren().add(new Imunity(0, 0));

        //root.getChildren().add(new Joker(0, 0));

        //root.getChildren().add(new Bullet(10, new Translate(), new Point3D(1, 0, 0)));

        //BulletHandler bh=new BulletHandler();
        //bh.handleBulletEvent(player,this.objects);
        //bh.createBullet(this.objects, player, healthIndicator, this);

        BulletHandler.create_bullets(this.objects, player, healthIndicator, this,false);
        BulletHandler.create_bullets(this.objects, player, healthIndicator, this,true);
        /**
         * 4.  Na ekranu, u gornjem desnom uglu, se prikazuje dužina trajanja igre u formatu mm:ss,
         * dok se u gornjem levom uglu prikazuje broj osvojenih poena. Žuti novčić daje jedan poen.
         */



        Text t=new Text("MIKI");
        root.getChildren().add(t);
        //genrate square obstacl

        this.setup_abecede_obstacles();
        setup_obstacles();
        initialize_thunder();
        initialize_health();
        initialize_imunity();
        initialize_joker();

    }

    private void generate_gray_obstacles() {
        int n=0;
        while(n<3){
            while(true){
                double x=(new Random()).nextDouble()*WINDOW_SIZE-300+20;
                double z=(new Random()).nextDouble()*WINDOW_SIZE-300+20;

                GrayBarbObstacle gbo=new GrayBarbObstacle(x,z);
                gbo.setRotationAxis(new Point3D(0,1,0));

                this.objects.getChildren().add(gbo);

                boolean overlaping=false;
                for(int i=0;i<this.objects.getChildren().size();i++){
                    Node node=this.objects.getChildren().get(i);
                    if(!node.equals(gbo) && node.getBoundsInParent().intersects(gbo.getBoundsInParent()) ){
                        overlaping=true;
                        this.objects.getChildren().remove(gbo);
                        break;
                    }
                }
                if(overlaping==false){
                    list_of_gray_obstacles.add(gbo);
                    break;
                }
            }
            n+=1;
        }

    }

    /**
     * 2. Učiniti da se novčići stvaraju svakih pet sekundi na nasumičnoj lokaciji.
     */

    private double previous_call=0;
    private void setup_coin(double now){
        if(now-previous_call<5*pow(10,9)) return;
        previous_call=now;

        while(true){

            double x=(new Random()).nextDouble()*WINDOW_SIZE-300;
            double z=(new Random()).nextDouble()*WINDOW_SIZE-300;

            Coin coin=new Coin(x,z);
            boolean contain=false;
            this.objects.getChildren().add(coin);

            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);
                if(!coin.equals(node) && node.getBoundsInParent().intersects(coin.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(coin);
                    break;
                }
            }
            if(contain==false){
                break;
            }
        }
    }

    private void setup_obstacles(){

        for(int i=0;i<10;i++){

            while(true){

                double x=new Random().nextDouble()*(WINDOW_SIZE- SquareObstacle.SIZE)-300.0;
                double z=new Random().nextDouble()*(WINDOW_SIZE-SquareObstacle.SIZE)- 300.0;

                SquareObstacle so=new SquareObstacle(x,z);

                this.objects.getChildren().add(so);

                boolean contain=false;
                for(int j=0;j<this.objects.getChildren().size();j++){
                    Node node=this.objects.getChildren().get(j);

                    if(!so.equals(node) && so.getBoundsInParent().intersects(node.getBoundsInParent())){
                        contain=true;
                        this.objects.getChildren().remove(so);
                        break;
                    }
                }

                if(contain==false){
                    break;
                }
            }
        }
    }

    private long previousThunder=0;

    private void setup_thunder(long dns){

        //System.out.println(previousThunder/1.0E9D+" "+dns/1.0E9D+" "+(dns - previousThunder)/1.0E9D);
        previousThunder+=dns;
        if (previousThunder < 12e9D) return;

        previousThunder=0;

        System.out.println("uso");
        while(true){
            double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
            double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

            Energy energy=new Energy(x,z);

            this.objects.getChildren().add(energy);

            boolean contain=false;
            for(int j=0;j<this.objects.getChildren().size();j++){
                Node node=this.objects.getChildren().get(j);

                if(!energy.equals(node) && energy.getBoundsInParent().intersects(node.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(energy);
                    break;
                }
            }

            if(contain==false){
                System.out.println("thunder");
                break;
            }

        }
    }

    private void initialize_thunder(){
        thunder_timer=new MyTimer((dns)->{
            setup_thunder(dns);
            return false;
        });
        thunder_timer.start();
    }

    private long previous_snowflake_time=0;
    private void setup_snowflake(long dns){

        previous_snowflake_time+=dns;

        if(previous_snowflake_time<5.e9) return;
        previous_snowflake_time=0;

        double come_back=(new Random()).nextDouble();
        System.out.println(come_back);
        if(come_back<0.7){
            return;
        }

        while(true){

            double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
            double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

            Snowflake snowflake=new Snowflake(x, z);
            this.objects.getChildren().add(snowflake);

            boolean contain=false;
            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);

                if(!node.equals(snowflake) && node.getBoundsInParent().intersects(snowflake.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(snowflake);
                    break;
                }
            }
            if(contain==false){
                //System.out.println("SNOW");
                break;
            }
        }

    }

    private void initialize_snowflake_generator(){
        this.snowflake_timer=new MyTimer((dns)->{
            this.setup_snowflake(dns);
            return false;
        });
        this.snowflake_timer.start();
    }

    private double previous_time_health=0;
    private void setup_health(long dns){

        previous_time_health+=dns;

        if(previous_time_health<10e9) return;

        previous_time_health=0;

        double come_back=(new Random()).nextDouble();
        //System.out.println(come_back);
        //System.out.println("hEALTH:"+come_back);
        if(come_back<0.8){
            return;
        }

        while(true){

            double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
            double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

            Health health=new Health(x, z);

            this.objects.getChildren().add(health);


            boolean contain=false;
            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);

                if(!node.equals(health) && node.getBoundsInParent().intersects(health.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(health);
                    break;
                }
            }
            if(contain==false){
                break;
            }
        }
    }

    private void initialize_health(){
        this.health_timer=new MyTimer((dns)->{
            this.setup_health(dns);
            return false;
        });
        this.health_timer.start();
    }

    private double previous_imunity_time=0;
    private void setup_imunity(long dns){

        previous_imunity_time+=dns;
        if(previous_imunity_time<10e9) return;
        previous_imunity_time=0;

        double come_back=(new Random()).nextDouble();
        if(come_back<0.9){
            return;
        }

        while(true){
            double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
            double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

            Imunity imunity=new Imunity(x, z);

            this.objects.getChildren().add(imunity);

            boolean contain=false;
            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);

                if(!node.equals(imunity) && node.getBoundsInParent().intersects(imunity.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(imunity);
                    break;
                }
            }
            if(contain==false){
                break;
            }
        }
    }

    private void initialize_imunity(){
        this.imunity_timer=new MyTimer((dns)->{
            this.setup_imunity(dns);
            return false;
        });
        this.imunity_timer.start();
    }

    private long joker_time_elapsed=0;
    private void setup_joker(long dns){
        joker_time_elapsed+=dns;
        if(joker_time_elapsed<5.e9) return;
        joker_time_elapsed=0;

        double come_back=(new Random()).nextDouble();
        if(come_back<0.1){
            return;
        }

        System.gc();
        while(true){

            double x=new Random().nextDouble()*(WINDOW_SIZE-Energy.BOX_WIDTH)-300.0;
            double z=new Random().nextDouble()*(WINDOW_SIZE-Energy.BOX_WIDTH)- 300.0;

            System.out.println("uso0)0"+" "+x+" "+z);

            Joker joker=new Joker(x,z);

            this.objects.getChildren().add(joker);

            boolean contain=false;
            for(int i=0;i<this.objects.getChildren().size();i++){
                Node node=this.objects.getChildren().get(i);

                if(!node.equals(joker) && node.getBoundsInParent().intersects(joker.getBoundsInParent())){
                    contain=true;
                    this.objects.getChildren().remove(joker);
                    break;
                }
            }
            if(contain==false){
                break;
            }
        }
    }

    private void initialize_joker(){
        this.joker_timer=new MyTimer(
                (dns)->{
                    this.setup_joker(dns);
                    return false;
                }
        );
        this.joker_timer.start();
    }


    private void setup_abecede_obstacles(){

        int n=0;

        while(n<10){

            while(true){
                double choice=(new Random()).nextDouble();


                if(choice<0.5) {
                    double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
                    double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

                    LObstacle obs=new LObstacle(x,z);
                    this.objects.getChildren().add(obs);

                    boolean contain=false;

                    for(int i=0;i<this.objects.getChildren().size();i++){
                        Node node=this.objects.getChildren().get(i);

                        if(!node.equals(obs) && node.getBoundsInParent().intersects(obs.getBoundsInParent())){
                            contain=true;
                            this.objects.getChildren().remove(obs);
                            break;
                        }
                    }
                    if(!contain){
                        break;
                    }
                }else{
                    double x=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)-300.0;
                    double z=new Random().nextDouble()*(WINDOW_SIZE- Energy.BOX_WIDTH)- 300.0;

                    PObstacle obs=new PObstacle(x,z);
                    this.objects.getChildren().add(obs);

                    boolean contain=false;

                    for(int i=0;i<this.objects.getChildren().size();i++){
                        Node node=this.objects.getChildren().get(i);

                        if(!node.equals(obs) && node.getBoundsInParent().intersects(obs.getBoundsInParent())){
                            contain=true;
                            this.objects.getChildren().remove(obs);
                            break;
                        }
                    }
                    if(!contain){
                        break;
                    }
                }
            }
            n+=1;
        }

        int c=0;
        int c2=0;
        for (int i=0;i<this.objects.getChildren().size();i++){
            c+=1;
            if(this.objects.getChildren().get(i) instanceof Abecede) c2+=1;
        }
        System.out.println(c+" "+c2);
    }


    private void showStage()
    {
        stage.setTitle("Kanmi");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        timer.start();
    }

    /**
     * 5.
     *
     * Pri vrhu ekrana prikazati indikator energije u obliku žute trake. Energija se troši dok se
     * igrač kreće. Ona se automatski dopunjuje do maksimalne. Energija utiče na brzinu kretanja
     * igrača: pri maksimalnoj energiji se igrač kreće maksimalnom brzinom, dok ako izgubi svu
     * energiju igrač ne može da se kreće.
     *
     */
    private void updatePlayer(long now)
    {
        double oldX = player.getTranslateX();
        double oldZ = player.getTranslateZ();

        boolean stayed=true;

        Player.PLAYER_SPEED=this.progress_bar.get_live()/100.*0.8;

        if (player.isUpPressed())
        {
            stayed=false;
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * Math.cos(Math.toRadians(player.getRotate() - 90)));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * Math.sin(Math.toRadians(player.getRotate() - 90)));
        }
        if (player.isDownPressed())
        {
            stayed=false;
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * Math.cos(Math.toRadians(player.getRotate() + 90)));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * Math.sin(Math.toRadians(player.getRotate() + 90)));
        }
        if (player.isLeftPressed())
        {
            stayed=false;
            player.setTranslateX(player.getTranslateX() - Player.PLAYER_SPEED * Math.cos(Math.toRadians(player.getRotate())));
            player.setTranslateZ(player.getTranslateZ() + Player.PLAYER_SPEED * Math.sin(Math.toRadians(player.getRotate())));
        }
        if (player.isRightPressed())
        {
            stayed=false;
            player.setTranslateX(player.getTranslateX() + Player.PLAYER_SPEED * Math.cos(Math.toRadians(player.getRotate())));
            player.setTranslateZ(player.getTranslateZ() - Player.PLAYER_SPEED * Math.sin(Math.toRadians(player.getRotate())));
        }

        if(stayed){
            //System.out.println(Utilities.clamp(this.progress_bar.get_live()+0.01, 0., 100.));
            this.progress_bar.set_live(Utilities.clamp(this.progress_bar.get_live()+0.01, 0., 100.));
        }else{
            //System.out.println(Utilities.clamp(this.progress_bar.get_live()-0.1, 0., 100.));
            this.progress_bar.set_live(Utilities.clamp(this.progress_bar.get_live()-0.02, 0.,100.));


        }


        if (player.getTranslateX() + Player.PLAYER_RADIUS / 2 >= world.getBoundsInLocal().getMaxX() ||
            player.getTranslateX() - Player.PLAYER_RADIUS / 2 <= world.getBoundsInLocal().getMinX())
            player.setTranslateX(oldX);
        if (player.getTranslateZ() + Player.PLAYER_RADIUS / 2 >= world.getBoundsInLocal().getMaxZ() ||
            player.getTranslateZ() - Player.PLAYER_RADIUS / 2 <= world.getBoundsInLocal().getMinZ())
            player.setTranslateZ(oldZ);

        for (int i = 0; i < objects.getChildren().size(); ++i)
        {
            Node node = objects.getChildren().get(i);

            if (node instanceof Coin)
            {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent()))
                {
                    objects.getChildren().remove(node);
                    --i;
                    cnt.incNumOfCoins(((Coin)node).getType().getPoints());
                }
            }else if(node instanceof SquareObstacle) {
                if (node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    this.player.setTranslateX(oldX);
                    this.player.setTranslateZ(oldZ);
                }
            } else if(node instanceof Energy){
                if(node.getBoundsInParent().intersects(player.getBoundsInParent())){
                    --i;
                    this.objects.getChildren().remove(node);
                    this.progress_bar.set_live(Utilities.clamp(this.progress_bar.get_live()+33,0,100));
                }
            }else if(node instanceof Snowflake){
                if(node.getBoundsInParent().intersects(player.getBoundsInParent())){
                    --i;
                    this.objects.getChildren().remove(node);
                    frozen_game=true;

                    MyTimer timer=new MyTimer((dns)->{
                        frozen_time+=dns;
                        lti.set_snowflake_left_time(10-(int)(frozen_time/1.0e9));//set left time
                        if(frozen_time<10.e9) return false;
                        frozen_time=0;
                        frozen_game=false;
                        lti.empty_string();//empty left time label
                        return true;
                    });
                    timer.start();
                }
            }else if(node instanceof Abecede){

                //System.out.println(node.getBoundsInParent()+" "+player.getBoundsInParent());

                // Pomakni igrača prema gore za 5
                double originalPlayerY = player.getTranslateY();
                player.setTranslateY(originalPlayerY + 5);


                if(((Abecede) node).intersects(player) && !player.isSpacePressed()) {
                    this.player.setTranslateX(oldX);
                    this.player.setTranslateZ(oldZ);
                    // Vrati igrača na njegovu originalnu poziciju
                    player.setTranslateY(originalPlayerY);
                }else if(((Abecede)node).intersects(player) && player.isSpacePressed()){
                    // Vrati igrača na njegovu originalnu poziciju
                    player.setTranslateY(originalPlayerY);

                    Timeline timeline=new Timeline(
                            new KeyFrame[]{

                                    new KeyFrame(Duration.ONE,
                                            new KeyValue(player.translateYProperty(), player.getTranslateY()-5)
                                            ),
                                    new KeyFrame(Duration.seconds(1.5),
                                            new KeyValue(player.translateXProperty(),
                                                    player.getTranslateX()+15
                                            )
                                    ),
                                    new KeyFrame(Duration.seconds(2.),
                                            new KeyValue(player.translateYProperty(),originalPlayerY)
                                            )
                            }
                    );

                    timeline.play();
                }else{
                    // Vrati igrača na njegovu originalnu poziciju
                    player.setTranslateY(originalPlayerY);
                }
            }else if((node instanceof RedObstacle) || (node instanceof GrayBarbObstacle)) {
                if (this.player.getBoundsInParent().intersects(node.getBoundsInParent())) {
                    if (this.healthIndicator.get_live() - 0.02 == 0) {
                        this.end_game = true;
                    }
                    if(!immunity_active)
                        this.healthIndicator.set_live(Utilities.clamp(this.healthIndicator.get_live() - 0.02, 0, 100));
                }
            }else if(node instanceof Health){
                if(this.player.getBoundsInParent().intersects(node.getBoundsInParent())){
                    --i;
                    this.objects.getChildren().remove(node);
                    this.healthIndicator.set_live(Utilities.clamp(this.healthIndicator.get_live()+25,0,100));

                }
            }else if(node instanceof Imunity){
                if(node.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    --i;
                    this.objects.getChildren().remove(node);
                    immunity_active=true;

                    MyTimer timer=new MyTimer((dns)->{
                        immunity_time+=dns;
                        lti.set_immunity_left_time(10-(int)(immunity_time/1.e9));
                        if(immunity_time<10.e9) return false;
                        immunity_time=0;
                        immunity_active=false;
                        lti.empty_string();
                        return true;
                    });
                    timer.start();
                }

            }else if(node instanceof Joker){
                if(node.getBoundsInParent().intersects(player.getBoundsInParent())){
                    --i;
                    this.objects.getChildren().remove(node);

                    double choice=(new Random()).nextDouble()*100;

                    if(choice<40){
                        //(verovatnoća 40%) Igrač dobija nasumičan broj poena od 1 do 10 sa jednakim
                        //verovatnoćama 10%.
                        double points=(new Random()).nextDouble()*100;
                        this.cnt.incNumOfCoins((int)(points/10));
                    }else if(choice<60){
                        //(verovatnoća 20%) Oduzima se 20% energije, a dodaje 20% zdravlja.
                        this.progress_bar.set_live(Utilities.clamp(this.progress_bar.get_live()-20, 0, 100));
                        this.healthIndicator.set_live(Utilities.clamp(this.healthIndicator.get_live()+20, 0, 100));
                        if(this.healthIndicator.get_live()==0){//kraj ako je healthIndicator 0
                            this.end_game=true;
                        }
                    }else if(choice<80){
                        //(verovatnoća 20%) Oduzima se 20% zdravlja, a dodaje 20% energije
                        this.progress_bar.set_live(Utilities.clamp(this.progress_bar.get_live()+20, 0, 100));
                        this.healthIndicator.set_live(Utilities.clamp(this.healthIndicator.get_live()-20, 0, 100));
                        if(this.healthIndicator.get_live()==0){
                            this.end_game=true;
                        }
                    }else if(choice<90){
                        //zamrzavanje
                        frozen_game=true;

                        MyTimer timer=new MyTimer((dns)->{
                            frozen_time+=dns;
                            lti.set_snowflake_left_time(10-(int)(frozen_time/1.0e9));//set left time
                            if(frozen_time<10.e9) return false;
                            frozen_time=0;
                            frozen_game=false;
                            lti.empty_string();//empty left time label
                            return true;
                        });
                        timer.start();
                    }else{
                        //(verovatnoća 10%) Bonus imuniteta

                        immunity_active=true;

                        MyTimer timer=new MyTimer((dns)->{
                            immunity_time+=dns;
                            lti.set_immunity_left_time(10-(int)(immunity_time/1.e9));
                            if(immunity_time<10.e9) return false;
                            immunity_time=0;
                            immunity_active=false;
                            lti.empty_string();
                            return true;
                        });
                        timer.start();
                    }
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        stage = primaryStage;

        setupScene();
        showStage();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private void stop_program(){
        cnt.end();
        thunder_timer.stop();
        snowflake_timer.stop();
        health_timer.stop();
        imunity_timer.stop();
        this.joker_timer.stop();
        //BulletHandler.timer.stop();
        timer_subscene.endGame();
        BulletHandler.end_bullets_firing();
    }


    public void set_end_game_indicator(){
        this.end_game=true;
    }

    public boolean get_end_game_indicator(){
        return this.end_game;
    }


}
