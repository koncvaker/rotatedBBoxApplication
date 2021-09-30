package dataStorage;

import java.util.Vector;

public class AnnotationSegmentation {
    double x1;
    double y1;
    double x2;
    double y2;
    double x3;
    double y3;
    double x4;
    double y4;

    public AnnotationSegmentation(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    public AnnotationSegmentation(Vector<Double> values){
        this.x1 = values.get(0);
        this.y1 = values.get(1);
        this.x2 = values.get(2);
        this.y2 = values.get(3);
        this.x3 = values.get(4);
        this.y3 = values.get(5);
        this.x4 = values.get(6);
        this.y4 = values.get(7);
    }

    public AnnotationSegmentation(double x, double y, double w, double h, double theta){
        double degree = Math.toDegrees(theta);
        this.x1 = x;
        this.y1 = y;

        double tempX;
        double tempY;

        //Top right
        tempX = x + w;
        tempY = y;
        this.x2 = Math.cos(theta) * (tempX-this.x1) - Math.sin(theta) * (tempY-this.y1) + this.x1;
        this.y2 = Math.sin(theta) * (tempX-this.x1) + Math.cos(theta) * (tempY-this.y1) + this.y1;
        //Bottom right
        tempX = x + w;
        tempY = y - h;
        this.x3 = Math.cos(theta) * (tempX-this.x1) - Math.sin(theta) * (tempY-this.y1) + this.x1;
        this.y3 = Math.sin(theta) * (tempX-this.x1) + Math.cos(theta) * (tempY-this.y1) + this.y1;
        //Bottom left
        tempX = x;
        tempY = y - h;
        this.x4 = Math.cos(theta) * (tempX-this.x1) - Math.sin(theta) * (tempY-this.y1) + this.x1;
        this.y4 = Math.sin(theta) * (tempX-this.x1) + Math.cos(theta) * (tempY-this.y1) + this.y1;

    }

    public void print(){
        System.out.println("[x1: " + x1 + ",y1: " + y1 + ";x2: " + x2 + ",y2: " + y2 + ";x3: " + x3 + ",y3: " + y3 + ";x4: " + x4 + ",y4: " + y4 + "]");
    }
}
