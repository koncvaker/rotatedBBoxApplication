package dataStorage;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.awt.*;
import java.util.Vector;

public class AnnotationSegmentationNew {
    Vector<Vector<Double>> segmentation;

    public AnnotationSegmentationNew(Vector<Vector<Double>> segmentation){
        this.segmentation = segmentation;
    }
    public AnnotationSegmentationNew(double x, double y, double w, double h, double theta){

        segmentation = new Vector<Vector<Double>>();

        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double x4;
        double y4;

        double px;
        double py;

        //Center
        double ox = x + w/2;
        double oy = y + h/2;

        double angle;
        if(theta < 0){
            angle = Math.toRadians(360 + Math.toDegrees(theta));
        }
        else {
            angle = theta;
        }
        angle *= -1;
        //Top left
        x1 = Math.cos(angle) * (x - ox) - Math.sin(angle) * (y - oy) + ox;
        y1 = Math.sin(angle) * (x - ox) + Math.cos(angle) * (y - oy) + oy;

        //Top right
        px = x + w;
        py = y;
        x2 = Math.cos(angle) * (px - ox) - Math.sin(angle) * (py - oy) + ox;
        y2 = Math.sin(angle) * (px - ox) + Math.cos(angle) * (py - oy) + oy;

        //Bottom right
        px = x + w;
        py = y + h;
        x3 = Math.cos(angle) * (px - ox) - Math.sin(angle) * (py - oy) + ox;
        y3 = Math.sin(angle) * (px - ox) + Math.cos(angle) * (py - oy) + oy;

        //Bottom left
        px = x;
        py = y + h;
        x4 = Math.cos(angle) * (px - ox) - Math.sin(angle) * (py - oy) + ox;
        y4 = Math.sin(angle) * (px - ox) + Math.cos(angle) * (py - oy) + oy;

        Vector<Double> doubleVector = new Vector<Double>();
        doubleVector.add(x1);
        doubleVector.add(y1);
        segmentation.add(doubleVector);
        doubleVector = new Vector<Double>();
        doubleVector.add(x2);
        doubleVector.add(y2);
        segmentation.add(doubleVector);
        doubleVector = new Vector<Double>();
        doubleVector.add(x3);
        doubleVector.add(y3);
        segmentation.add(doubleVector);
        doubleVector = new Vector<Double>();
        doubleVector.add(x4);
        doubleVector.add(y4);
        segmentation.add(doubleVector);
    }


    public void print(){
        int vectorCounter = 1;
        for(Vector<Double> i : segmentation){
            System.out.println("y" + vectorCounter + ": " + i.get(0));
            System.out.println("x" + vectorCounter + ": " + i.get(1));
            vectorCounter++;
        }
    }

    public Vector<Vector<Double>> getSegmentation(){
        return segmentation;
    }


    public Vector<Double> toDoubleVector(Double imgRatio) {
        Vector<Double> doubleVector = new Vector<Double>();
        for(Vector<Double> i : segmentation){
            doubleVector.add(i.get(0)*imgRatio);
            doubleVector.add(i.get(1)*imgRatio);
        }
        return doubleVector;
    }
}
