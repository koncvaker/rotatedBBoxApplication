package com.example.labelapp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// TODO: 2021. 09. 30. Read xml, generate json rework, 
// TODO: 2021. 09. 26. Generate rectangles from bounding boxes 
// TODO: 2021. 09. 26. Generate rectangles from segmentations 
// TODO: 2021. 09. 26. Create radio button to switch between them
// TODO: 2021. 09. 30. The xml files stores the (xCenter,yCenter) coordinates, for display we need (xMin,yMax) and for train we need (xMin,yMin) 
public class AnnotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AnnotApplication.class.getResource("annot-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setResizable(false);
        stage.setTitle("AnnotationCore app");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}