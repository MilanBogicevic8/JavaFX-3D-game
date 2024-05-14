package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.MissingFormatWidthException;

public class GrayBarbObstacle extends Group {


    public GrayBarbObstacle(double x,double z){

        Sphere sphere=new Sphere(10);
        sphere.getTransforms().add(new Translate(0, 10,0));

        PhongMaterial mat=new PhongMaterial(Color.GRAY);
        mat.setSpecularColor(Color.WHITE);
        sphere.setMaterial(mat);

        createTriangularMashes();

        this.getChildren().add(sphere);

        this.setTranslateX(x);
        this.setTranslateZ(z);
    }

    private void createTriangularMashes() {
        TriangleMesh mesh=new TriangleMesh();

        float[] points={
            -1.f,2.f,-1.f,
             1.f,2.f,-1.f,
             1.f,2.f,1.f,
             -1.f,2.f,1.f,
             0.f,-2.0f,0.f
        };

        float[] texCoords={
            0.5f,0.5f
        };

        int[] faces={
            3,0,0,0,4,0,
            2,0,3,0,4,0,
            1,0,2,0,4,0,
            0,0,1,0,4,0,
            0,0,3,0,2,0,
            1,0,0,0,2,0
        };

        mesh.getTexCoords().addAll(texCoords);
        mesh.getPoints().addAll(points);
        mesh.getFaces().addAll(faces);

        MeshView view0=new MeshView();
        view0.setMesh(mesh);

        super.getChildren().add(view0);


        MeshView view1=new MeshView();
        view1.setMesh(mesh);
        view1.getTransforms().addAll(
                new Translate(2,0,0),
                new Rotate(45)
        );
        super.getChildren().add(view1);

        MeshView view2=new MeshView();
        view2.setMesh(mesh);
        view2.getTransforms().addAll(
                new Translate(-2,0,0),
                new Rotate(-45)
        );
        super.getChildren().add(view2);

        MeshView view3=new MeshView();
        view3.setMesh(mesh);
        view3.getTransforms().addAll(
                new Translate(0,0,2),
                new Rotate(-45,new Point3D(1, 0, 0))
        );
        super.getChildren().add(view3);

        MeshView view4=new MeshView();
        view4.setMesh(mesh);
        view4.getTransforms().addAll(
                new Translate(0,0,-2),
                new Rotate(45,new Point3D(1,0,0))
        );
        super.getChildren().add(view4);

    }
}
