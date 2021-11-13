package com.example.labelapp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafxIntegration.Annotation;

import java.io.IOException;
import java.util.Vector;


public class AnnotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AnnotApplication.class.getResource("annot-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setResizable(false);
        stage.setTitle("AnnotationCore app");
        stage.setScene(scene);
        stage.show();
        Vector<Double> temp = new Vector<Double>();
        temp.add(2347.7239921001965);
        temp.add(1541.2761733035225);
        temp.add(2358.690706496559);
        temp.add(1544.332362517006);
        temp.add(2006.6900078998028);
        temp.add(2807.438426696477);
        temp.add(1995.7232935034403);
        temp.add(2804.382237482994);
        Annotation.getRectangleFromCoordinates(temp,1);
    }

    public static void main(String[] args) {
        launch();
    }
}