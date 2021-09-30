package dataStorage;


import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class AnnotationBBox {
    double x;
    double y;
    double width;
    double height;
    //We have to store it in radian
    double theta;

    public AnnotationBBox(double x, double y, double width, double height, double theta){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.theta = theta;
    }
    public AnnotationBBox(Vector<Double> values){
        x       = values.get(0);
        y       = values.get(1);
        width   = values.get(2);
        height  = values.get(3);
        theta   = values.get(4);
        // TODO: 2021. 09. 28. Normalize bbox between -45˙ and 45˙  
        if(theta < Math.PI/4){

        }
        else if(theta > Math.PI/4){
            
        }
    }

    public Rectangle toRectangle(){
        Rectangle retval = new Rectangle();
        retval.setX(x);
        retval.setY(y);
        retval.setWidth(width);
        retval.setHeight(height);

        return retval;
    }

    public void print(){
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("Width: " + width);
        System.out.println("Height: "+ height);
        System.out.println("Theta: " + theta);
    }
}
