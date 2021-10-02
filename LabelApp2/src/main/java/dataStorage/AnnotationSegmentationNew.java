package dataStorage;

import javafx.scene.shape.Polygon;

import java.util.Vector;

public class AnnotationSegmentationNew {
    Vector<Vector<Double>> segmentation;

    public AnnotationSegmentationNew(Vector<Vector<Double>> segmentation){
        this.segmentation = segmentation;
    }
    public AnnotationSegmentationNew(double x, double y, double w, double h, double theta){

        segmentation = new Vector<Vector<Double>>();
        double degree = Math.toDegrees(theta);
        double x1 = x;
        double y1 = y;

        double tempX;
        double tempY;

        //Top left
        tempX = x;
        tempY = y + h;
        double x2 = Math.cos(theta) * (tempX-x1) - Math.sin(theta) * (tempY-y1) + x1;
        double y2 = Math.sin(theta) * (tempX-x1) + Math.cos(theta) * (tempY-y1) + y1;
        //Top right
        tempX = x + w;
        tempY = y + h;
        double x3 = Math.cos(theta) * (tempX-x1) - Math.sin(theta) * (tempY-y1) + x1;
        double y3 = Math.sin(theta) * (tempX-x1) + Math.cos(theta) * (tempY-y1) + y1;
        //Bottom right
        tempX = x + w;
        tempY = y;
        double x4 = Math.cos(theta) * (tempX-x1) - Math.sin(theta) * (tempY-y1) + x1;
        double y4 = Math.sin(theta) * (tempX-x1) + Math.cos(theta) * (tempY-y1) + y1;

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

    public Vector<Polygon> toPolygonVector(){
        Vector<Polygon> retval = new Vector<Polygon>();
        for (Vector<Double> i : segmentation ) {
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll((Double[])i.toArray());
            retval.add(polygon);
        }
        return retval;
    }

}
