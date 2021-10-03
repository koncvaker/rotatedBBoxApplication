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
        doubleVector.add(x2);
        doubleVector.add(y2);
        doubleVector.add(x3);
        doubleVector.add(y3);
        doubleVector.add(x4);
        doubleVector.add(y4);
        segmentation.add(doubleVector);
    }


    public void print(){
        for(int i = 0 ; i < 8 ; i+=2){
            System.out.println("y" + i + ": " + segmentation.get(0).get(i));
            int tempi = i+1;
            System.out.println("x" + tempi + ": " + segmentation.get(0).get(i+1));
        }
    }

    public Vector<Vector<Double>> getSegmentation(){
        return segmentation;
    }


    public Vector<Double> toDoubleVector(Double imgRatio) {
        Vector<Double> doubleVector = new Vector<Double>();
        doubleVector.add(segmentation.get(0).get(0)*imgRatio);
        doubleVector.add(segmentation.get(0).get(1)*imgRatio);
        doubleVector.add(segmentation.get(0).get(2)*imgRatio);
        doubleVector.add(segmentation.get(0).get(3)*imgRatio);
        doubleVector.add(segmentation.get(0).get(4)*imgRatio);
        doubleVector.add(segmentation.get(0).get(5)*imgRatio);
        doubleVector.add(segmentation.get(0).get(6)*imgRatio);
        doubleVector.add(segmentation.get(0).get(7)*imgRatio);
        return doubleVector;
    }
}
