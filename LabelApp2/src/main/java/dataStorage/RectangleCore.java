package dataStorage;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class RectangleCore {
    private double score;
    private String category;
    private Vector<Rectangle> rectangleVector;
    private Vector<Polygon> polygonVector;


    public Vector<Polygon> getPolygonVector() {
        return polygonVector;
    }

    public void setPolygonVector(Vector<Polygon> polygonVector) {
        this.polygonVector = polygonVector;
    }

    public Vector<Rectangle> getRectangleVector() {
        return rectangleVector;
    }

    public void setRectangleVector(Vector<Rectangle> rectangleVector) {
        this.rectangleVector = rectangleVector;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
