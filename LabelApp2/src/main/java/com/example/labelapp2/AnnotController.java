package com.example.labelapp2;

import dataStorage.DataWorker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public class AnnotController {
    public Button setXMLReadPathBtn;
    public Button setPhotoReadPathBtn;
    public Button setJSONReadPathBtn;
    public Button setJSONWritePathBtn;
    public Button setJSONFileNameBtn;

    public Label photoReadPathLabel;
    public Label jsonReadPathLabel;
    public Label jsonWritePathLabel;
    public Label jsonFileNameLabel;
    public Label xmlReadPathLabel;
    public TextField jsonFilenameTextField;

    @FXML
    private Label imageFileName;
    @FXML
    private ImageView imageView;
    @FXML
    private Button JSONOpenerBtn;
    @FXML
    private Button imageShowerBtn;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button ImageOpenerBtn;
    @FXML
    private Button AnnotationShowBtn;
    @FXML
    private Button AnnotationClearBtn;
    @FXML
    private Pane RectanglePane;
    @FXML
    private Label w1;
    @FXML
    private Label w2;
    @FXML
    private Label w3;
    @FXML
    private Label h1;
    @FXML
    private Label h2;
    @FXML
    private Label h3;



    private String readXMLPath;
    private String writeJSONPath;
    private String readImgFolderPath;
    private String readJSONPath;
    private String writeJSONFileName;
    private int imgId;
    private String[] readImages;
    private final DataWorker dataStorage = new DataWorker();
    public double imgWidth;
    public double imgHeight;


    @FXML
    protected void getXMLReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        readXMLPath = file.getPath();
        xmlReadPathLabel.setText(readXMLPath);
    }
    @FXML
    protected void getPhotoReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        readImgFolderPath = file.getPath();
        photoReadPathLabel.setText(readImgFolderPath);
    }
    @FXML
    protected void getJSONReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        readJSONPath = file.getPath();
        jsonReadPathLabel.setText(readJSONPath);
    }
    @FXML
    protected void getJSONWritePath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        writeJSONPath = file.getPath();
        jsonWritePathLabel.setText(writeJSONPath);
    }
    @FXML
    protected void getJSONFilenamePath(){

        writeJSONFileName = jsonFilenameTextField.getText();
        jsonFileNameLabel.setText(writeJSONFileName);
    }


    @FXML
    protected void JSONOpen() {
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        readJSONPath = file.getPath();
        dataStorage.readJSON(readJSONPath);
        System.out.println(file);
    }

    @FXML
    protected void FolderOpener() {
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File file = directoryChooser.showDialog(stage);
        SetFileList(file);

        System.out.println(file);
    }

    @FXML
    protected void StartImage() {

        imgId = 0;
        SetImage();

    }

    @FXML
    protected void NextImage() {
        if (imgId < readImages.length -1) {
            imgId = imgId + 1;
            SetImage();
        } else {
            return;
        }

    }

    @FXML
    protected void PrevImage() {
        if (imgId > 0) {
            imgId = imgId - 1;
            SetImage();
        } else {
            return;
        }


    }

    private void SetImage() {
        if (readImages.length > 0) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(readImgFolderPath + '\\' + readImages[imgId]);
                Image image = new Image(input);

                imageView.setImage(image);
                w1.setText(Double.toString(imageView.getFitWidth()));
                w2.setText(Double.toString(imageView.boundsInParentProperty().get().getWidth()));
                w3.setText(Double.toString(image.getWidth()));
                h1.setText(Double.toString(imageView.getFitHeight()));
                h2.setText(Double.toString(imageView.boundsInParentProperty().get().getHeight()));
                h3.setText(Double.toString(image.getHeight()));
                imageFileName.setText(readImages[imgId]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    protected void GenerateRectangles(){
        Rectangle r = new Rectangle();
        r.setWidth(142.1402);
        r.setHeight(206.6305);
        r.setX(1);
        r.setY(1);
        r.setFill(Color.TRANSPARENT);

        r.setStroke(Color.BLACK);
        r.setArcHeight(5);
        r.setArcWidth(5);
        RectanglePane.getChildren().add(r);
    }
    @FXML
    protected void RemoveRectangles(){
        RectanglePane.getChildren().removeAll();
        RectanglePane.getChildren().clear();

    }



    private void SetFileList(File files){
        File[] listOfFiles = files.listFiles();
        Vector<String> tempVector = new Vector<String>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].isHidden()) {
                tempVector.add(listOfFiles[i].getName());
                System.out.println(listOfFiles[i].getName());
            }
        }
        System.out.println(tempVector.size());
        readImages = tempVector.toArray(new String[tempVector.size()]);
        readImgFolderPath = files.getPath();

        imageShowerBtn.setDisable(tempVector.size() <= 0 || readImgFolderPath.length() <= 0);
    }
}