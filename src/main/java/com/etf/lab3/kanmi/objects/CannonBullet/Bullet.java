package com.etf.lab3.kanmi.objects.CannonBullet;

import com.etf.lab3.kanmi.Variables;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Bullet extends Sphere {
    private Translate position;
    private Point3D speed;

    private int strength = 1;
    private Color color = Color.ORANGE;

    private double bulletSpeed = Variables.BULLET_SPEED;


    public Bullet(double radius, Translate position, Point3D speed) {
        super(radius);
        this.position = position;
        this.speed = speed;
        super.getTransforms().add(this.position);
    }

    public Bullet(double radius, Translate position, Point3D speed, Color color) {
        super(radius);
        this.position = position;
        this.speed = speed;
        this.color = color;
        super.getTransforms().add(this.position);
    }

    public boolean update(long dns, double left, double right, double up, double down, double front, double back) {
        double newX = this.position.getX() - this.speed.getX() * bulletSpeed;
        double newY = this.position.getY() - this.speed.getY() * bulletSpeed;
        double newZ = this.position.getZ() - this.speed.getZ() * bulletSpeed;

        this.position.setX(newX);
        this.position.setY(newY);
        this.position.setZ(newZ);

        double radius = super.getRadius();

        boolean isXOutOfBounds = newX <= (left - radius) || newX >= (right + radius);
        boolean isYOutOfBounds = newY <= (up - radius) || newY >= (down + radius);
        boolean isZOutOfBounds = newZ <= (front - radius) || newZ >= (back + radius);

        return isXOutOfBounds || isYOutOfBounds || isZOutOfBounds;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setColor(Color c) {
        this.color = c;
        super.setMaterial(new PhongMaterial(c));
    }

    public void setBulletSpeed(double bs) {
        this.bulletSpeed = bs;
    }

    public Point3D getSpeed() {
        return speed;
    }

    public void setSpeed(Point3D speed) {
        this.speed = speed;
    }
}
