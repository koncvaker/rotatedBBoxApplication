package javafxIntegration;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class Annotation {
    Label label;
    Rectangle bbox;
    double score;
    int type; //Groundproof annotation = 1, groundproof segmentation = 2, annotation = 3, segmentation = 4

    public Annotation(String labelText,double score,int type,double x,double y,double w, double h,double theta){
        Rectangle bbox = new Rectangle();
        bbox.setY(y);
        bbox.setWidth(w);
        bbox.setHeight(h);
        bbox.setX(x);
        bbox.rotateProperty().set(-Math.toDegrees(theta));
        bbox.setFill(Color.TRANSPARENT);
        bbox.setStroke(getColor(type));
        bbox.setArcHeight(5);
        bbox.setArcWidth(5);
        this.bbox = bbox;
    }

    public Annotation(String labelText, double score, int type, Vector<Double> segmentation){


    }

    public Rectangle getRectangleResized(double ratio){
        Rectangle retval = new Rectangle();
        retval.setX(bbox.getX()*ratio);
        retval.setY(bbox.getY()*ratio);
        retval.setWidth(bbox.getWidth()*ratio);
        retval.setHeight(bbox.getHeight()*ratio);
        return retval;
    }

    public static Color getColor(int type){
        if (type == 1){
            return Color.DARKGREEN;
        }
        else if (type == 2){
            return Color.GREEN;
        }
        else if (type == 3){
            return Color.DARKBLUE;
        }
        else if (type == 3){
            return Color.BLUE;
        }
        else{
            return Color.BLACK;
        }
    }

    public static Rectangle getRectangleFromCoordinates(Vector<Double> segmentation,int type){
        double x1;
        double x2;
        double x3;
        double x4;
        double y1;
        double y2;
        double y3;
        double y4;
        x1 = segmentation.get(0);
        y1 = segmentation.get(1);
        x2 = segmentation.get(2);
        y2 = segmentation.get(3);
        x3 = segmentation.get(4);
        y3 = segmentation.get(5);
        x4 = segmentation.get(6);
        y4 = segmentation.get(7);

        double xmin;
        double xsecond;
        double ymin;
        double ysecond;

        xmin = Integer.MAX_VALUE;
        xsecond = Integer.MAX_VALUE;
        ymin = Integer.MAX_VALUE;
        ysecond = Integer.MAX_VALUE;

        for (int i = 0; i < 8 ; i+= 2){
            if (segmentation.get(i) < xmin){
                xsecond = xmin;
                ysecond = ymin;
                xmin = segmentation.get(i);
                ymin = segmentation.get(i+1);
            } else if (segmentation.get(i) < xsecond) {
                xsecond = segmentation.get(i);
                ysecond = segmentation.get(i+1);
            }
        }

        if (ysecond < ymin){
            double changerTemp;
            changerTemp = xmin;
            xmin = xsecond;
            xsecond = changerTemp;
            changerTemp = ymin;
            ymin = ysecond;
            ysecond = changerTemp;
        }


        double width = Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double height = Math.sqrt( Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));

        double theta = Math.toRadians(calculateAngle(segmentation.get(0),segmentation.get(1),segmentation.get(2),segmentation.get(3)));

            x1 = xmin - xsecond;
            y1 = ymin - ysecond;

            x2 = x1 * Math.cos(theta) - y1 * Math.sin(theta);
            y2 = x1 * Math.sin(theta) + y1 * Math.cos(theta);

            xmin = x2 + xsecond;
            ymin = y2 + ysecond;



        System.out.println(xmin);
        System.out.println(ymin);
        System.out.println(width);
        System.out.println(height);
        System.out.println(theta);

        Rectangle bbox = new Rectangle();
        bbox.setX(xmin);
        bbox.setY(ymin);
        bbox.setWidth(width);
        bbox.setHeight(height);
        bbox.rotateProperty().set(-Math.toDegrees(theta));
        bbox.setFill(Color.TRANSPARENT);
        bbox.setStroke(getColor(type));
        bbox.setArcHeight(5);
        bbox.setArcWidth(5);
        return bbox;
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between -45 and +45
        while (angle < -45){
            angle += 90;
        }
        while (angle > 45){
            angle -= 90;
        }
        return angle;
    }
}
